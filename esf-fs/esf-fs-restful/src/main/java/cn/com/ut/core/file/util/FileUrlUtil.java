package cn.com.ut.core.file.util;

import java.util.HashMap;

import cn.com.ut.core.common.util.CommonUtil;

public class FileUrlUtil {

	private static interface ResourceType {
		String IMG = "img";
		String VIDEO = "video";
		String DOC = "doc";
	}

	static HashMap<String, String> tppeMap = new HashMap<String, String>() {
		{
			put("gif", ResourceType.IMG);
			put("jpg", ResourceType.IMG);
			put("jpeg", ResourceType.IMG);
			put("png", ResourceType.IMG);
			put("bmp", ResourceType.IMG);

			put("swf", ResourceType.VIDEO);
			put("flv", ResourceType.VIDEO);

			put("mp3", ResourceType.VIDEO);
			put("wav", ResourceType.VIDEO);
			put("wma", ResourceType.VIDEO);
			put("wmv", ResourceType.VIDEO);
			put("mid", ResourceType.VIDEO);
			put("avi", ResourceType.VIDEO);
			put("mpg", ResourceType.VIDEO);
			put("asf", ResourceType.VIDEO);
			put("rm", ResourceType.VIDEO);
			put("rmvb", ResourceType.VIDEO);
			put("mp4", ResourceType.VIDEO);
		}
	};

	/**
	 * 根据上传文件名称获取一个URL
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getOssUrl(String fullName) {

		String type = getFileType(fullName);
		StringBuffer url = new StringBuffer();
		url.append(type).append("/").append(fullName);
		return url.toString();
	}

	/**
	 * 获取全名
	 * 
	 * @param userId
	 * @param fileName
	 * @return
	 */
	public static String getFullFileName(String userId, String fileName) {

		if (CommonUtil.isEmpty(userId) || userId.length() > 33) {
			userId = "systemFile";
		}
		StringBuffer url = new StringBuffer();
		if (fileName.indexOf(".") == -1) {
			url.append(userId).append("/").append(CommonUtil.getUUID());
			return url.toString();
		}
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		url.append(userId).append("/").append(CommonUtil.getUUID()).append(".").append(fileExt);
		return url.toString();
	}

	private static String getFileType(String fileName) {

		if (CommonUtil.isEmpty(fileName) || fileName.indexOf(".") == -1) {
			return ResourceType.DOC;
		}
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String type = tppeMap.get(fileExt);
		if (CommonUtil.isEmpty(type)) {
			return ResourceType.DOC;
		}
		return type;
	}

	/**
	 * 获取旧框架的Type
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getOldFileType(String fileName) {

		switch (getFileType(fileName)) {
		case ResourceType.IMG:
			return cn.com.ut.core.common.resource.ResourceType.IMG;
		case ResourceType.VIDEO:
			return cn.com.ut.core.common.resource.ResourceType.VIDEO;
		default:
			return cn.com.ut.core.common.resource.ResourceType.DOC;
		}
	}

}
