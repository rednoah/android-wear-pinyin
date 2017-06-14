package woogle.cky;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SyllableNode {
	
	public String syllable;
	public boolean isTerminal;
	public SyllableNode parent;
	public HashMap<String, SyllableNode> children;
	
	public SyllableNode() {
		this(null, false, null, new HashMap<String, SyllableNode>());
	}
	
	public SyllableNode(String syllable, boolean isTerminal,
			SyllableNode parent) {
		this(syllable, isTerminal, parent, new HashMap<String, SyllableNode>());
	}
	
	public SyllableNode(String syllable, boolean isTerminal,
			SyllableNode parent, HashMap<String, SyllableNode> children) {
		this.syllable = syllable;
		this.isTerminal = isTerminal;
		this.parent = parent;
		this.children = children;
	}

	public void addSyllable(String syllable) {
		if(syllable.isEmpty())
			return;
		String sub = syllable.substring(0, 1);
		SyllableNode node = children.get(sub);
		if(node == null) {
			node = new SyllableNode(sub, false, this);
			children.put(sub, node);
		}
		if (sub.length() == 1)
			node.isTerminal = true;
		// ตÝน้
		node.addSyllable(syllable.substring(1));
	}
	
	public static void write(PrintWriter out, SyllableNode node, int indent) {
		if(!(node instanceof RootSyllableNode)) {
			write(out, indent);
			out.print(node.syllable);
			if(node.isTerminal)
				out.print("_");
			out.println();
		}
		for(SyllableNode n: node.children.values()) {
			write(out, n, indent+1);
		}
	}
	
	public static void write(PrintWriter out, int indent) {
		for(int i=0; i<indent; ++i)
			out.print(" ");
	}
	
	public List<String> getAllSyllables() {
		List<String> syllables = new ArrayList<String>();
		StringBuffer startSyslable = new StringBuffer();
		SyllableNode parent = this;
		while(!(parent instanceof RootSyllableNode)) {
			startSyslable.insert(0, parent.syllable);
			parent = parent.parent;
		}
		if(isTerminal)
			syllables.add(startSyslable.toString());
		if(!children.isEmpty()) {
			for(SyllableNode node: children.values()) {
				for(String s: node.getAllSubsequentSyllables())
					syllables.add(startSyslable.toString() + s);
			}
		}
		return syllables;
	}
	
	public List<String> getAllSubsequentSyllables() {
		List<String> syllables = new ArrayList<String>();
		if(isTerminal)
			syllables.add(syllable);
		if(!children.isEmpty()) {
			for(SyllableNode node: children.values()) {
				for(String s: node.getAllSubsequentSyllables())
					syllables.add(syllable + s);
			}
		}
		return syllables;
	}
}

class RootSyllableNode extends SyllableNode {
	public List<String> find(String syllable) {
		SyllableNode node = this;
		while(syllable.length() > 0) {
			String first = syllable.substring(0, 1);
			node = node.children.get(first);
			if(node != null) {
				syllable = syllable.substring(1);
			} else {
				break;
			}
		}
		List<String> l = new ArrayList<String>();
		if (node != null) {
			l.addAll(node.getAllSyllables());
		}
		return l;
	}
}