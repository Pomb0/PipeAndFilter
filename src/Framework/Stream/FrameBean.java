package Framework.Stream;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by Jaime on 26/02/2015.
 */
public class FrameBean implements Serializable, Cloneable{
	private LinkedHashMap<Integer, AttributeBean> attributeMap = new LinkedHashMap<>();
	
	public AttributeBean getAttribute(int id){
		return  attributeMap.get(id);
	}

	public AttributeBean setAttribute(AttributeBean attrib){
		return  attributeMap.put(attrib.getKeyAsInt(), attrib);
	}

	public LinkedHashMap<Integer, AttributeBean> getAttributeMap() {
		return attributeMap;
	}
}
