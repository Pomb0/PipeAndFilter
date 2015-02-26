package Samples.CTest;

import Filters.SourceFilter;
import Filters.SinkFilter;
import Samples.JTest.JaimesTestFilter;

public class CostaPlumer {
    public static void main(String argv[]) {
        
        // Create Filters
        SourceFilter source = new SourceFilter("data/FlightData.dat");
        JaimesTestFilter jaime = new JaimesTestFilter();
        SinkFilter toString = new SinkFilter("data/dataTest.dat");

        // Connect Pipes
        toString.Connect(jaime);
        jaime.Connect(source);
        
        // Start Filters
        source.start();
        jaime.start();
        toString.start();
    }
}