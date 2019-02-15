package cn.com.ut.core.common.util.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

public class FstCodec {

	private static final FSTConfiguration fstConfig;

	static {
		fstConfig = FSTConfiguration.createDefaultConfiguration();
	}

	@SuppressWarnings("unchecked")
	public static <T> T decode(byte[] input) {

		return (T) fstConfig.asObject(input);
	}

	public static byte[] encode(Object obj) {

		return fstConfig.asByteArray(obj);
	}

	@SuppressWarnings("unchecked")
	public static <T> T decode(InputStream input) {

		FSTObjectInput fstInput = null;
		try {
			fstInput = new FSTObjectInput(input);
			return (T) fstInput.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new SerializeException("FstCodec#decode");
		} finally {
			if (fstInput != null) {
				try {
					fstInput.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void encode(OutputStream output, Object obj) throws IOException {

		FSTObjectOutput fstOutput = null;
		try {
			fstOutput = new FSTObjectOutput(output);
			fstOutput.writeObject(obj);
		} catch (IOException e) {
			throw new SerializeException("FstCodec#encode");
		} finally {
			if (fstOutput != null) {
				try {
					fstOutput.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
