package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

/**
 * Separates the frames presenting readings outside the min-max range as defined in the constructor
 * on a given field, into different pipes. Pipe 0 containes the values in range, and pipe 1 contains the
 * values outside of it.
 */
public class WildPointFilter extends ExpandedFilterFramework {
    private int field;  // 3
    private double min, max;

    /**
     * This filter requires the wild point logger to be connected to the pipe 1
     * and the valid and interpolated points will be sent to the pipe 0
     */

    public WildPointFilter(int field, double min, double max) {
        this.field = field;
        this.min = min;
        this.max = max;
    }
    
    public void filter() throws EndOfStreamException {
        FrameBean frame = readFrame();
        AttributeBean attribute = frame.getAttribute(field);

        if(attribute == null) {
            writeFrame(frame);
            return;
        }

        double pressure = attribute.getValueAsDouble();
        if(pressure < min || pressure > max) {
            AttributeBean wild = new AttributeBean();
            wild.setKey(6).setValue(1f);
            frame.setAttribute(wild);
            logWildPoint(frame);
        }
        
        writeFrame(0, frame);
    }

    /**
     * This functions sends the wild points to a specific pipe for logging!
     */
    
    private void logWildPoint(FrameBean frame) {
        FrameBean payLoadFrame = new FrameBean();
        payLoadFrame.setAttribute(frame.getAttribute(0));
        payLoadFrame.setAttribute(frame.getAttribute(field));
        writeFrame(1, payLoadFrame);
    }
}
