package cn.com.ut.core.common.resource.fileoperation;

import java.util.HashMap;

import cn.com.ut.core.common.resource.ResourceType;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.PropertiesUtil;

public class FileOperationUtil {

	private static String fileSystem;

	/**
	 * HDFS压缩图片保存路径
	 */
	// public static final String HDFS_SCALE_IMAGE_PATH = "/image/scaleimg/";

	public static final String baseServerPath = "/UIF_CORE_WEB/UploadjsonServlet";
	public static final String imageServerPath = "/UIF_CORE_WEB/FileUpload";

	public static interface HDFS_PATH {
		public static final String MEDIA = "/media/";
		public static final String FLASH = "/flash/";
		public static final String FILE = "/file/";
		public static final String IMAGE = "/image/";
	}

	public static interface FILE_TYPE {
		public static final String MEDIA = "media";
		public static final String FLASH = "flash";
		public static final String FILE = "file";
		public static final String IMAGE = "image";
	}

	public static final HashMap<String, String> resType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(ResourceType.IMG, "gif,jpg,jpeg,png,bmp");
			put(ResourceType.AUDIO, "mp3,wav,wma,mid");
			put(ResourceType.VIDEO, "swf,flv,wmv,avi,mpg,asf,rm,rmvb,mp4");
			put(ResourceType.DOC, "doc,docx,xls,xlsx,ppt,pptx,pdf");
			put(ResourceType.OTHER, "htm,html,txt,zip,rar,gz,bz2");
		}
	};

	// 定义允许上传的文件扩展名
	public static final HashMap<String, String> extMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(FILE_TYPE.IMAGE, "gif,jpg,jpeg,png,bmp");
			put(FILE_TYPE.FLASH, "swf,flv");
			put(FILE_TYPE.MEDIA, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
			put(FILE_TYPE.FILE, "doc,docx,xls,xlsx,ppt,pptx,pdf,htm,html,txt,zip,rar,gz,bz2");
		}
	};

	static {
		fileSystem = PropertiesUtil.getProperty("file.system");
	}

	/**
	 * 获取文件存储的路径
	 * 
	 * @param type
	 * @param userName
	 * @return
	 */
	public static String getDir(String type, String userName) {

		String dir = getHdfsDir(type) + userName + "/";
		if ("alioss".equals(fileSystem)) {
			return hdfsTooss(dir);
		}
		return dir;
	}

	/**
	 * 把hadoop路径转换为oss路径
	 * 
	 * @param serverPath
	 * @return
	 */
	private static String hdfsTooss(String serverPath) {

		if (CommonUtil.isEmpty(serverPath)) {
			return serverPath;
		}
		if ('/' == serverPath.charAt(0)) {
			return serverPath.substring(1, serverPath.length());
		}
		return serverPath;
	}

	public static String getHdfsDir(String type) {

		switch (type) {
		case FILE_TYPE.IMAGE:
			// return HDFS_PATH.IMAGE;
			return HDFS_PATH.IMAGE;
		case FILE_TYPE.FLASH:
			return HDFS_PATH.FLASH;
		case FILE_TYPE.FILE:
			return HDFS_PATH.FILE;
		case FILE_TYPE.MEDIA:
			return HDFS_PATH.MEDIA;
		default:
			return "tem/";
		}
	}

	public static String getFileType(String fileExt) {

		if (extMap.get(FILE_TYPE.IMAGE).contains(fileExt)) {
			return FILE_TYPE.IMAGE;
		} else if (extMap.get(FILE_TYPE.FLASH).contains(fileExt)) {
			return FILE_TYPE.FLASH;
		} else if (extMap.get(FILE_TYPE.MEDIA).contains(fileExt)) {
			return FILE_TYPE.MEDIA;
		} else if (extMap.get(FILE_TYPE.FILE).contains(fileExt)) {
			return FILE_TYPE.FILE;
		} else {
			return null;
		}

	}

	public static String getResourceType(String fileExt) {

		if (resType.get(ResourceType.IMG).contains(fileExt)) {
			return ResourceType.IMG;
		} else if (resType.get(ResourceType.AUDIO).contains(fileExt)) {
			return ResourceType.AUDIO;
		} else if (resType.get(ResourceType.VIDEO).contains(fileExt)) {
			return ResourceType.VIDEO;
		} else if (resType.get(ResourceType.DOC).contains(fileExt)) {
			return ResourceType.DOC;
		} else {
			return ResourceType.OTHER;
		}

	}

}
