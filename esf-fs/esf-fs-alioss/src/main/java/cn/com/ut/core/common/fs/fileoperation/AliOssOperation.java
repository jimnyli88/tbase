package cn.com.ut.core.common.fs.fileoperation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.internal.Mimetypes;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.SimplifiedObjectMeta;

import cn.com.ut.core.common.fs.api.FsOperation;
import cn.com.ut.core.common.resource.ResourceType;
import cn.com.ut.core.common.util.CommonUtil;

public class AliOssOperation implements FsOperation {

	private OSSClient oSSClient;

	private String region;

	private String bucketName;

	public void setRegion(String region) {

		if (this.region == null) {
			this.region = region;
		}
	}

	public String getBucketName() {

		return bucketName;
	}

	public void setBucketName(String bucketName) {

		this.bucketName = bucketName;
	}

	public OSSClient getoSSClient() {

		return oSSClient;
	}

	public void setoSSClient(OSSClient oSSClient) {

		this.oSSClient = oSSClient;
	}

	@Override
	public void downLoadToLocal(String serverPath, File localPath) {

		serverPath = hdfsTooss(serverPath);
		try {
			// 下载object到文件
			oSSClient.getObject(new GetObjectRequest(bucketName, serverPath), localPath);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public OutputStream getOutputstreamByPath(String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		throw new UnsupportedOperationException();
	}

	@Override
	public void fileUpload(InputStream in, String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		if (serverPath == null || "".equals(serverPath.trim())) {
			throw new RuntimeException("serverPath is null");
		}
		ObjectMetadata metadata = null;
		metadata = new ObjectMetadata();
		metadata.setContentType(Mimetypes.getInstance().getMimetype(serverPath));
		oSSClient.putObject(bucketName, serverPath, in, metadata);
	}

	@Override
	public boolean existsFile(String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		boolean found = false;
		found = oSSClient.doesObjectExist(bucketName, serverPath);
		return found;
	}

	@Override
	public void append(InputStream in, String serverPath, Integer beginsize) throws IOException {

		serverPath = hdfsTooss(serverPath);
		appendUploadObject(in, serverPath);

	}

	/**
	 * alioss 没提供
	 */
	@Override
	public String getMD5ByPath(String serverPath) throws IOException, NoSuchAlgorithmException {

		serverPath = hdfsTooss(serverPath);
		SimplifiedObjectMeta res = getObjectMsg(serverPath);
		if (res == null) {
			return null;
		}
		return res.getETag();
	}

	@Override
	public boolean delete(String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		// 删除Object
		oSSClient.deleteObject(bucketName, serverPath);
		return true;
	}

	@Override
	public List<Hashtable<String, Object>> listAllFile(String serverDir) throws IOException {

		serverDir = hdfsTooss(serverDir);
		// 列举Object
		ObjectListing objectListing = oSSClient.listObjects(bucketName, serverDir);
		List<OSSObjectSummary> sums = objectListing.getObjectSummaries();

		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		List<Hashtable<String, Object>> fileList = new ArrayList<Hashtable<String, Object>>();
		if (sums == null || sums.size() < 1) {
			return null;
		}
		for (OSSObjectSummary s : sums) {
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			String path = s.getKey();
			if (path.contains("/")) {
				hash.put("is_dir", true);
				hash.put("has_file", true);
				hash.put("filesize", 0L);
				hash.put("is_photo", false);
				hash.put("filetype", "");
			} else {
				String fileExt = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
				hash.put("is_dir", false);
				hash.put("has_file", false);
				hash.put("filesize", s.getSize());
				hash.put("is_photo", Arrays.<String> asList(fileTypes).contains(fileExt));
				hash.put("filetype", fileExt);
			}
			String filename = path.substring(path.lastIndexOf("/") + 1);
			if ((boolean) hash.get("is_photo")) {
				if (filename.matches("_\\d+×\\d+\\.\\w+")) {
					filename = filename.substring(0, filename.lastIndexOf("_"));
				}
			}
			hash.put("filename", filename);
			boolean isExists = false;
			for (Hashtable<String, Object> elem : fileList) {
				if (elem.get("filename").equals(hash.get(("filename")))) {
					isExists = true;
					break;
				}
			}
			if (!isExists)
				fileList.add(hash);
		}
		return fileList;
	}

	@Override
	public long getBlockSize(String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		SimplifiedObjectMeta res = getObjectMsg(serverPath);
		if (res == null) {
			return 0;
		}
		return res.getSize();
	}

	@Override
	public InputStream getInputStream(String serverPath) throws IOException {

		serverPath = hdfsTooss(serverPath);
		InputStream in = null;
		OSSObject ossObject = oSSClient.getObject(bucketName, serverPath);
		in = ossObject.getObjectContent();
		return in;
	}

	/**
	 * 获取geturl
	 * 
	 * @return
	 */
	@Override
	public String getUrl(String serverPath, int height, int width, String type) {

		if (CommonUtil.isEmpty(serverPath)) {
			return null;
		}
		serverPath = hdfsTooss(serverPath);
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(bucketName).append(".").append(region).append(".aliyuncs.com")
				.append("/");
		try {
			if (serverPath.lastIndexOf("/") > 0) {
				sb.append(serverPath.substring(0, serverPath.lastIndexOf("/") + 1));
				sb.append(URLEncoder.encode(
						serverPath.substring(serverPath.lastIndexOf("/") + 1, serverPath.length()),
						"UTF-8"));
			} else {
				sb.append(URLEncoder.encode(serverPath, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String style = getStyle(height, width, type);
		if (CommonUtil.isNotEmpty(style)) {
			sb.append("?").append(getStyle(height, width, type));
		}
		return sb.toString();
	}

	private String getStyle(int height, int width, String type) {

		if (!ResourceType.IMG.equals(type)) {
			return null;
		}
		if (height < 1 && width < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("x-oss-process=image/resize,m_fill");
		if (height > 0) {
			sb.append(",h_").append(height);
		}
		if (width > 0) {
			sb.append(",w_").append(width);
		}
		return sb.toString();
	}

	/**
	 * 追加上传
	 * 
	 * @return
	 */
	private AppendObjectResult appendUploadObject(InputStream in, String key) {

		AppendObjectResult result = null;
		boolean found = oSSClient.doesObjectExist(bucketName, key);
		long size = 0;
		if (found) {
			SimplifiedObjectMeta objectMeta = oSSClient.getSimplifiedObjectMeta(bucketName, key);
			if (objectMeta != null) {
				size = objectMeta.getSize();
			}
		}

		ObjectMetadata metadata = null;
		metadata = new ObjectMetadata();
		metadata.setContentType(Mimetypes.getInstance().getMimetype(key));
		AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName, key, in,
				metadata);
		appendObjectRequest.setPosition(size);
		result = oSSClient.appendObject(appendObjectRequest);
		return result;
	}

	/**
	 * 获取文件轻量信息（速度快）
	 * 
	 * @param bucketName
	 * @param key
	 * @return
	 */
	private SimplifiedObjectMeta getObjectMsg(String key) {

		SimplifiedObjectMeta objectMeta = null;
		boolean found = oSSClient.doesObjectExist(bucketName, key);
		if (!found) {
			return objectMeta;
		}
		objectMeta = oSSClient.getSimplifiedObjectMeta(bucketName, key);
		return objectMeta;
	}

	/**
	 * 把hadoop路径转换为oss路径
	 * 
	 * @param serverPath
	 * @return
	 */
	private String hdfsTooss(String serverPath) {

		if (CommonUtil.isEmpty(serverPath)) {
			return serverPath;
		}
		if ('/' == serverPath.charAt(0)) {
			return serverPath.substring(1, serverPath.length());
		}
		return serverPath;
	}
}