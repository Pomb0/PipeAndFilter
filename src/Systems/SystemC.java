package Systems;

import Filters.FileSinkFilter;
import Filters.FileSourceFilter;
import Filters.RawFileSinkFilter;
import Filters.ZipperFilter;

public class SystemC {
	public static final String FilePathA = "data/SubSetA.dat";
	public static final String FilePathB = "data/SubSetB.dat";
	public static final String FileOutput = "data/StreamC.dat";

	public static void main(String argv[]) {
		/**
		 * System C gets two subsets of data, A and B, from files, and merges them together
		 * keeping chronological order.
		 */
		//Create Filters.
		FileSourceFilter fileA = new FileSourceFilter(FilePathA);
		FileSourceFilter fileB = new FileSourceFilter(FilePathB);
		ZipperFilter zipper = new ZipperFilter();
		RawFileSinkFilter sink = new RawFileSinkFilter(FileOutput);
		//FileSinkFilter sink = new FileSinkFilter(FileOutput);

		/** 
         * Connect Filters:
		 * FileA ---\
		 *            > Zipper ---> Sink
		 * FileB ---/
		 */
		sink.Connect(zipper);
		zipper.Connect(fileA);
		zipper.Connect(fileB);

		//Start Filters.
		fileA.start();
		fileB.start();
		zipper.start();
		sink.start();
	}
}
