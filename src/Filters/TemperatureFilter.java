package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

/**
 * Converts a given attribute from fahrenheit to celsius.
 */

public class TemperatureFilter extends ExpandedFilterFramework {
    private int field;  // 4

    public TemperatureFilter(int field) { this.field = field; }

    @Override
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