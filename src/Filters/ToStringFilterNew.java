package Filters;

import Framework.ExpandedFilterFramework;
import Framework.FilterFramework;
import Framework.Stream.FrameBean;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToStringFilterNew extends ExpandedFilterFramework {
    public void filter() throws FilterFramework.EndOfStreamException {
        FrameBean frame;
        DecimalFormat df;
        String [] frameArray = {null, null, null, null, null, null};

        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");
        
        frame = readFrame();

        if(frame.getAttribute(0) != null) {
            TimeStamp.setTimeInMillis(frame.getAttribute(0).getValueAsLong());
            frameArray[0] = TimeStampFormat.format(TimeStamp.getTime());
        }

        if(frame.getAttribute(1) != null) {
            frameArray[1] = String.valueOf(frame.getAttribute(1).getValueAsDouble());
        }

        if(frame.getAttribute(2) != null) {
            df = new DecimalFormat("000000.0000");
            frameArray[2] = df.format(frame.getAttribute(2).getValueAsDouble());
        }

        if(frame.getAttribute(3) != null) {
            df = new DecimalFormat("00.0000");
            frameArray[3] = df.format(frame.getAttribute(3).getValueAsDouble());
        }

        if(frame.getAttribute(4) != null) {
            df = new DecimalFormat("000.00000");
            frameArray[4] = df.format(frame.getAttribute(4).getValueAsDouble());
        }

        if(frame.getAttribute(5) != null) {
            frameArray[5] = String.valueOf(frame.getAttribute(5).getValueAsDouble());
        }
        
        SendInfo(ConcatString(frameArray));
    }

    private void SendInfo(String finalFrame) {
        byte[] bytes = finalFrame.getBytes(Charset.forName("UTF-8"));
        for (byte aByte : bytes) WriteFilterOutputPort(aByte);
    }

    private String ConcatString(String[] finalArray) {
        StringBuilder finalString = new StringBuilder();

        for (String tmpString : finalArray) {
            if (tmpString != null) {
                finalString.append(tmpString);
                finalString.append("\t");
            }
        }

        finalString.append("\n");
        return finalString.toString();
    }
}
