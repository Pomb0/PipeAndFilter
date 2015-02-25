package Filters;

import Framework.ExpandedFilterFramework;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by guilherme on 2/25/15.
 */
public class TemperatureFilter extends ExpandedFilterFramework {
    @Override
    public void run(){
        int measurementLength = 8;
        int idLength = 4;
        byte dataByte = 0;
        int bytesRead = 0;
        int id;

        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:MM:dd::hh:mm:ss");

        long measurement;
        DecimalFormat df;
        String timestamp = "";
        double celsiusValue;

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

                switch(id){
                    case(0):
                        TimeStamp.setTimeInMillis(measurement);
                        timestamp = TimeStampFormat.format(TimeStamp.getTime());
                        break;
                    case(4):
                        celsiusValue = ConvertToCelsius(Double.longBitsToDouble(measurement));
                        FilterDispatcher(timestamp, celsiusValue);
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.println(this.getName() + "::ToString Exiting; bytes read: " + bytesRead);
                break;

            }
        }
    }

    private void FilterDispatcher(String timestamp, double celsiusValue) {
        byte[] bytes = timestamp.getBytes();

        int byteswritten = 0;                // Number of bytes written to the stream.

        for(int i = 0 ; i < 4 ; i++){
            WriteFilterOutputPort((byte)((0 >> ((7 - i) * 8)) & 0xff));
        }

        for (byte aByte : bytes) {
            WriteFilterOutputPort(aByte);
            byteswritten++;

        }

        for(int i = 0 ; i < 4 ; i++){
            WriteFilterOutputPort((byte)((4 >> ((7 - i) * 8)) & 0xff));
        }

        for(int i = 0; i < 8; i++)
            WriteFilterOutputPort((byte)((Double.doubleToLongBits(celsiusValue) >> ((7 - i) * 8)) & 0xff));

        System.out.println(this.getName() + "::Sent " + byteswritten + " bytes ");
    }

    private double ConvertToCelsius(double v) {
        return (v - 32) / 1.8;
    }
}
