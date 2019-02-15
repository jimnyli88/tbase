package cn.com.ut.core.restful.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.com.ut.core.common.fs.api.FsOperation;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.ExceptionUtil;
import cn.com.ut.core.file.util.FileUrlUtil;

@Controller
@RequestMapping
public class FileSystemController {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemController.class);

	@Autowired
	private FsOperation fsOperation;

	/**
	 * 实现文件上传
	 */
	@RequestMapping(value = "/UploadjsonServlet", method = { RequestMethod.POST })
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "userid", required = false) String userid) {

		// 这个是返回给前台的文件名 为了简短分两步走
		String fullFileName = FileUrlUtil.getFullFileName(userid, file.getOriginalFilename());
		String ossUrl = FileUrlUtil.getOssUrl(fullFileName);
		// 上传文件
		try {
			fsOperation.fileUpload(file.getInputStream(), ossUrl);
		} catch (IOException e) {
			ExceptionUtil.throwServiceException("上传文件发生异常");
		}
		return fullFileName;
	}

	/**
	 * 文件下载
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/DfsDownLoad")
	public void download(HttpServletResponse response, @RequestParam(name = "url") String url,
			@RequestParam(name = "width", required = false) Integer width,
			@RequestParam(name = "height", required = false) Integer height) throws IOException {

		String ossUrl = FileUrlUtil.getOssUrl(url);
		if (width == null) {
			width = 0;
		}
		if (height == null) {
			height = 0;
		}
		String resurl = fsOperation.getUrl(ossUrl, height, width,
				FileUrlUtil.getOldFileType(ossUrl));
		if (CommonUtil.isNotEmpty(resurl)) {
			response.sendRedirect(resurl);
			return;
		}

	}

}
