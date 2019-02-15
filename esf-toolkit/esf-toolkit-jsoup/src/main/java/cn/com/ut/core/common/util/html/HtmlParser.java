package cn.com.ut.core.common.util.html;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.com.ut.core.common.http.UrlParameter;
import cn.com.ut.core.common.util.CommonUtil;
/**
*
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class HtmlParser {

	public static final int MAX_WORD_COUNT = 200;
	public static final String[] IMG_SRC = { "UploadjsonServlet", "FileLoad", "resource", "group" };

	static interface ImageSrc {
		String RESOURCE = "resource";
		String GROUP = "group";
		String FILE_LOAD = "FileLoad";
		String UPLOAD_JSON = "UploadjsonServlet";
	}

	public static void main(String[] args) throws Exception {

		Document doc = loadHtmlByInput(new FileInputStream("D:/html/03.htm"), "GBK");
		String s = parsePlainText(doc);
		System.out.println(s);
		List<String> images = parseImage(doc, 2);
		System.out.println(images);
	}

	public static Document loadHtmlByHttpGet(String url) throws IOException {

		return Jsoup.connect(url).get();
	}

	public static Document loadHtml(String html) {

		return Jsoup.parse(html);
	}

	public static Document loadHtmlByInput(InputStream input, String charsetName)
			throws IOException {

		InputStreamReader isr = new InputStreamReader(input, charsetName);
		byte[] bs = IOUtils.toByteArray(isr, "UTF-8");
		String html = new String(bs);
		return Jsoup.parse(html);
	}

	public static String parsePlainText(Document doc) {

		return parsePlainText(doc, MAX_WORD_COUNT);
	}

	public static String parsePlainText(String plainText) {

		return parsePlainText(plainText, MAX_WORD_COUNT);
	}

	public static String parsePlainText(String plainText, int number) {

		if (plainText == null)
			return null;

		StringBuilder txt = new StringBuilder(plainText);
		if (txt.length() > number) {
			return txt.replace(number, txt.length(), "......").toString();
		} else {
			return txt.toString();
		}
	}

	/**
	 * 从html中提取限定字数的普通文本
	 * 
	 * @param doc
	 * @param number
	 * @return 从html中提取限定字数的普通文本
	 */
	public static String parsePlainText(Document doc, int number) {

		return parsePlainText(doc.text(), number);
	}

	public static String parseImage(Document doc) {

		List<String> images = parseImage(doc, 1);
		if (images == null || images.isEmpty()) {
			return null;
		} else {
			return images.get(0);
		}
	}

	/**
	 * 从html中提取图片路径
	 * 
	 * @param doc
	 * @param number
	 * @return 从html中提取图片路径
	 */
	public static List<String> parseImage(Document doc, int number) {

		Elements es = doc.getElementsByTag("img");
		List<String> list = new ArrayList<String>();
		if (es.isEmpty())
			return list;

		number = number == 0 ? 1 : number;
		int min = Math.min(number, es.size());

		int count = 0;
		Iterator<Element> iterator = es.iterator();
		while (iterator.hasNext() && count < min) {
			Element e = iterator.next();
			if (e.hasAttr("src")) {
				String src = e.attr("src");
				if (!CommonUtil.isEmpty(src)) {
					Map<String, String> map = UrlParameter.parseParameter(src);
					String value = null;
					if (src.indexOf(ImageSrc.FILE_LOAD) != -1) {
						value = map.get(ImageSrc.GROUP);
					} else if (src.indexOf(ImageSrc.UPLOAD_JSON) != -1) {
						value = map.get(ImageSrc.RESOURCE);
					}

					if (value != null && !"".equals(value)) {
						list.add(value);
						count++;
					}
				}
			}
		}
		return list;
	}
}
