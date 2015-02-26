package Systems;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class SystemB {
    public static final String FilePath = "data/FlightData.dat";
    public static final String FileLog = "data/PressureLog.dat";
    public static final String FileOutput = "data/StreamB.dat";

    public static void main(String argv[]) {
        /**
         * System B gets a set of data from the file, converts the measures of temperature 
         * from fahrenheit to Celsius, and the altitude from feet to meters.
         * The pressure measures are filtered, the only accepted values are the ones between a given interval.
         * The rejected pressure measurements are extrapolated from the average of the neighboring accepted values.
         * If the first values are rejected, they will be replaced by the first accepted value.
         * If the last values are rejected, they will be replaced by the last accepted value.
         * The rejected measurements will be logged to a file.
         * Finally the extrapolated and the accepted files will be saved to a text file.
         * The extrapolated measurements will precede an *.
         * All other measures are filtered and finally the output is whiten to a file as text.
         */

        /**
         * Splitter Configuration:
         * Each list of integers maps the fields that will be sent to that pipe.
         * There is a direct correspondence between the index of the list and the pipe.
         */
        List<List<Integer>> splitMap = Arrays.asList(
                Arrays.asList(2, 3),
                Arrays.asList(4)
        );

        //Create Filters
        FileSourceFilter file = new FileSourceFilter(FilePath);
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap);
        TemperatureFilter temperature = new TemperatureFilter(4);
        AltitudeFilter altitude = new AltitudeFilter(2);
        FieldAggregatorFilter aggregator = new FieldAggregatorFilter();
        WildPointFilter wildPoint = new WildPointFilter(3, 50, 80);
        PressureExtrapolationFilter pleasureExpr = new PressureExtrapolationFilter(3);
        FileSinkFilter logger = new FileSinkFilter(FileLog);
        FileSinkFilter sink = new FileSinkFilter(FileOutput);

        /**
         * Connect Filters:
         *                       / > Temperature ---\                                 / > PressureExtr ---> Sink
         * File ---> Splitter ---                     > Aggregator ---> WildPoint --->
         *                       \ > Altitude    ---/                                 \ > Logger
         */
        sink.Connect(pleasureExpr);
        pleasureExpr.Connect(wildPoint, 0);
        logger.Connect(wildPoint, 1);
        wildPoint.Connect(aggregator);
        aggregator.Connect(altitude);
        aggregator.Connect(temperature);
        altitude.Connect(splitter, 0);
        temperature.Connect(splitter, 1);
        splitter.Connect(file);

        //Start Filters
        file.start();
        splitter.start();
        altitude.start();
        temperature.start();
        aggregator.start();
        wildPoint.start();
        pleasureExpr.start();
        logger.start();
        sink.start();
    }
}
