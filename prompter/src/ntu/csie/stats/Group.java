package ntu.csie.stats;

import static java.util.Arrays.*;

import java.util.HashSet;
import java.util.Set;

public enum Group {

	NonNative("15D15A08A88", "15D1BE4BE42", "15D1FFF44BD", "15D210C7136", "15D2A5DEC20", "15D2528FA3F"),

	FatFingers("15D1297BBD1", "15D17F5D8DE", "15D1ABF11ED", "15D1BE4BE42", "15D210C7136", "15D217ACFA6", "15D21C2AEBB", "15D2528FA3F", "15D2BEEE130", "15D2CB98F33");

	private Set<String> sessions;

	Group(String... sessions) {
		this.sessions = new HashSet<>(asList(sessions));
	}

	public boolean accept(Sample s) {
		return sessions.contains(s.getSession());
	}

}
