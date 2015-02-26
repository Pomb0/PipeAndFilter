package Samples.JTest;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class JaimesTestFilter extends ExpandedFilterFramework {
	public void filter() throws EndOfStreamException {
		FrameBean frame;
		frame = readFrame();
        AttributeBean attributeBean = frame.getAttribute(1);
        attributeBean.setValue(69f);
        frame.setAttribute(attributeBean);
        
        writeFrame(frame);
	}
}
