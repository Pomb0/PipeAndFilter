package Samples.JTest;

import Filters.FileSinkFilter;
import Filters.FileSourceFilter;
import Filters.ZipperFilter;

public class JaimePlumber {
	public static void main(String argv[]) {

		FileSourceFilter fileA = new FileSourceFilter("data/SubSetA.dat");
		FileSourceFilter fileB = new FileSourceFilter("data/SubSetB.dat");
		ZipperFilter zipper = new ZipperFilter();
		FileSinkFilter sink = new FileSinkFilter("data/AB.dat");

		sink.Connect(zipper);
		zipper.Connect(fileA);
		zipper.Connect(fileB);


		fileA.start();
		fileB.start();
		zipper.start();
		sink.start();



	} // main

} // Plumber