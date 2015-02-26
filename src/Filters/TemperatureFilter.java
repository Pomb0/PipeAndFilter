package Filters;

import Framework.ExpandedFilterFramework;

import java.nio.ByteBuffer;

public class TemperatureFilter extends ExpandedFilterFramework {
    int field = 4;
    
    public void run(){
        int measurementLength = 8;
        int idLength = 4;
        byte dataByte = 0;
        int id;

        long measurement;
        System.out.println(this.getName() + "::Temperature Filter Starting ");

        while (true) {
            id = 0;
            measurement = 0;
            try {
                
                for (int i = 0; i < idLength; i++) {
                    dataByte = ReadFilterInputPort();
                    id = id | (dataByte & 0xFF);
                    if (i != idLength - 1) { id = id << 8; }
                }
                
                if(id != field) {
                    for(int i = 0 ; i < 4 ; i++){ WriteFilterOutputPort((byte)((id >> ((7 - i) * 8)) & 0xff)); }
                    for (int i = 0; i < measurementLength; i++) {
                        dataByte = ReadFilterInputPort();
                        measurement = measurement | (dataByte & 0xFF);
                        if (i != measurementLength - 1) { measurement = measurement << 8; }
                        WriteFilterOutputPort(dataByte);
                    }
                }
                else {
                    for (int i = 0; i < measurementLength; i++) {
                        dataByte = ReadFilterInputPort();
                        measurement = measurement | (dataByte & 0xFF);
                        if (i != measurementLength - 1) { measurement = measurement << 8; }
                    }
                    FilterDispatcher(field, Double.doubleToLongBits(ConvertToCelsius(Double.longBitsToDouble(measurement))));
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.println(this.getName() + "::Temperature Filter Exiting ");
                break;
            }
        }
    }

    private void FilterDispatcher( int id, long value) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(id).array();
        for(byte aByte : bytes) WriteFilterOutputPort(aByte);
        
        System.out.println(ByteBuffer.wrap(bytes).getInt());
        
        bytes = ByteBuffer.allocate(8).putLong(value).array();
        for(byte aByte : bytes) WriteFilterOutputPort(aByte);

        System.out.println(ByteBuffer.wrap(bytes).getDouble());
    }

    private double ConvertToCelsius(double v) {
        return ((v - 32) / 1.8);
    }
}
