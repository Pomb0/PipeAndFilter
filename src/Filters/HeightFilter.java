package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

public class HeightFilter extends ExpandedFilterFramework {

    @Override
    public void filter() throws EndOfStreamException {
        FrameBean frame = readFrame();
    }

    private double FeetToMeters(double v) {
        return v / 3.2808;
    }
}
