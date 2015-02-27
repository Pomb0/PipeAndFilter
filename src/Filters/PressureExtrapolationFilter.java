package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.LinkedList;

/**
 * Extrapolates the pressure reading on frames containing an attribute with id 6
 * based on the surrounding frames that do not contain said attribute.
 *
 * More details on how this process works on the assginement specification.
 */

public class PressureExtrapolationFilter extends ExpandedFilterFramework {
    private int field;  // 3
    private double lastGoodValue;
    private LinkedList<FrameBean> values;
    private boolean isFirst;

    public PressureExtrapolationFilter(int field) {
        this.field = field;
        this.values = new LinkedList<>();
        this.isFirst = true;
    }

    @Override
    public void filter() throws EndOfStreamException {
        try {
            FrameBean frame = readFrame();
            double tempPressure = frame.getAttribute(field).getValueAsDouble();

            if (frame.getAttribute(6) != null) { this.values.add(frame); }
            else {
                if (!values.isEmpty()) { dispatch(tempPressure); }

                this.isFirst = false;
                this.lastGoodValue = tempPressure;
                writeFrame(frame);
            }
        } catch (EndOfStreamException e) {
            if (!values.isEmpty()) { dispatch(this.lastGoodValue); }
            throw new EndOfStreamException("End of input stream reached");
        }
    }

    private void dispatch(double tempPressure) {
        Double valueToSend;
        FrameBean temp;

        if (this.isFirst) { valueToSend = tempPressure; }
        else { valueToSend = (this.lastGoodValue + tempPressure) / 2; }

        while (!this.values.isEmpty()) {
            temp = this.values.removeFirst();
            temp.getAttribute(this.field).setValue(valueToSend);
            writeFrame(temp);
        }
    }
}
