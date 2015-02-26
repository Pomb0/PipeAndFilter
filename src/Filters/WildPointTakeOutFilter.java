package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

import java.util.LinkedList;

public class WildPointTakeOutFilter extends ExpandedFilterFramework{
    private int field;  // 3
    private double lastGoodValue;
    private LinkedList values;
    private boolean isFirst;

    public WildPointTakeOutFilter (int field) {
        this.field = field;
        this.values = new LinkedList();
        this.isFirst = true;
    }

    public void filter() {

        try{
            FrameBean frame = readFrame();
            AttributeBean pressure = frame.getAttribute(field);
            double tempPressure = pressure.getValueAsDouble();

            if(tempPressure > 80 || tempPressure < 50){
                this.values.add(frame);

            }else{

                if(!values.isEmpty()){   Dispatch(tempPressure);         }

                this.isFirst = false;
                this.lastGoodValue = tempPressure;
                writeFrame(frame);
            }
        }catch (EndOfStreamException e){
            if(!values.isEmpty()){   Dispatch(this.lastGoodValue);       }
            throw new EndOfStreamException("End of input stream reached");
        }



    }

    private void Dispatch(double tempPressure) {
        Double valueToSend;
        FrameBean temp;

        if(this.isFirst){   valueToSend = tempPressure;   }
        else{    valueToSend = (this.lastGoodValue + tempPressure) / 2;    }


        while(!this.values.isEmpty()) {
            temp = (FrameBean) this.values.getFirst();

            temp.getAttribute(this.field).setValue(valueToSend);
            writeFrame(temp);

            this.values.removeFirst();

        }



    }
}
