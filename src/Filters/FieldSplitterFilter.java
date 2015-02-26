package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.List;

public class FieldSplitterFilter extends ExpandedFilterFramework{
	private List<List<Integer>> pipeLoad; //Contains a list each element representing a pipe, each contained the fields to be sent.

	public FieldSplitterFilter(List<List<Integer>> list) {  //Gets a list each representing a pipe, containing the contained fields.
		pipeLoad = list;
	}

	@Override
	public void filter() throws EndOfStreamException {
		FrameBean frame = readFrame();
		FrameBean payLoadFrame;
		int i = 0;

		for(List<Integer> listOfAttribs : pipeLoad){
			payLoadFrame = new FrameBean();
			if(!listOfAttribs.contains(0)) payLoadFrame.setAttribute(frame.getAttribute(0)); //The first element in a frame must always be 0
			for(Integer a : listOfAttribs) payLoadFrame.setAttribute(frame.getAttribute(a));
			writeFrame(i, payLoadFrame);
			i++;
		}

	}

}
