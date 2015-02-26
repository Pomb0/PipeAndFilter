package Filters;

import Framework.ExpandedFilterFramework;
import Framework.Stream.FrameBean;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class ZipperFilter extends ExpandedFilterFramework{
	public void filter() throws EndOfStreamException {
		LinkedList<Integer> pipes = new LinkedList<>();
		PriorityQueue<FrameBean> queue = new PriorityQueue<>();

		int i;
		for(i=0;i<inputReadPorts.size();i++) pipes.push(i);

		while(!pipes.isEmpty()){

		}

	}
}
