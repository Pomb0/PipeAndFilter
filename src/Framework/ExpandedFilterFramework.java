package Framework;

import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Vector;

public abstract class ExpandedFilterFramework extends FilterFramework{
	protected List<PipedInputStream> inputReadPorts = new Vector<>();
	protected List<PipedOutputStream> outputWritePorts = new Vector<>();
	protected List<FilterFramework> inputFilters = new Vector<>();
	protected List<AttributeBean> inputBuffer = new Vector<>();

	@Override
	public abstract void run();

	@Override
	public void Connect(FilterFramework Filter) {
		Connect((ExpandedFilterFramework)Filter);
	}
	public void Connect(ExpandedFilterFramework filter) {
		Connect(filter, 0);
	}
	public void Connect(ExpandedFilterFramework filter, int pipeId) {
		PipedInputStream inputStream = new PipedInputStream();

		try {
			// Connect this filter's input to the upstream pipe's output stream
			inputStream.connect(filter.getOutputWritePortForConnection(pipeId));
			inputReadPorts.add(inputStream);
			inputBuffer.add(null);
			inputFilters.add(filter);
		} catch (Exception Error) {
			System.out.println(this.getName() + " FilterFramework error connecting::" + Error);
		}
	}

	protected FrameBean getFrame() throws EndOfStreamException{
		return getFrame(0);
	}
	protected FrameBean getFrame(int pipeId) throws EndOfStreamException {
		FrameBean frame = new FrameBean();
		AttributeBean buffer = inputBuffer.get(pipeId);
		
		if(buffer!=null) frame.setAttribute(buffer);
		else frame.setAttribute(readAttribute(pipeId));
		/* Assumes the first attribute in the pipe will be the id */
		
		do{
			buffer = readAttribute(pipeId);
			if(buffer.getKeyAsInt() != 0){
				frame.setAttribute(buffer);
			}else{
				inputBuffer.set(pipeId, buffer);
				buffer=null;
			}
		}while(buffer!=null);
		
		return frame;
	}


	protected AttributeBean readAttribute() throws EndOfStreamException {
		return readAttribute(0);
	}
	protected AttributeBean readAttribute(int pipeId) throws EndOfStreamException {
		AttributeBean attrib = new AttributeBean();
		byte[] key = attrib.getKey();
		byte[] value = attrib.getValue();
		int i;
		
		for(i=0;i<AttributeBean.KEYSIZE;i++) key[i] = ReadFilterInputPort(pipeId);
		for(i=0;i<AttributeBean.VALUESIZE;i++) value[i] = ReadFilterInputPort(pipeId);
		
		return attrib;
	}
	
	@Override
	protected byte ReadFilterInputPort() throws EndOfStreamException {
		return ReadFilterInputPort(0);
	}
	protected byte ReadFilterInputPort(int pipeId) throws EndOfStreamException {
		byte datum = 0;
		PipedInputStream inputPort = inputReadPorts.get(pipeId);
		try {
			while (inputPort != null && inputPort.available() == 0) {
				if (EndOfInputStream()) {
					throw new EndOfStreamException("End of input stream reached");
				}
				sleep(250);
			}
		}catch (EndOfStreamException Error) {
			throw Error;
		} catch (Exception Error) {
			System.out.println(this.getName() + " Error in read port wait loop::" + Error);
		}

		try {
			datum = (byte) inputPort.read();
			return datum;
		}catch (Exception Error) {
			return datum;
		}
	}

	@Override
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
	protected void WriteFilterOutputPort(int pipeId, byte datum){
		PipedOutputStream output = outputWritePorts.get(pipeId);
		try {
			output.write((int) datum);
			output.flush();
		} catch (Exception Error) {
			System.out.println(this.getName() + " Pipe write error::" + Error);
		}
	}

	protected PipedOutputStream getOutputWritePortForConnection() {
		return getOutputWritePortForConnection(0);
	}
	private PipedOutputStream getOutputWritePortForConnection(int pipeId){
		if(pipeId != outputWritePorts.size()){ //Has to be continuously indexed, no sparse arrays plz.
			System.out.println(this.getName() + " [ERROR] index repeated or not continuous!.");
			return null;
		}
		PipedOutputStream outputStream = new PipedOutputStream();
		outputWritePorts.add(pipeId, outputStream);
		return outputStream;
	}

	@Override
	protected void ClosePorts() { //Close input ports
		for(PipedInputStream in : inputReadPorts) {
			try {
				in.close();
			} catch (Exception Error) {
				System.out.println(this.getName() + " ClosePorts error::" + Error);
			}
		}
		inputReadPorts.clear();
		for(PipedOutputStream out : outputWritePorts) { //Close input ports
			try {
				out.close();
			} catch (Exception Error) {
				System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);
			}
		}
		outputWritePorts.clear();
	}

	@Override
	protected boolean EndOfInputStream() {
		return EndOfInputStream(0);
	}
	protected boolean EndOfInputStream(int inputFilterId) {
		FilterFramework inputFilter = inputFilters.get(inputFilterId);
		if(inputFilter == null){
			System.out.println("ERROR CHECKING IF INPUT IS ALIVE");
			return false;
		}
		return !inputFilter.isAlive();
	}

}
