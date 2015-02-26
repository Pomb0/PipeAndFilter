package Systems;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class SystemA {
    public static final String FilePath = "data/FlightData.dat";
    public static final String FileOutput = "data/StreamA.dat";

    public static void main(String argv[]) {
        /**
         * System A gets a set of data from the file, converts the measures of temperature 
         * from fahrenheit to Celsius, and the altitude from feet to meters.
         * All other measures are filtered and finally the output is whiten to a file as text.
         */

        /**
         * Splitter Configuration:
         * Each list of integers maps the fields that will be sent to that pipe.
         * There is a direct correspondence between the index of the list and the pipe.
         */
        List<List<Integer>> splitMap = Arrays.asList(
                Arrays.asList(4),
                Arrays.asList(2)
        );
        
        //Create Filters.
        FileSourceFilter file = new FileSourceFilter(FilePath);
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap);
        TemperatureFilter temperature = new TemperatureFilter(4);
        AltitudeFilter altitude = new AltitudeFilter(2);
        FieldAggregatorFilter aggregator = new FieldAggregatorFilter();
        FileSinkFilter sink = new FileSinkFilter(FileOutput);
        

        /** 
         * Connect Filters:
         *                       / > Temperature ---\
         * File ---> Splitter ---                     > Aggregator ---> Sink
         *                       \ > Altitude    ---/
         */
        sink.Connect(aggregator);
        aggregator.Connect(temperature);
        aggregator.Connect(altitude);
        temperature.Connect(splitter, 0);
        altitude.Connect(splitter, 1);
        splitter.Connect(file);

        //Start Filters.
        file.start();
        splitter.start();
        altitude.start();
        temperature.start();
        aggregator.start();
        sink.start();
    }
}
