package Framework;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Vector;

public abstract class ExpandedFilterFramework extends FilterFramework{
	protected List<PipedInputStream> inputReadPorts = new Vector<>();
	protected List<PipedOutputStream> outputWritePorts = new Vector<>();
	protected List<FilterFramework> inputFilters = new Vector<>();

	public ExpandedFilterFramework() {
	}

	@Override
	public abstract void run();

	public void Connect(ExpandedFilterFramework filter) {
		Connect(filter, 0);
	}

	public void Connect(ExpandedFilterFramework filter, int pipeId) {
		PipedInputStream inputStream = new PipedInputStream();

		try {
			// Connect this filter's input to the upstream pipe's output stream
			inputStream.connect(filter.getOutputWritePortForConnection(pipeId));
			inputReadPorts.add(pipeId, inputStream);
			inputFilters.add(filter);
		} catch (Exception Error) {
			System.out.println(this.getName() + " FilterFramework error connecting::" + Error);
		}
	}

	protected byte ReadFilterInputPort() throws EndOfStreamException {
		return ReadFilterInputPort(0);
	}

	protected byte ReadFilterInputPort(int pipeId) throws EndOfStreamException {
		byte datum = 0;
		PipedInputStream inputPort = inputReadPorts.get(pipeId);
		try {
			while (inputPort.available() == 0) {
				if (EndOfInputStream()) {
					throw new EndOfStreamException("End of input stream reached");
				}
				sleep(250);
			}
		}catch (EndOfStreamException Error) {
			throw Error;
		} catch (Exception Error) {
			System.out.println("\n" + this.getName() + " Error in read port wait loop::" + Error);
		}

		try {
			datum = (byte) inputPort.read();
			return datum;
		}catch (Exception Error) {
			System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
			return datum;
		}
	}

	protected void WriteFilterOutputPort(int pipeId, byte datum){
		PipedOutputStream output = outputWritePorts.get(pipeId);
		try {
			output.write((int) datum);
			output.flush();
		} catch (Exception Error) {
			System.out.println("\n" + this.getName() + " Pipe write error::" + Error);
		}
	}

	protected void WriteFilterOutputPort(byte datum) {
		for(PipedOutputStream output : outputWritePorts) {
			try {
				output.write((int) datum);
				output.flush();
			} catch (Exception Error) {
				System.out.println("\n" + this.getName() + " Pipe write error::" + Error);
			}
		}
	}

	protected PipedOutputStream getOutputWritePortForConnection() {
		return getOutputWritePortForConnection(0);
	}

	private PipedOutputStream getOutputWritePortForConnection(int pipeId){
		if(pipeId > outputWritePorts.size()){ //Has to be continuously indexed, no sparse arrays plz.
			System.out.println(this.getName() + " [ERROR] input connections must be indexed using continuous integers.");
			return null;
		}
		PipedOutputStream outputStream = new PipedOutputStream();
		outputWritePorts.add(pipeId, outputStream);
		return outputStream;
	}

	protected void ClosePorts() { //Close input ports
		for(PipedInputStream in : inputReadPorts) {
			try {
				in.close();
			} catch (Exception Error) {
				System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);
			}
		}
		for(PipedOutputStream out : outputWritePorts) { //Close input ports
			try {
				out.close();
			} catch (Exception Error) {
				System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);
			}
		}
	}

}
