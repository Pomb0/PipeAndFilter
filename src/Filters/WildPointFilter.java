package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

public class WildPointFilter extends ExpandedFilterFramework { 
    private int field;
    private double min, max;

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
        
        double psi = attribute.getValueAsDouble();
        
        if(psi < min || psi > max) {
            attribute.setKey(6).setValue(1f);
            frame.setAttribute(attribute);
        } else {
            attribute.setKey(6).setValue(0f);
            frame.setAttribute(attribute);
        }
        
        writeFrame(frame);
    }
}
