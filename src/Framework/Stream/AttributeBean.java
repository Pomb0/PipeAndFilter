package Framework.Stream;

import java.io.Serializable;

/**
 * Created by Jaime on 26/02/2015.
 */
public class AttributeBean implements Serializable, Cloneable{
	private byte[] key = new byte[4];
	private byte[] value = new byte[8];


	
	public int getKeyAsInt(){
		//TODO costa - returns key as an integer
		return 0;
	}

	public double getValueAsDouble(){
		//TODO costa - returns value as a double
		return 0;
	}

	public void setKey(int key) {
		//TODO costa - convert int key to byte array this.key
	}


	public void setValue(double value) {
		//TODO costa - convert double value to byte array this.value
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
