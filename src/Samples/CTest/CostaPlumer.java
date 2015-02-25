/**
 * ***************************************************************************************************************
 * File:Plumber.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 * 1.0 November 2008 - Sample Pipe and Filter code (ajl).
 * <p/>
 * Description:
 * <p/>
 * This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
 * instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
 * that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
 * of useful things that you can do with the input stream of data.
 * <p/>
 * Parameters: 		None
 * <p/>
 * Internal Methods:	None
 * <p/>
 * ****************************************************************************************************************
 */

package Samples.CTest;

import Filters.*;

import java.util.Arrays;
import java.util.List;

public class CostaPlumer {
    public static void main(String argv[]) {
        int[] array = {0, 1, 2, 3, 4};

        List<List<Integer>> splitMap1 = Arrays.asList(
                Arrays.asList(0, 2),
                Arrays.asList(0, 4)
        );

        FileSourceFilter source = new FileSourceFilter("data/FlightData.dat");
        FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap1);
        
        HeightFilter height = new HeightFilter();
        TemperatureFilter temperature = new TemperatureFilter();
        
        ToStringFilter toString1 = new ToStringFilter(array);
        FileSinkFilter sink1 = new FileSinkFilter("data/data1.dat");

        ToStringFilter toString2 = new ToStringFilter(array);
        FileSinkFilter sink2 = new FileSinkFilter("data/data2.dat");
        
        sink2.Connect(toString2);
        sink1.Connect(toString1);
        toString2.Connect(temperature);
        toString1.Connect(height);
        height.Connect(splitter, 0);
        temperature.Connect(splitter, 1);
        splitter.Connect(source);
        
        source.start();
        splitter.start();
        height.start();
        temperature.start();
        toString1.start();
        toString2.start();
        sink1.start();
        sink2.start();
    }
}