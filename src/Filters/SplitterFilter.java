package Filters;

import Framework.ExpandedFilterFramework;

import java.util.List;
import java.util.Vector;

/**
 * Created by Jaime on 25/02/2015.
 */
public class SplitterFilter extends ExpandedFilterFramework{
	private int outputCount;
	private List<List<Integer>> fieldDestiny; //Contains a list of fields, each containing a list of pipes!

	public SplitterFilter(List<List<Integer>> map) {  //Gets a list each representing a pipe, containing the contained fields.
		this.fieldDestiny = new Vector<>();
		initializeSplitterMap(map);
	}

	@Override
	public void run() {

	}

	public void initializeSplitterMap(List<List<Integer>> map){
		int i = 0;
		for(List<Integer> list : map){
			for(Integer f : list){
				if(fieldDestiny.get(f)==null) fieldDestiny.add(f, new Vector<Integer>());
				fieldDestiny.get(f).add(i);
			}
			i++;
		}
	}
}
