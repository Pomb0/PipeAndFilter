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
	
	private ByteBuffer key = ByteBuffer.allocate(KEYSIZE);
	private ByteBuffer value = ByteBuffer.allocate(VALUESIZE);

	public int getKeyAsInt(){
        return key.getInt(0);
	}

	public double getValueAsDouble(){
		return value.getDouble(0);
	}

    public long getValueAsLong(){
        return value.getLong(0);
    }

	public AttributeBean setKey(int key) {
        this.key.putInt(0, key);
		return this;
	}

	public AttributeBean setValue(double value) {
        this.value.putDouble(0, value);
		return this;
	}

    public AttributeBean setValue(long value) {
        this.value.putLong(0, value);
	    return this;
    }



	public byte[] getKey() {
		return key.array();
	}

	public AttributeBean setKey(byte[] key) {
		this.key = ByteBuffer.wrap(key.clone());
		return this;
	}

	public byte[] getValue() {
		return value.array();
	}

	public AttributeBean setValue(byte[] value) {
		this.value = ByteBuffer.wrap(value.clone());
		return this;
	}
}
