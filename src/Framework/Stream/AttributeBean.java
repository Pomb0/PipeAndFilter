package Framework.Stream;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by Jaime on 26/02/2015.
 */
public class AttributeBean implements Serializable, Cloneable{
	private byte[] key = new byte[4];
	private byte[] value = new byte[8];


	
	public int getKeyAsInt(){
        return ByteBuffer.wrap(key).getInt();
	}

	public double getValueAsDouble(){
		return ByteBuffer.wrap(key).getDouble();
	}

	public void setKey(int key) {
        this.key = ByteBuffer.allocate(4).putInt(key).array();
	}


	public void setValue(double value) {
        this.value = ByteBuffer.allocate(8).putDouble(value).array();
	}



	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
}
