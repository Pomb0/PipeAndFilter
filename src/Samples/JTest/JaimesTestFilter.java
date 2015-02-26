package Samples.JTest;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

/**
 * Created by Jaime on 26/02/2015.
 */
public class JaimesTestFilter extends ExpandedFilterFramework {
	public void filter() throws EndOfStreamException {
		FrameBean frame;
		AttributeBean attrib;
		frame = readFrame();
		System.out.println(">> " + frame.getAttribute(2).getValueAsDouble() + " : " + frame.getAttribute(3).getValueAsDouble());
	}
}
