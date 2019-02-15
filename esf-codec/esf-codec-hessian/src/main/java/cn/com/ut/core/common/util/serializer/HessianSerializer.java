package cn.com.ut.core.common.util.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * Hessian序列化工具类
 * 
 * @author wuxiaohua
 * @since 2017年7月7日
 */
public class HessianSerializer {

	/**
	 * 序列化
	 * 
	 * @param obj
	 *            对象实例
	 * @return 序列化后字节数组
	 */
	public static byte[] serialize(Object obj) {

		ByteArrayOutputStream bos = null;
		Hessian2Output out = null;
		try {
			bos = new ByteArrayOutputStream();
			out = new Hessian2Output(bos);
			out.startMessage();
			out.writeObject(obj);
			out.completeMessage();
		} catch (IOException e) {
			throw new SerializeException();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new SerializeException();
				}
			}
		}

		return bos == null ? null : bos.toByteArray();
	}

	/**
	 * 反序列化
	 * 
	 * @param data
	 *            字节数组
	 * @param clazz
	 *            对象类型
	 * @return 对象实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, Class<T> clazz) {

		ByteArrayInputStream bis = null;
		Hessian2Input in = null;
		Object obj = null;
		try {
			bis = new ByteArrayInputStream(data);
			in = new Hessian2Input(bis);
			in.startMessage();
			obj = in.readObject(clazz);
			in.completeMessage();
		} catch (IOException e) {
			throw new SerializeException();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new SerializeException();
			}
		}
		return obj == null ? null : (T) obj;
	}

}
