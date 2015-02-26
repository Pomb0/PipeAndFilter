package Framework;

import Framework.Stream.AttributeBean;
import Framework.Stream.FrameBean;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class ExpandedFilterFramework extends FilterFramework{
	protected List<PipedInputStream> inputReadPorts = new Vector<>();
	protected List<PipedOutputStream> outputWritePorts = new Vector<>();
	protected List<FilterFramework> inputFilters = new Vector<>();
	protected List<AttributeBean> inputBuffer = new Vector<>();
	
	
	
	/**
	 * A custom implementation of the run method
	 * delegates the filtering code to the filter method
	 * while taking care of termination on EndOfStreamException
	 * this change is completely optional and the run method can
	 * still be overridden.
	 */
	
	public void filter() throws EndOfStreamException{
	};

	@Override
	public void run(){
		while(true){
			try {
				filter();
			} catch (EndOfStreamException e) {
				ClosePorts();
				System.out.println(this.getClass().getCanonicalName() + "::Closing...");
				break;
			}
		}
	}

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
		} catch (Exception e) {
			System.out.println(this.getClass().getCanonicalName() + "::Problem connecting >>" + e);
		}
	}

	protected void writeFrame(FrameBean frame){
		for(int i = 0; i<outputWritePorts.size(); i++) writeFrame(i, frame);
	}
	protected void writeFrame(int pipeId, FrameBean frame){
		AttributeBean attrib;
		byte[] key;
		byte[] value;
		int i;

		LinkedHashMap<Integer, AttributeBean> map = frame.getAttributeMap();
		for(Map.Entry<Integer, AttributeBean> entry : map.entrySet()){
			attrib = entry.getValue();
			key = attrib.getKey();
			value = attrib.getValue();
			for(i=0;i<AttributeBean.KEYSIZE;i++) WriteFilterOutputPort(pipeId, key[i]);
			for(i=0;i<AttributeBean.VALUESIZE;i++) WriteFilterOutputPort(pipeId, value[i]);
		}
	}

	protected FrameBean readFrame() throws EndOfStreamException{
		return readFrame(0);
	}
	protected FrameBean readFrame(int pipeId) throws EndOfStreamException {
		FrameBean frame = new FrameBean();
		AttributeBean buffer = inputBuffer.get(pipeId);
		inputBuffer.set(pipeId, null);
		
		if(buffer!=null) frame.setAttribute(buffer);
		else frame.setAttribute(readAttribute(pipeId));
		/* Assumes the first attribute in the pipe will be the id */
		
		do{
			try {
				buffer = readAttribute(pipeId);
			} catch (EndOfStreamException e) {
				frame.setAttribute(buffer);
				return frame;
			}
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
		} catch (Exception e) {
			System.out.println(this.getClass().getCanonicalName() + "::Problem reading port, wait loop >>" + e);
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
			} catch (Exception e) {
				System.out.println(this.getClass().getCanonicalName() + "::Problem writing to pipe >>" + e);
			}
		}
	}
	protected void WriteFilterOutputPort(int pipeId, byte datum){
		PipedOutputStream output = outputWritePorts.get(pipeId);
		try {
			output.write((int) datum);
			output.flush();
		} catch (Exception e) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem writing to pipe >>" + e);
		}
	}

	protected PipedOutputStream getOutputWritePortForConnection() {
		return getOutputWritePortForConnection(0);
	}
	private PipedOutputStream getOutputWritePortForConnection(int pipeId){
		if(pipeId != outputWritePorts.size()){ //Has to be continuously indexed, no sparse arrays plz.
            System.out.println(this.getClass().getCanonicalName() + "::Problem getting output port >> Index repeated or not continuous.");
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
			} catch (Exception e) {
                System.out.println(this.getClass().getCanonicalName() + "::Problem closing ports >>" + e);
			}
		}
		inputReadPorts.clear();
		for(PipedOutputStream out : outputWritePorts) { //Close input ports
			try {
				out.close();
			} catch (Exception e) {
                System.out.println(this.getClass().getCanonicalName() + "::Problem closing ports >>" + e);
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
            System.out.println(this.getClass().getCanonicalName() + "::Problem checking if input is alive.");
			return false;
		}
		return !inputFilter.isAlive();
	}
	
}
