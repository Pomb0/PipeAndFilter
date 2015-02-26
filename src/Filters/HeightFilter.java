package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class HeightFilter extends ExpandedFilterFramework {
    private int field;  // 2

    public HeightFilter(int field) { this.field = field; }

    public void filter() throws EndOfStreamException {
        FrameBean frame = readFrame();
	    AttributeBean height = frame.getAttribute(field);
        height.setValue(FeetToMeters(height.getValueAsDouble()));
	    writeFrame(frame);
    }

    private double FeetToMeters(double v) {
        return v / 3.2808;
    }
}
