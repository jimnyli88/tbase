package cn.com.ut.core.common.resource.fileoperation;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.com.ut.core.common.resource.ResourceType;
import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.validator.Error;

/**
 * 文件上传工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class FileUploadUtil {

	/**
	 * 上传的图片文件允许的最大尺寸
	 */
	public static final long IMAGE_MAX_SIZE = 1024 * 1024 * 2;

	/**
	 * 上传的文件允许的最大尺寸
	 */
	public static final long File_MAX_SIZE = 1024 * 1024 * 100;

	/**
	 * 上传的图片文件允许的格式
	 */
	public static final String[] IMAGE_EXT = new String[] { "jpg", "jpeg", "gif", "png", "bmp" };
	/**
	 * 上传的视频文件允许的格式
	 */
	public static final String[] VIDEO_EXT = new String[] { "flv", "avi", "mp4", "wma", "rmvb",
			"rm", "mid", "3gp" };

	/**
	 * 文件类型
	 */
	public static interface FILE_TYPE {
		public static final String MEDIA = "media";
		public static final String FLASH = "flash";
		public static final String FILE = "file";
		public static final String IMAGE = "image";
	}

	/**
	 * 资源类型
	 */
	public static final HashMap<String, String> resType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(ResourceType.IMG, "gif,jpg,jpeg,png,bmp");
			put(ResourceType.AUDIO, "mp3,wav,wma,mid");
			put(ResourceType.VIDEO, "swf,flv,wmv,avi,mpg,asf,rm,rmvb,mp4");
			put(ResourceType.DOC, "doc,docx,xls,xlsx,ppt,pptx,pdf");
			put(ResourceType.OTHER, "htm,html,txt,zip,rar,gz,bz2,htm,html,txt,zip,rar,gz,bz2");
		}
	};

	/**
	 * 定义允许上传的文件扩展名
	 */
	public static final HashMap<String, String> extMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(FILE_TYPE.IMAGE, "gif,jpg,jpeg,png,bmp");
			put(FILE_TYPE.FLASH, "swf,flv");
			put(FILE_TYPE.MEDIA, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			put(FILE_TYPE.FILE, "doc,docx,xls,xlsx,ppt,pptx,pdf,htm,html,txt,zip,rar,gz,bz2");
		}
	};

	/**
	 * HDFS图片保存路径
	 */
	public static final String HDFS_IMAGE_PATH = "/image/";

	/**
	 * HDFS文件保存路径
	 */
	public static final String HDFS_FILE_PATH = "/file/";

	/**
	 * 临时分配内容大小
	 */
	public static final int TEMP_MEMORY_SIZE = 1024 * 512;

	/**
	 * 验证图片上传类型
	 * 
	 * @param fileName
	 * @return 如果可以上传文件类型在允许类型中，返回true，否则返回false
	 */
	public static boolean checkImage(String fileName) {

		return Arrays.asList(IMAGE_EXT).contains(fileName.substring(fileName.lastIndexOf(".") + 1));
	}

	/**
	 *
	 * @param filePath
	 * @return DiskFileItemFactory
	 */
	public static DiskFileItemFactory getDiskFileItemFactory(String filePath) {

		// 实例化一个硬盘文件工厂，用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		// 设置上传文件时用于临时存放文件的内存大小，多于的部分将临时存在硬盘
		dfif.setSizeThreshold(TEMP_MEMORY_SIZE);
		// 设置存放临时文件和上传文件的目录
		File uploadFile = new File(filePath);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}

		dfif.setRepository(uploadFile);
		return dfif;
	}

	/**
	 *
	 * @param dfif
	 * @param request
	 * @return ServletFileUpload
	 */
	public static ServletFileUpload getServletFileUpload(DiskFileItemFactory dfif,
			HttpServletRequest request) {

		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		// 设置单个文件最大上传尺寸
		sfu.setFileSizeMax(File_MAX_SIZE);
		// 设置最大上传尺寸
		sfu.setSizeMax(File_MAX_SIZE * 4);

		return sfu;
	}

	/**
	 * 检验上传文件： 返回0代表校验通过； 返回1代表没有上传的文件； 返回2代表上传文件列表中包含无效文件； 返回3代表上传文件的大小超过了最大限制；
	 * 
	 * @param fileItems
	 * @param respResult
	 * @return 返回检查结果编码
	 */
	public static int checkFileItem(List<FileItem> fileItems, Error error) {

		String msg = "";
		int result = 0;

		if (CollectionUtil.isEmptyCollection(fileItems)) {
			error.setErrMsg("没有上传的文件");
			result = 1;
			return result;
		}

		// 文件大小
		long size = 0;
		// 客户端完整路径
		String filePath = null;

		for (FileItem fileItem : fileItems) {

			filePath = fileItem.getName();
			size = fileItem.getSize();
			if ("".equals(filePath) || size == 0) {
				msg = "上传文件列表中包含无效文件";
				error.setErrMsg(msg);
				result = 2;
				break;
			}

			if (size > File_MAX_SIZE) {
				msg = "上传的文件【" + filePath + "】尺寸过大，文件尺寸不允许超过：" + File_MAX_SIZE / (1024 * 1024)
						+ "MB";
				error.setErrMsg(msg);
				result = 3;
				break;
			}

			error.setErrMsg(msg);
		}
		return result;
	}

	/**
	 * 检验上传图片： 返回0代表校验通过； 返回1代表没有上传的图片； 返回2代表上传图片列表中包含无效文件； 返回3代表上传图片的大小超过了最大限制；
	 * 返回4代表上传格式不允许；
	 * 
	 * @param fileItems
	 * @param respResult
	 * @return 返回检查结果编码
	 */
	public static int checkImage(List<FileItem> fileItems, Error error) {

		String msg = "";
		int result = 0;

		if (CollectionUtil.isEmptyCollection(fileItems)) {
			error.setErrMsg("没有上传的文件");
			result = 1;
			return result;
		}

		// 文件大小
		long size = 0;
		// 客户端完整路径
		String filePath = null;
		// 文件扩展名
		String fileExt = null;
		List<String> extList = Arrays.asList(IMAGE_EXT);

		for (FileItem fileItem : fileItems) {

			filePath = fileItem.getName();
			size = fileItem.getSize();
			if ("".equals(filePath) || size == 0) {
				msg = "上传文件列表中包含无效文件";
				// respResult.addErrMsg(msg);
				result = 2;
				break;
			}

			if (size > IMAGE_MAX_SIZE) {
				msg = "上传的文件【" + filePath + "】尺寸过大，文件尺寸不允许超过：" + IMAGE_MAX_SIZE / (1024 * 1024)
						+ "MB";
				error.setErrMsg(msg);
				result = 3;
				break;
			}

			fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);

			// 判断文件类型是否合法
			boolean allowedFlag = extList.contains(fileExt.toLowerCase());
			if (!allowedFlag) {
				msg = "上传的文件【" + filePath + "】格式无效，必须是以下的这些格式：" + extList.toString();
				error.setErrMsg(msg);
				result = 4;
				break;
			}

			error.setErrMsg(msg);
		}
		return result;
	}

	/**
	 * 校验上传的视频文件
	 * 
	 * @param fileItems
	 * @param respResult
	 * @return 返回校验结果编码
	 */
	public static int checkVideo(List<FileItem> fileItems, Error error) {

		String msg = "";
		int result = 0;

		if (CollectionUtil.isEmptyCollection(fileItems)) {
			error.setErrMsg("没有上传的文件");
			result = 1;
			return result;
		}

		// 文件大小
		long size = 0;
		// 客户端完整路径
		String filePath = null;
		// 文件扩展名
		String fileExt = null;
		List<String> extList = Arrays.asList(VIDEO_EXT);

		for (FileItem fileItem : fileItems) {

			filePath = fileItem.getName();
			size = fileItem.getSize();
			if ("".equals(filePath) || size == 0) {
				msg = "上传文件列表中包含无效文件";
				error.setErrMsg(msg);
				result = 2;
				break;
			}

			// if (size > IMAGE_MAX_SIZE) {
			// msg = "上传的文件【" + filePath + "】尺寸过大，文件尺寸不允许超过：" + IMAGE_MAX_SIZE /
			// (1024 * 1024)
			// + "MB";
			// respResult.addErrMsg(msg);
			// result = 3;
			// break;
			// }

			fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);

			// 判断文件类型是否合法
			boolean allowedFlag = extList.contains(fileExt.toLowerCase());
			if (!allowedFlag) {
				msg = "上传的文件【" + filePath + "】格式无效，必须是以下的这些格式：" + extList.toString();
				error.setErrMsg(msg);
				result = 4;
				break;
			}

			error.setErrMsg(msg);
		}
		return result;
	}
}
