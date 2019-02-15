package cn.com.ut.core.restful.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.ut.core.common.fs.api.FsOperation;
import cn.com.ut.core.common.resource.fileoperation.FileUploadUtil;

@Controller
@RequestMapping
public class FileDownLoadController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownLoadController.class);

    @Autowired
    private FsOperation fsOperation;

    /**
     * 支持中文，文件名长度无限制，不支持国际化
     */
    @RequestMapping(value = "/FileDownLoad/**", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;

        String filePath = request.getParameter("filePath");
        String fileName = null;
        int index = -1;
        if ((index = filePath.lastIndexOf("/")) != -1) {
            fileName = filePath.substring(index + 1);
        } else {
            fileName = filePath;
        }

        String fullPath = filePath;
        if (!filePath.startsWith("/")) {
            fullPath = FileUploadUtil.HDFS_FILE_PATH + filePath;
        }

        InputStream input = null;
        try {
            input = fsOperation.getInputStream(fullPath);
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(input.available()));

            bis = new BufferedInputStream(input);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        } finally {
            IOUtils.closeQuietly(input, bis, bos);
        }
    }

    /**
     * 该方法支持支持国际化，但是文件名不能超过17个汉字，而且在IE6下存在bug
     */
    public void downloadI18N(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;

        String filePath = request.getParameter("filePath");
        String fileName = request.getParameter("fileName");
        try {
            long fileLength = new File(filePath).length();

            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(fileLength));

            bis = new BufferedInputStream(new FileInputStream(filePath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

}
