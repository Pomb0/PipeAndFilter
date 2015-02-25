package Framework;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public abstract class ExpandedFilterFramework extends FilterFramework{

	protected PipedInputStream[] InputReadPort;
	protected PipedOutputStream[] OutputWritePort;
	protected FilterFramework[] InputFilter;

	public ExpandedFilterFramework() {
	}

	@Override
	public abstract void run();

	private void test(){
	}
}
