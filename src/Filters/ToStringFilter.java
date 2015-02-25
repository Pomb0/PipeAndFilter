package Filters;

import Framework.FilterFramework;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToStringFilter extends FilterFramework{
    private int [] idList;
    private int bytesSoFar;

    public ToStringFilter(int[] idList) {
        this.idList = idList;
    }

    public void run() {
        int measurementLength = 8;
        int idLength = 4;
        byte dataByte = 0;
        int bytesRead = 0;
        int id;

        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");

        StringBuilder finalFrame = new StringBuilder();
        long measurement;

        DecimalFormat df;
        String tempString;

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
                        if(finalFrame.length() != 0) {
                            finalFrame.append("\n");
                            SendInfo(finalFrame.toString());
                        }
                        finalFrame = new StringBuilder();
                        TimeStamp.setTimeInMillis(measurement);
                        tempString = TimeStampFormat.format(TimeStamp.getTime());
                        break;
                    case (1):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        break;
                    case (2):
                        df = new DecimalFormat("000000.0000");
                        tempString = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (3):
                        df = new DecimalFormat("00.0000");
                        tempString = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (4):
                        df = new DecimalFormat("000.00000");
                        tempString = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case (5):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        break;
                    default:
                        tempString = " ? ";
                        finalFrame.append(tempString);
                }

                if (CheckArrayForID(id)) {
                    if (finalFrame.length() == 0) { finalFrame.append(tempString); }
                    else { finalFrame.append("\t").append(tempString); }
                }

            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.println(this.getName() + "::ToString Exiting; bytes read: " + bytesRead);
                break;

            }
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

    private boolean CheckArrayForID(int id) {
        for (int anIdList : this.idList) {
            if (anIdList == id) { return true; }
        }
        return false;
    }

}