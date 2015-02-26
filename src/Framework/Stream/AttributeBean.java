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

	public int getKeyAsInt(){
        return ByteBuffer.wrap(key).getInt();
	}

	public double getValueAsDouble(){
		return ByteBuffer.wrap(value).getDouble();
	}

    public long getValueAsLong(){
        return ByteBuffer.wrap(value).getLong();
    }

	public AttributeBean setKey(int key) {
        this.key = ByteBuffer.allocate(KEYSIZE).putInt(key).array();
		return this;
	}

	public AttributeBean setValue(double value) {
        this.value = ByteBuffer.allocate(VALUESIZE).putDouble(value).array();
		return this;
	}

    public AttributeBean setValue(long value) {
        this.value = ByteBuffer.allocate(VALUESIZE).putLong(value).array();
	    return this;
    }



	public byte[] getKey() {
		return key;
	}

	public AttributeBean setKey(byte[] key) {
		this.key = key.clone();
		return this;
	}

	public byte[] getValue() {
		return value;
	}

	public AttributeBean setValue(byte[] value) {
		this.value = value.clone();
		return this;
	}
}
