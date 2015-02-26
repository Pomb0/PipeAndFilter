package Framework.Stream;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by Jaime on 26/02/2015.
 */
public class AttributeBean implements Serializable, Cloneable{
	/**
	 * Constants to avoid magic values.
	 */
	public static final int KEYSIZE = 4;
	public static final int VALUESIZE = 8;
	
	private byte[] key = new byte[KEYSIZE];
	private byte[] value = new byte[VALUESIZE];

   	//TODO add get as date function
	
	public int getKeyAsInt(){
        return ByteBuffer.wrap(key).getInt();
	}

	public double getValueAsDouble(){
		return ByteBuffer.wrap(value).getDouble();
	}

	public void setKey(int key) {
        this.key = ByteBuffer.allocate(KEYSIZE).putInt(key).array();
	}


	public void setValue(double value) {
        this.value = ByteBuffer.allocate(VALUESIZE).putDouble(value).array();
	}



	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key.clone();
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value.clone();
	}
}
