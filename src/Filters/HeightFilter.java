package Filters;

import Framework.ExpandedFilterFramework;

public class HeightFilter extends ExpandedFilterFramework {
    int field = 2;
    public void run(){
        int measurementLength = 8;
        int idLength = 4;
        byte dataByte = 0;
        int id;

        long measurement;
        System.out.println(this.getName() + "::Height Filter Starting ");

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
                    FilterDispatcher(ConvertToMeters(Double.longBitsToDouble(measurement)));
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.println(this.getName() + "::Height Filter Exiting ");
                break;
            }
        }
    }

    private void FilterDispatcher(double value) {
        for(int i = 0 ; i < 4 ; i++){ WriteFilterOutputPort((byte)((field >> ((7 - i) * 8)) & 0xff)); }
        for(int i = 0; i < 8; i++) {
            WriteFilterOutputPort((byte)((Double.doubleToLongBits(value) >> ((7 - i) * 8)) & 0xff));
        }
    }

    private double ConvertToMeters(double v) {
        System.out.println(v);
        return v / 3.2808;
    }
}
