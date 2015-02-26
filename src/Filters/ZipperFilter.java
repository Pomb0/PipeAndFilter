package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by Jaime on 26/02/2015.
 */
public class ZipperFilter extends ExpandedFilterFramework{
	@Override
	public void filter() throws EndOfStreamException {
		LinkedList<Integer> pipes = new LinkedList<>();
		PriorityQueue<FrameBean> queue = new PriorityQueue<>();

		int i;
		for(i=0;i<inputReadPorts.size();i++) pipes.push(i);

		while(!pipes.isEmpty()){

		}

	}
}
