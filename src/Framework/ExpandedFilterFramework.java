package Framework;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public abstract class ExpandedFilterFramework extends FilterFramework{
	private PipedInputStream[] InputReadPorts;
	private PipedOutputStream[] OutputWritePorts;
	private FilterFramework[] InputFilters;

	public ExpandedFilterFramework() {
	}

	@Override
	public abstract void run();

	private void test(){
	}
}
