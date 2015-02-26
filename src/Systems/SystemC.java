package Systems;

import Filters.FileSourceFilter;

public class SystemC {
	public static void main(String argv[]) {
		/**
		 * System C gets two subsets of data, A and B, from files, and merges them together
		 * keeping chronological order.
		 */

		FileSourceFilter fileA = new FileSourceFilter("data/SubSetA.dat");
		FileSourceFilter fileB = new FileSourceFilter("data/SubSetA.dat");

	}
}
