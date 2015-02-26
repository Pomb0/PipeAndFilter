package Samples.CTest;

import Filters.HeightFilter;
import Filters.SourceFilter;
import Filters.SinkFilter;
import Filters.TemperatureFilter;

public class CostaPlumer {
    public static void main(String argv[]) {
        
        // Create Filters
        SourceFilter source = new SourceFilter("data/FlightData.dat");
        TemperatureFilter temperature = new TemperatureFilter();
        HeightFilter height = new HeightFilter();
        SinkFilter toString = new SinkFilter("data/dataTest.dat");

        // Connect Pipes
        toString.Connect(height);
        height.Connect(temperature);
        temperature.Connect(source);
        
        // Start Filters
        source.start();
        temperature.start();
        height.start();
        toString.start();
    }
}