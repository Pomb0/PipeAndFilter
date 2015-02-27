package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.List;

/**
 * Splits a frame into multiple frames acording to the input, each containing the specified fields only.
 * The filed 0 is always added as it represents the begining of a frame.
 */
public class FieldSplitterFilter extends ExpandedFilterFramework{
	private List<List<Integer>> pipeLoad; //Contains a list each element representing a pipe, each contained the fields to be sent.


	/**
	 *
	 * @param list : Each element of the list represents a pipe, and it's elements represent the id of the fields to be sent to that pipe.
	 *             For example the first element of the list will be a list containing the id's to be sent to pipe 0, and so on.
	 */
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
