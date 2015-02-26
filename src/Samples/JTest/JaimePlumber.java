package Samples.JTest;

import Filters.FieldSplitterFilter;
import Filters.FileSinkFilter;
import Filters.ToStringFilterNew;

import java.util.Arrays;
import java.util.List;

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
public class JaimePlumber {
	public static void main(String argv[]) {
		/****************************************************************************
		 * Here we instantiate three filters.
		 ****************************************************************************/

		List<List<Integer>> splitMap1 = Arrays.asList(
			Arrays.asList(0,2),
			Arrays.asList(4)
		);

		SourceFilter source = new SourceFilter();
		FieldSplitterFilter splitter = new FieldSplitterFilter(splitMap1);
		ToStringFilterNew string1 = new ToStringFilterNew();
		ToStringFilterNew string2 = new ToStringFilterNew();
		FileSinkFilter file1 = new FileSinkFilter("data/out1.dat");
		FileSinkFilter file2 = new FileSinkFilter("data/out2.dat");

		splitter.Connect(source);
		string1.Connect(splitter,0);
		string2.Connect(splitter,1);
		file1.Connect(string1);
		file2.Connect(string2);

		source.start();
		splitter.start();
		string1.start();
		string2.start();
		file1.start();
		file2.start();


	} // main

} // Plumber