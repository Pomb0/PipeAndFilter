package Filters;

import Framework.FilterFramework;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToStringFilter extends FilterFramework{
    private int [] idList;
    private int bytesSoFar;
    private String [] frameArray;


    public ToStringFilter(int[] idList) {
        this.idList = idList;
        this.frameArray = new String[idList.length];
        FrameArrayStart();
    }

    private void FrameArrayStart() {
        for(int i = 0 ; i < frameArray.length ; i++){
            frameArray[i] = null;
        }
    }

    public void run() {
        int measurementLength = 8;
        int idLength = 4;
        byte dataByte = 0;
        int bytesRead = 0;
        int id;

        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");

        long measurement;

        DecimalFormat df;

        System.out.println(this.getName() + "::ToStringStarting ");

        while (true) {
            id = 0;
            measurement = 0;
            try {
                for (int i = 0; i < idLength; i++) {
                    dataByte = ReadFilterInputPort();
                    id = id | (dataByte & 0xFF);

                    if (i != idLength - 1) { id = id << 8; }
                    bytesRead++;
                }

                for (int i = 0; i < measurementLength; i++) {
                    dataByte = ReadFilterInputPort();
                    measurement = measurement | (dataByte & 0xFF);

                    if (i != measurementLength - 1) { measurement = measurement << 8; }
                    bytesRead++;
                }

                switch (id) {
                    case (0):
                        ResetArray(frameArray);
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

                if (id == 4) {
                    SendInfo(ConcatString(frameArray));
                }

            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.println(this.getName() + "::ToString Exiting; bytes read: " + bytesRead);
                break;

            }
        }
    }

    private void ResetArray(String[] frameArray) {
        for(int i=0 ; i < frameArray.length; i++){
            frameArray[i] = null;
        }
    }

    private void SendInfo(String finalFrame) {
        byte[] bytes = finalFrame.getBytes(Charset.forName("UTF-8"));

        int byteswritten = 0;                // Number of bytes written to the stream.

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


        for (int i = 0 ; i < this.idList.length ; i++) {
            tmpString = finalArray[this.idList[i]];
            if(tmpString != null){
                finalString.append(tmpString);
                finalString.append("\t");
            }
        }


        finalString.append("\n");
        return finalString.toString();
    }

}