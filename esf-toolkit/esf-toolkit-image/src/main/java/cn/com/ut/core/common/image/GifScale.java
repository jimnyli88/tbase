package cn.com.ut.core.common.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

import cn.com.ut.core.common.image.gif.AnimatedGifEncoder;
import cn.com.ut.core.common.image.gif.GifDecoder;
import cn.com.ut.core.common.image.gif.Scalr;
import cn.com.ut.core.common.image.gif.Scalr.Mode;

/**
 * GIF图片压缩
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class GifScale {

	/**
	 * 判断图片是否gif图片
	 * 
	 * @param in
	 * @param gd
	 * @return 如果是gif图片则返回true,否则返回false
	 */
	public static boolean isRealGIF(InputStream in, GifDecoder gd) {

		if (gd == null)
			gd = new GifDecoder();
		int status = gd.read(in);
		if (status != GifDecoder.STATUS_OK) {
			return false;
		}
		return true;
	}

	/**
	 * gif图片压缩
	 * 
	 * @param out
	 * @param gd
	 * @param scale
	 * @return 返回压缩后的图片长宽数组
	 */
	public static int[] gifScaling(OutputStream out, GifDecoder gd, double scale) {

		AnimatedGifEncoder ge = new AnimatedGifEncoder();
		ge.start(out);
		ge.setRepeat(0);

		for (int i = 0; i < gd.getFrameCount(); i++) {
			BufferedImage frame = gd.getFrame(i);
			int width = frame.getWidth();
			int height = frame.getHeight();
			width = (int) (width * scale);
			height = (int) (height * scale);
			width = Math.max(width, 1);
			height = Math.max(height, 1);

			BufferedImage rescaled = Scalr.resize(frame, Mode.FIT_EXACT, width, height);
			int delay = gd.getDelay(i);
			ge.setDelay(delay);
			ge.addFrame(rescaled);
		}
		ge.finish();
		return new int[] { ge.getWidth(), ge.getHeight() };
	}

	/**
	 * gif图片压缩
	 * 
	 * @param out
	 * @param gd
	 * @param wid
	 *            压缩后的宽
	 * @return 返回压缩后的图片长宽数组
	 */
	public static int[] gifScalingByWidth(OutputStream out, GifDecoder gd, int wid) {

		AnimatedGifEncoder ge = new AnimatedGifEncoder();
		ge.start(out);
		ge.setRepeat(0);

		for (int i = 0; i < gd.getFrameCount(); i++) {
			BufferedImage frame = gd.getFrame(i);
			int width = frame.getWidth();
			int height = frame.getHeight();
			height = (int) (height * wid);
			height = height / width;
			wid = Math.max(wid, 1);
			height = Math.max(height, 1);
			BufferedImage rescaled = Scalr.resize(frame, Mode.FIT_EXACT, wid, height);
			int delay = gd.getDelay(i);
			ge.setDelay(delay);
			ge.addFrame(rescaled);
		}
		ge.finish();
		return new int[] { ge.getWidth(), ge.getHeight() };
	}

}
