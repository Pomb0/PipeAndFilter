package Samples.CTest;

import Filters.FileSinkFilter;
import Filters.FileSourceFilter;
import Filters.ToStringFilterNew;

public class CostaPlumer {
    public static void main(String argv[]) {
        // Create Filters
        FileSourceFilter source = new FileSourceFilter("data/FlightData.dat");
        ToStringFilterNew toString = new ToStringFilterNew();
        FileSinkFilter sink = new FileSinkFilter("data/data.dat");

        // Connect Pipes
        sink.Connect(toString);
        toString.Connect(source);
        
        // Start Filters
        source.start();
        toString.start();
        sink.start();
    }
}