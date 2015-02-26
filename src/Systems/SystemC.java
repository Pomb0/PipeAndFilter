package Systems;

import Filters.FileSourceFilter;
import Filters.ZipperFilter;

public class SystemC {
	public static final String FilePathA = "data/SubSetA.dat";
	public static final String FilePathB = "data/SubSetB.dat";
	public static final String FileOutput = "data/";

	public static void main(String argv[]) {
		/**
		 * System C gets two subsets of data, A and B, from files, and merges them together
		 * keeping chronological order.
		 */

		FileSourceFilter fileA = new FileSourceFilter(FilePathA);
		FileSourceFilter fileB = new FileSourceFilter(FilePathB);
		ZipperFilter zipper = new ZipperFilter();


	}
}
