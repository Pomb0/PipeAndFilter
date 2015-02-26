package Samples.JTest;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class JaimesTestFilter extends ExpandedFilterFramework {
	public void filter() throws EndOfStreamException {
		FrameBean frame;
		frame = readFrame();
        frame.setAttribute(frame.getAttribute(1).setValue(69f));
        writeFrame(frame);
	}
}
