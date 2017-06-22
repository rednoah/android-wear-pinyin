package woogle.lattice;

import java.util.Comparator;


public class LatticeStateComparator implements Comparator<LatticeState> {

	@Override
	public int compare(LatticeState o1, LatticeState o2) {
		if (o1.ngramScore > o2.ngramScore)
			return -1;
		else if (o1.ngramScore == o2.ngramScore)
			return 0;
		else
			return 1;
	}

}
