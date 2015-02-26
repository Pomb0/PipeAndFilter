package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by Jaime on 26/02/2015.
 */
public class ZipperFilter extends ExpandedFilterFramework{
	@Override
	public void filter() throws EndOfStreamException {
		int i,p;
		int size = inputReadPorts.size();
		LinkedList<Integer> pipes = new LinkedList<>();
		PriorityQueue<FrameBean> queue = new PriorityQueue<FrameBean>(size, new ChronologicalFrameComparator());
		for(i=0;i<inputReadPorts.size();i++) pipes.push(i);

		while(!pipes.isEmpty()){
		//TODO Finish this fucking shit :p
		}

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
