package cn.com.ut.core.common.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * JPEG图片压缩
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class JpegScale {

	/**
	 * 按照指定比例压缩图片
	 * 
	 * @param srcImg
	 * @param out
	 * @param scale
	 * @return 图片压缩后的长宽数组
	 * @throws IOException
	 */
	public static int[] jpegScaling(BufferedImage srcImg, OutputStream out, double scale)
			throws IOException {

		int w = (int) (srcImg.getWidth() * scale);
		int h = (int) (srcImg.getHeight() * scale);
		w = Math.max(w, 1);
		h = Math.max(h, 1);

		// AffineTransformOp ato = new AffineTransformOp(
		// AffineTransform.getScaleInstance(scale, scale), null);
		// BufferedImage drawImg = new BufferedImage(w, h,
		// BufferedImage.TYPE_3BYTE_BGR);
		// ato.filter(srcImg, drawImg);

		BufferedImage dstImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dstImg.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.drawImage(srcImg.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		g.dispose();

		ImageIO.write(dstImg, "jpeg", out);

		return new int[] { w, h };
	}

	/**
	 * 按照指定宽压缩图片
	 * 
	 * @param srcImg
	 * @param out
	 * @param wid
	 * @return 图片压缩后的长宽数组
	 * @throws IOException
	 */
	public static int[] jpegScalingByWidth(BufferedImage srcImg, OutputStream out, int wid)
			throws IOException {

		int width = srcImg.getWidth();
		int height = srcImg.getHeight();
		height = (int) (height * wid);
		int w = wid;
		int h = height / width;
		w = Math.max(w, 1);
		h = Math.max(h, 1);
		BufferedImage dstImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dstImg.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.drawImage(srcImg.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
		g.dispose();

		ImageIO.write(dstImg, "jpeg", out);

		return new int[] { w, h };
	}

	/**
	 * 按照指定的比例和质量压缩图片
	 * 
	 * @param srcImg
	 * @param out
	 * @param scale
	 * @param quality
	 * @return 图片压缩后的长宽数组
	 * @throws IOException
	 */
	@SuppressWarnings("restriction")
	public static int[] jpegScaling(BufferedImage srcImg, OutputStream out, double scale,
			float quality) throws IOException {

		int w = (int) (srcImg.getWidth() * scale);
		int h = (int) (srcImg.getHeight() * scale);
		w = Math.max(w, 1);
		h = Math.max(h, 1);

		BufferedImage dstImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		dstImage.getGraphics().drawImage(srcImg.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0,
				null);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		// 设置图片压缩质量
		if (quality < 1.0f) {
			JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(dstImage);
			encodeParam.setQuality(quality, true);
			encoder.encode(dstImage, encodeParam);
		} else {
			encoder.encode(dstImage);
		}

		dstImage.flush();

		return new int[] { w, h };
	}
}
