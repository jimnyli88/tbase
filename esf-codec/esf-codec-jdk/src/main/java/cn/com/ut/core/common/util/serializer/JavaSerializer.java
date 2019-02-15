package cn.com.ut.core.common.util.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
* java序列化工具类
* @author wuxiaohua
* @since 2013-12-22下午2:27:50
*/
public class JavaSerializer {

	/**
	 * JDK序列化器
	 * 
	 * @param obj
	 * @return byte[]
	 */
	public static byte[] serialize(Object obj) {

		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new SerializeException(e);
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				throw new SerializeException(e);
			}
		}

		return bos == null ? null : bos.toByteArray();
	}

	/**
	 * 反序列化
	 * 
	 * @param b
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] b) {

		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(b);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException e) {
			throw new SerializeException(e);
		} catch (ClassNotFoundException e) {
			throw new SerializeException(e);
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				throw new SerializeException(e);
			}
		}

		return obj == null ? null : (T) obj;
	}
}
