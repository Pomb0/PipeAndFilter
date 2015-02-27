package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Merges two ordered streams keeping order based on attribute 0.
 */
public class ZipperFilter extends ExpandedFilterFramework{
	@Override
	public void filter() throws EndOfStreamException {
		int i,p;
		int size = inputReadPorts.size();
		FrameBean frame;
		LinkedList<Integer> pipes = new LinkedList<>();
		PriorityQueue<FrameBean> queue = new PriorityQueue<FrameBean>(size, new ChronologicalFrameComparator());
		for(i=0;i<inputReadPorts.size();i++) pipes.push(i);

		i = 0;
		while(!pipes.isEmpty()){
			p = pipes.pop();
			try{
				frame = readFrame(p);
				queue.add(frame);
				i++;
				pipes.addLast(p);
			}catch (EndOfStreamException exception){
				size--;
			}

			if(i>=size){ //Read one from each, read all pipes!
				i = 0;
				frame = queue.remove();
				writeFrame(frame);
			}
		}
		while(!queue.isEmpty()) writeFrame(queue.remove());
		throw new EndOfStreamException("End of input stream reached");
	}
}


class ChronologicalFrameComparator implements Comparator<FrameBean> {
	@Override
	public int compare(FrameBean x, FrameBean y) {
		if( x.getAttribute(0).getValueAsLong() < y.getAttribute(0).getValueAsLong() ) return -1;
		if( x.getAttribute(0).getValueAsLong() > y.getAttribute(0).getValueAsLong() ) return 1;
		return 0;
	}
}
