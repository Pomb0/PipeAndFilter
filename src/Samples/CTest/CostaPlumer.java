package Samples.CTest;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class CostaPlumer {
    public static void main(String argv[]) {
        List<List<Integer>> splitMap1 = Arrays.asList(
                Arrays.asList(0, 2),
                Arrays.asList(0, 4)
        );
        
        // Create Filters
        SourceFilter source = new SourceFilter("data/FlightData.dat");
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap1);
        TemperatureFilter temperature = new TemperatureFilter();
        HeightFilter height = new HeightFilter();
        FieldAggregatorFilter aggregator = new FieldAggregatorFilter();
        SinkFilter toString = new SinkFilter("data/dataTest.dat");

        // Connect Pipes
        toString.Connect(aggregator);
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
        toString.start();
    }
}