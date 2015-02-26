package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jaime on 26/02/2015.
 */
public class FieldAggregatorFielder extends ExpandedFilterFramework{
	@Override
	public void filter() throws EndOfStreamException {
		FrameBean frame;
		FrameBean payLoadFrame = null;
		LinkedHashMap<Integer, AttributeBean> attribMap;

		for(int p = 0; p<inputReadPorts.size(); p++){
			if(payLoadFrame == null) payLoadFrame = readFrame(p);
			else{
				frame = readFrame();
				attribMap = frame.getAttributeMap();
				for(Map.Entry<Integer, AttributeBean> entry : attribMap.entrySet()){
					payLoadFrame.setAttribute(entry.getValue());
				}
			}
		}
		writeFrame(payLoadFrame);
	}
}
