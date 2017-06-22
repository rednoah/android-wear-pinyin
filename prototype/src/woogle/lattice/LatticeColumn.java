package woogle.lattice;

import java.util.ArrayList;
import java.util.List;

public class LatticeColumn {
	List<LatticeState> states;
	int selectedIndex;
	
	private static final int NOT_SELECT = -1;
	
	public LatticeColumn() {
		states = new ArrayList<LatticeState>();
		selectedIndex = NOT_SELECT;
	}
	
	public boolean isSelected() {
	    return selectedIndex != NOT_SELECT;
	}
	
	public LatticeState getState(int index) {
		return states.get(index);
	}
	
	public LatticeState getSelectedState() {
		return getState(selectedIndex);
	}
	
	public int getStateSize() {
		return states.size();
	}
}
