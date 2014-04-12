package tv.mineinthebox.essentials.instances;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;

public class DeSerializeObjectFromBlob implements Callable<Object> {
	
	private final byte[] bytes;

	public DeSerializeObjectFromBlob(byte[] bytes) {
		this.bytes = bytes;
	}
	
	/**
	 * @author xize
	 * @param returns the object back to its orginal state byte for byte.
	 * @return Object
	 * @throws Exception - when the duration takes to long
	 */
	public Object getBlob() throws Exception {
		return call();
	}

	@Override
	public Object call() throws Exception {
		ByteArrayInputStream input;
		ObjectInputStream objinput;
		input = new ByteArrayInputStream(bytes);
		objinput = new ObjectInputStream(input);
		Object newobj = objinput.readObject();
		input.close();
		objinput.close();
		return newobj;
	}
	
	
}
