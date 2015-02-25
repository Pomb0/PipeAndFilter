package Filters;

import Framework.FilterFramework;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToStringFilter extends FilterFramework{
    private int [] idList;

    public ToStringFilter(int [] idList){
        this.idList = idList;
    }

    public void run(){
        int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
        int IdLength = 4;                // This is the length of IDs in the byte stream

        byte databyte = 0;                // This is the data byte read from the stream
        int bytesread = 0;                // This is the number of bytes read from the stream

        int id;                            // This is the measurement id
        int i;                            // This is a loop counter

        /************************/
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        StringBuilder finalFrame = new StringBuilder();
        long measurement;

        DecimalFormat df;
        Double tempDouble;
        String tempString;
        /************************/

        System.out.print(this.getName() + "::ToStringStarting ");

        while (true) {
            try {
                id = 0;

                for (i = 0; i < IdLength; i++) {
                    databyte = ReadFilterInputPort();    // This is where we read the byte from the stream...

                    id = id | (databyte & 0xFF);        // We append the byte on to ID...

                    if (i != IdLength - 1)                // If this is not the last byte, then slide the
                    {                                    // previously appended byte to the left by one byte
                        id = id << 8;                    // to make room for the next byte we append to the ID

                    } // if

                    bytesread++;                        // Increment the byte count

                }
                measurement = 0;

                for (i = 0; i < MeasurementLength; i++) {
                    databyte = ReadFilterInputPort();
                    measurement = measurement | (databyte & 0xFF);    // We append the byte on to measurement...

                    if (i != MeasurementLength - 1){                    // previously appended byte to the left by one byte
                        measurement = measurement << 8;                // to make room for the next byte we append to the

                    }
                    bytesread++;                                    // Increment the byte count

                }

                switch(id) {
                    case (0):
                        TimeStamp.setTimeInMillis(measurement);
                        tempString = TimeStampFormat.format(TimeStamp.getTime());
                        break;
                    case(1):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        break;
                    case(2):
                        df = new DecimalFormat("000000.0000");
                        tempString =  df.format(Double.longBitsToDouble(measurement));
                        break;
                    case(3):
                        df = new DecimalFormat("00.0000");
                        tempString = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case(4):
                        df = new DecimalFormat("000.00000");
                        tempString = df.format(Double.longBitsToDouble(measurement));
                        break;
                    case(5):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        break;

                    default :
                        tempString = " ? ";
                }

                if(CheckArrayForID(id) == true) {
                    if(finalFrame.length() == 0) {
                        finalFrame.append(tempString);
                    }else {
                        finalFrame.append("\t" + tempString);
                    }
                }

                if (id == 5) {
                    SendInfo(finalFrame.toString());
                }

            }catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print(this.getName() + "::ToString Exiting; bytes read: " + bytesread);
                break;

            }
        }
    }

    private void SendInfo(String finalFrame) {
        byte[] bytes = finalFrame.getBytes();
        int byteswritten = 0;                // Number of bytes written to the stream.

        System.out.print(this.getName() + "::Sending to Sink ");

        for(int i = 0 ; i < bytes.length ; i++){
            WriteFilterOutputPort(bytes[i]);
        }

        System.out.println("::Bytes written - " + byteswritten);

    }

    private boolean CheckArrayForID(int id) {
        int i;

        for(i = 0 ; i < this.idList.length; i++){
            if(this.idList[i] == id)
                return true;
        }
        return false;
    }

}
