package cn.com.ut.core.common.fs.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;

public interface FsOperation {

	/**
	 * 下载到本地
	 * 
	 * @param serverPath
	 *            服务器路径（key）
	 * @param localPath
	 *            本地路径
	 */
	void downLoadToLocal(String serverPath, File localPath);

	/**
	 * 获取输出流 alioss 这个方法实现有争议
	 * 
	 * @param serverPath
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputstreamByPath(String serverPath) throws IOException;

	/**
	 * 上传文件
	 * 
	 * @param in
	 * @param serverPath
	 * @throws IOException
	 */
	void fileUpload(InputStream in, String serverPath) throws IOException;

	/**
	 * 判断文件是否存在
	 * 
	 * @param serverPath
	 * @return
	 * @throws IOException
	 */
	boolean existsFile(String serverPath) throws IOException;

	/**
	 * 追加上传文件
	 * 
	 * @param in
	 * @param serverPath
	 * @param beginsize
	 *            开始位置 如果是空则从文件最末端续加
	 * @throws IOException
	 */
	void append(InputStream in, String serverPath, Integer beginsize) throws IOException;

	/**
	 * 获取文件的MD5 阿里OSS官方没有提供直接的方法
	 * 
	 * @param serverPath
	 *            服务器路径
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	String getMD5ByPath(String serverPath) throws IOException, NoSuchAlgorithmException;

	/**
	 * 删除文件
	 * 
	 * @param serverPath
	 * @return
	 * @throws IOException
	 */
	boolean delete(String serverPath) throws IOException;

	/**
	 * 获取所有文件的路径
	 * 
	 * @param serverDir
	 * @return
	 * @throws IOException
	 */
	List<Hashtable<String, Object>> listAllFile(String serverDir) throws IOException;

	/**
	 * 查询文件的字节数
	 * 
	 * @param serverPath
	 * @return
	 * @throws IOException
	 */
	long getBlockSize(String serverPath) throws IOException;

	/**
	 * 获取文件流
	 * 
	 * @param serverPath
	 * @return
	 */
	InputStream getInputStream(String serverPath) throws IOException;

	/**
	 * 获取阿里ossget请求的url
	 * 
	 * @param serverPath
	 * @param height
	 * @param width
	 * @param type
	 * @return
	 */
	String getUrl(String serverPath, int height, int width, String type);

}
