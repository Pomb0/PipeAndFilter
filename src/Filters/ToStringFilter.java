package Filters;

import Framework.ExpandedFilterFramework;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToStringFilter extends ExpandedFilterFramework {
    private int [] idList;
    private int bytesSoFar;
    private String [] frameArray;

    public ToStringFilter(int[] idList) {
        this.idList = idList;
        this.frameArray = new String[idList.length];
        FrameArrayStart();
    }

    public void run() {
        int measurementLength = 8;
        int idLength = 4;
        int id;
        long measurement;
        byte dataByte;

        DecimalFormat df;
        String [] frameArray = {null, null, null, null, null, null};

        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");

        System.out.println(this.getName() + "::ToString Filter Starting.");

        while (true) {
            id = 0;
            measurement = 0;
            try {
                for (int i = 0; i < idLength; i++) {
                    dataByte = ReadFilterInputPort();
                    id = id | (dataByte & 0xFF);
                    if (i != idLength - 1) { id = id << 8; }
                }

                for (int i = 0; i < measurementLength; i++) {
                    dataByte = ReadFilterInputPort();
                    measurement = measurement | (dataByte & 0xFF);
                    if (i != measurementLength - 1) { measurement = measurement << 8; }
                }

                switch (id) {
                    case (0):
                        if(frameArray[0] != null) SendInfo(ConcatString(frameArray));
                        TimeStamp.setTimeInMillis(measurement);
                        frameArray[id] = TimeStampFormat.format(TimeStamp.getTime());
                        break;
                    case (1):
                        frameArray[id] = String.valueOf(Double.longBitsToDouble(measurement));
                        break;
                    case (2):
                        df = new DecimalFormat("000000.0000");
                        frameArray[id] = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (3):
                        df = new DecimalFormat("00.0000");
                        frameArray[id] = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (4):
                        df = new DecimalFormat("000.00000");
                        frameArray[id] = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (5):
                        frameArray[id] = String.valueOf(Double.longBitsToDouble(measurement));
                        break;
                    default:
                        break;
                }

            } catch (EndOfStreamException e) {
                SendInfo(ConcatString(frameArray));
                ClosePorts();
                System.out.println(this.getName() + "::ToString Filter Exiting. Bytes read: " + bytesSoFar);
                break;
            }
        }
    }

    private void SendInfo(String finalFrame) {
        byte[] bytes = finalFrame.getBytes(Charset.forName("UTF-8"));
        int byteswritten = 0;

        for (byte aByte : bytes) {
            WriteFilterOutputPort(aByte);
            byteswritten++;
            bytesSoFar++;
        }
        System.out.println(this.getName() + "::Sent " + byteswritten + " bytes " + "(" + bytesSoFar + ")");
    }

    private String ConcatString(String[] finalArray) {
        StringBuilder finalString = new StringBuilder();
        String tmpString;

        for (int anIdList : this.idList) {
            tmpString = finalArray[anIdList];
            if (tmpString != null) {
                finalString.append(tmpString);
                finalString.append("\t");
            }
        }

        finalString.append("\n");
        return finalString.toString();
    }

    private void FrameArrayStart() {
        for(int i = 0 ; i < frameArray.length ; i++){
            frameArray[i] = null;
        }
    }
}