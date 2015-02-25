package Filters;

import Framework.ExpandedFilterFramework;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class FieldSplitterFilter extends ExpandedFilterFramework{
	private HashMap<Integer, List<Integer>> fieldDestiny; //Contains a list of fields, each containing a list of pipes!

	public FieldSplitterFilter(List<List<Integer>> map) {  //Gets a list each representing a pipe, containing the contained fields.
		this.fieldDestiny = new HashMap<>();
		initializeSplitterMap(map);
	}

	@Override
	public void run() {
		int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
		int IdLength = 4;                // This is the length of IDs in the byte stream

		byte databyte = 0;                // This is the data byte read from the stream
		int bytesread = 0;                // This is the number of bytes read from the stream

		long measurement;                // This is the word used to store all measurements - conversions are illustrated.
		int id;                            // This is the measurement id
		int i;                            // This is a loop counter



		while (true) {
			try {
				id = 0;
				for (i = 0; i < IdLength; i++) {
					databyte = ReadFilterInputPort();
					id = id | (databyte & 0xFF);
					if (i != IdLength - 1) id = id << 8;
					bytesread++;
				}

				for (measurement = i = 0; i < MeasurementLength; i++) {
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);
					if (i != MeasurementLength - 1) measurement = measurement << 8;
					bytesread++;
				}

				List<Integer> toPipes = this.fieldDestiny.get(id);
                if(toPipes != null)
				for(Integer p : toPipes) {
					for (i = 0; i < IdLength; i++) WriteFilterOutputPort(p, (byte) ((id >> ((7 - i) * 8)) & 0xff));
					for (i = 0; i < MeasurementLength; i++) WriteFilterOutputPort(p, (byte) ((measurement >> ((7 - i) * 8)) & 0xff));
				}


			} catch (EndOfStreamException e) {
				ClosePorts();
				System.out.println(this.getName() + "::Spliter Exiting; bytes read: " + bytesread);
				break;
			}

		}
	}

	public void initializeSplitterMap(List<List<Integer>> map){
		int i = 0;
		for(List<Integer> list : map){
			for(Integer f : list){
				if(!fieldDestiny.containsKey(f)) fieldDestiny.put(f, new Vector<Integer>());
				fieldDestiny.get(f).add(i);
			}
			i++;
		}
	}
}
