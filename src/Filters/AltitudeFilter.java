package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class AltitudeFilter extends ExpandedFilterFramework {
    private int field;  // 2

    public AltitudeFilter(int field) { this.field = field; }

    @Override
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
