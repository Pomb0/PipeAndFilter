package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class TemperatureFilter extends ExpandedFilterFramework {

    public void filter() throws EndOfStreamException {
        FrameBean frame = readFrame();
        AttributeBean temperature = frame.getAttribute(4);
        temperature.setValue(fahrenheitToCelsius(temperature.getValueAsDouble()));
        writeFrame(frame);
    }

    private double fahrenheitToCelsius(double v) {
        return ((v - 32) / 1.8);
    }
}