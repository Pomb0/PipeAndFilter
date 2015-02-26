package Samples.CTest;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class CostaPlumber {
    public static void main(String argv[]) {
        
        // Splitter configuration
        List<List<Integer>> splitMap1 = Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(4)
        );
        
        // Create Filters
        FileSourceFilter source = new FileSourceFilter("data/FlightData.dat");
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap1);
        TemperatureFilter temperature = new TemperatureFilter(4);
        HeightFilter height = new HeightFilter(2);
        FieldAggregatorFilter aggregator = new FieldAggregatorFilter();
        WildPointFilter wild = new WildPointFilter(3, 50, 80);
        FileSinkFilter toString = new FileSinkFilter("data/dataTest.dat");

        // Connect Pipes
        toString.Connect(wild);
        wild.Connect(aggregator);
        aggregator.Connect(height);
        aggregator.Connect(temperature);
        height.Connect(splitter, 0);
        temperature.Connect(splitter, 1);
        splitter.Connect(source);
        
        // Start Filters
        source.start();
        splitter.start();
        temperature.start();
        height.start();
        aggregator.start();
        wild.start();
        toString.start();
    }
}