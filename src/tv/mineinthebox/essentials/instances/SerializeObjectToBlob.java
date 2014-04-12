package tv.mineinthebox.essentials.instances;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class SerializeObjectToBlob implements Callable<byte[]> {

	private final Object obj;
	
	public SerializeObjectToBlob(Object obj) {
		this.obj = obj;
	}
	
	/**
	 * @author xize
	 * @param returns the byte[] array as a blob for mysql, this will be done through a separated thread.
	 * @return byte[]
	 * @throws Exception - when the duration takes to long
	 */
	public byte[] getBlob() throws Exception {
		return call();
	}
	
	@Override
	public byte[] call() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream obout = new ObjectOutputStream(out);
		obout.writeObject(obj);
		obout.flush();
		obout.close();
		out.close();
		return out.toByteArray();
	}

}
