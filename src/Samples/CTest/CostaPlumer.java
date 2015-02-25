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

public class CostaPlumer {
    public static void main(String argv[]) {
        int[] array = {0, 1, 2, 3, 4};

        FileSourceFilter Filter1 = new FileSourceFilter("data/FlightData.dat");
        HeightFilter Filter2 = new HeightFilter();
        ToStringFilter Filter3 = new ToStringFilter(array);
        FileSinkFilter Filter4 = new FileSinkFilter("data/data.dat");

        Filter4.Connect(Filter3);
        Filter3.Connect(Filter2);
        Filter2.Connect(Filter1);

        Filter1.start();
        Filter2.start();
        Filter3.start();
        Filter4.start();
    }
}