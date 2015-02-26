package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class TemperatureFilter extends ExpandedFilterFramework {
    private int field;  // 4

    public TemperatureFilter(int field) { this.field = field; }

    public void filter() throws EndOfStreamException {
        FrameBean frame = readFrame();
        AttributeBean temperature = frame.getAttribute(field);
        temperature.setValue(fahrenheitToCelsius(temperature.getValueAsDouble()));
        writeFrame(frame);
    }

    private double fahrenheitToCelsius(double v) {
        return ((v - 32) / 1.8);
    }
}