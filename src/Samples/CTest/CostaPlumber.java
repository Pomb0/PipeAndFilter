package Samples.CTest;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class CostaPlumber {
    public static void main(String argv[]) {
        
        // 00 - Time
        // 01 - Speed
        // 02 - Altitude
        // 03 - Pressure
        // 04 - Temperature
        // 05 - Pitch
        
        // Splitter configuration
        List<List<Integer>> splitMap1 = Arrays.asList(
                Arrays.asList(2, 3),
                Arrays.asList(4)
        );
        
        // Create Filters
        FileSourceFilter source = new FileSourceFilter("data/FlightData.dat");
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap1);
        TemperatureFilter temperature = new TemperatureFilter(4);
        AltitudeFilter height = new AltitudeFilter(2);
        FieldAggregatorFilter aggregator = new FieldAggregatorFilter();
        WildPointFilter wild = new WildPointFilter(3, 50, 80);
        FileSinkFilter logger = new FileSinkFilter("data/logger.dat");
        PressureExtrapolationFilter takeOut = new PressureExtrapolationFilter(3);
        FileSinkFilter toString = new FileSinkFilter("data/dataTest.dat");

        // Connect Pipes
        toString.Connect(takeOut);
        takeOut.Connect(wild, 0);
        logger.Connect(wild, 1);
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
        takeOut.start();
        logger.start();
    }
}