package cn.com.ut.core.common.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import cn.com.ut.core.common.util.CommonUtil;

/**
 * 文件工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the FileUtil class
	 * will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a
	 * scenario where you absolutely need a non-null ClassLoader reference: for
	 * example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
	 * reference as well).
	 * 
	 * @return the default ClassLoader (never <code>null</code>)
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {

		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = FileUtil.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * 根据指定路径获取InputStream
	 * 
	 * @param classPath
	 * @return InputStream
	 */
	public static InputStream inputStreamReader(String classPath) {

		return getDefaultClassLoader().getResourceAsStream(classPath);
	}

	/**
	 * 根据指定路径和文件名获取url集合
	 * 
	 * @param classPath
	 * @param fileName
	 * @return List<URL> or null
	 */
	public static List<URL> listUrls(String classPath, String fileName) {

		if (CommonUtil.isEmpty(classPath))
			return null;
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		List<URL> urls = new ArrayList<URL>();
		try {
			if (CommonUtil.isEmpty(fileName))
				resources = resolver.getResources("classpath*:" + classPath);
			else
				resources = resolver.getResources("classpath*:" + classPath + "/**/" + fileName);
		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		if (resources != null) {
			for (Resource resource : resources) {
				try {
					urls.add(resource.getURL());
				} catch (IOException e) {
					continue;
				}
			}

		}

		return urls;
	}

	/**
	 * 根据指定路径获取文件集合
	 * 
	 * @param path
	 * @param isClassPath
	 * @return List<File> or null
	 */
	public static List<File> listFiles(String path, boolean isClassPath) {

		List<File> files = new ArrayList<File>();

		if (CommonUtil.isEmpty(path))
			return null;

		String name = null;

		if (isClassPath) {
			URL url = FileUtil.class.getClassLoader().getResource(path);
			name = url.getPath();
		} else {
			name = path;
		}

		listFiles(files, new File(name));

		return files;
	}

	/**
	 * 如果指定的文件parent是文件，则加入files中, 如果是目录，则将parent下的文件添加到List<File>中
	 * 
	 * @param files
	 * @param parent
	 */
	public static void listFiles(List<File> files, File parent) {

		if (files == null || parent == null)
			return;
		if (parent.isDirectory()) {
			File[] fileArray = parent.listFiles();
			if (fileArray != null) {
				for (File file : fileArray)
					listFiles(files, file);
			}
		} else {
			files.add(parent);
		}
	}

	/**
	 * load File
	 * 
	 * @param classPath
	 * @return File or null
	 */
	public static File loadFile(String classPath) {

		if (CommonUtil.isEmpty(classPath))
			return null;

		URL url = FileUtil.class.getClassLoader().getResource(classPath);
		String path = url.getPath();

		if (CommonUtil.isEmpty(path))
			return null;

		File file = new File(path);
		if (file.isDirectory()) {
			return null;
		}
		return file;
	}

	/**
	 * 将指定的数据写入指定的路径的文件
	 * 
	 * @param data
	 * @param path
	 */
	public static void fileWrite(String data, String path) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			bw.write(data);
		} catch (IOException e) {
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * file Write
	 * 
	 * @param data
	 * @param path
	 */
	public static void fileWrite(byte[] data, String path) {

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(path));
			bos.write(data);
		} catch (IOException e) {
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					;
				}
		}
	}

	/**
	 * 读取指定路径的文件
	 * 
	 * @param path
	 * @return byte[] or null
	 */
	public static byte[] fileByteReader(String path) {

		FileInputStream fis = null;
		byte[] bs = null;
		try {
			fis = new FileInputStream(path);
			bs = inputStreamReader(fis);
		} catch (FileNotFoundException e) {
		}
		return bs;
	}

	/**
	 * 读取指定的输入流
	 * 
	 * @param in
	 * @param isClosed
	 * @return byte[] or null
	 */
	public static byte[] inputStreamReader(InputStream in, boolean isClosed) {

		if (in == null)
			return null;

		if (!(in instanceof BufferedInputStream)) {
			in = new BufferedInputStream(in);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int temp = -1;
		byte[] array = new byte[4096];
		try {
			while ((temp = in.read(array)) != -1)
				bos.write(array, 0, temp);
		} catch (IOException e) {
		} finally {
			if (in != null && isClosed)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		return bos.toByteArray();
	}

	/**
	 * {@link #inputStreamReader(InputStream, boolean)}
	 * 
	 * @param in
	 * @return byte[]
	 */
	public static byte[] inputStreamReader(InputStream in) {

		return inputStreamReader(in, true);
	}

	/**
	 * file转成byte数组
	 * 
	 * @param filePath
	 * @return byte[] or null
	 */
	public static byte[] file2byte(String filePath) {

		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
			fis.close();
			bos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void main(String[] args) {

		byte[] b = file2byte("F:\\media\\part5.mp4");
		System.out.println(b.length);
		InputStream in = new ByteArrayInputStream(b);
		try {
			System.out.println(in.available());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * byte[] to File
	 * 
	 * @param buf
	 * @param filePath
	 * @param fileName
	 */
	public static void byte2File(byte[] buf, String filePath, String fileName) {

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * byte数组转成file
	 * 
	 * @param buf
	 * @param file
	 * @return File or null
	 */
	public static File byte2File(byte[] buf, File file) {

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			// String filePath = file.getPath();
			// File dir = new File(filePath);
			// if (!dir.exists() && dir.isDirectory())
			// {
			// dir.mkdirs();
			// }
			// file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * file to byte[]
	 * 
	 * @param file
	 * @return byte[]
	 */
	public static byte[] file2byte(File file) {

		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
			fis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte[] to File
	 * 
	 * @param buf
	 * @param sourceFilePath
	 * @return File
	 */
	public static File byte2File(byte[] buf, String sourceFilePath) {

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			File file = new File(sourceFilePath);
			if (!file.exists() && file.isDirectory()) {
				file.mkdirs();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void deleteFiles(File... files) {

		if (files.length < 1) {
			return;
		}
		for (File file : files) {
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}
}
