package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Converts a frame to text and writes them to a file acording to the assignment's sepcifications.
 */

public class FileSinkFilter extends ExpandedFilterFramework {
    private String dataPath;

    public FileSinkFilter(String dataPath) {
        this.dataPath = dataPath;
        try {
            FileWriter fw = new FileWriter(this.dataPath);
            fw.close();
        } catch (IOException e) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem truncating " + dataPath + " >>" + e);
        }
    }

    @Override
    public void filter() throws EndOfStreamException {
        String frameString = frameToString(readFrame());
        try {
            FileWriter fw = new FileWriter(this.dataPath, true);
            fw.write(frameString);
            fw.close();
        } catch (IOException e) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem writing to " + dataPath + " >>" + e);
        }
    }

    private String frameToString(FrameBean frame) {
        DecimalFormat df;
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");
        StringBuilder frameString = new StringBuilder();

        if (frame.getAttribute(0) != null) {
            TimeStamp.setTimeInMillis(frame.getAttribute(0).getValueAsLong());
            frameString.append(TimeStampFormat.format(TimeStamp.getTime())).append("\t");
        }
        if (frame.getAttribute(4) != null) {
            df = new DecimalFormat("000.00000");
            frameString.append(df.format(frame.getAttribute(4).getValueAsDouble())).append("\t");
        }
        if (frame.getAttribute(2) != null) {
            df = new DecimalFormat("000000.00000");
            frameString.append(df.format(frame.getAttribute(2).getValueAsDouble())).append("\t");
        }
        if (frame.getAttribute(3) != null) {
            df = new DecimalFormat("00.00000");
            frameString.append(df.format(frame.getAttribute(3).getValueAsDouble()));
            if (frame.getAttribute(6) != null) {
                frameString.append("*");
            }
            frameString.append("\t");
        }
        if (frame.getAttribute(1) != null) {
            frameString.append(String.valueOf(frame.getAttribute(1).getValueAsDouble())).append("\t");
        }
        if (frame.getAttribute(5) != null) {
            frameString.append(String.valueOf(frame.getAttribute(5).getValueAsDouble())).append("\t");
        }

        frameString.append("\n");
        return frameString.toString();
    }
}
