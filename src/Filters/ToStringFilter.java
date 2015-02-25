package Filters;

import Framework.FilterFramework;

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
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        String.format("%06d.%40d", tempString);
                        break;
                    case(3):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        String.format("%02d:%40d%s" , tempString);
                        break;
                    case(4):
                        tempString = String.valueOf(Double.longBitsToDouble(measurement));
                        String.format("%02")
                }

                if(CheckArrayForID(id) == true) {
                    finalFrame.append(tempString);
                }

                if (id == 4) {
                    System.out.print(TimeStampFormat.format(TimeStamp.getTime()) + " ID = " + id + " " + Double.longBitsToDouble(measurement));
                }

            }catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print(this.getName() + "::ToString Exiting; bytes read: " + bytesread);
                break;

            }
        }
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
