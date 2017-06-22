package woogle.lattice;

import java.util.List;


public interface Decoder {
	public void init(Lattice lattice);
	public void decode(Lattice lattice, int beginIndex);
	public void finish(Lattice lattice);
	public List<List<String>> getNBestPath(Lattice lattice, int n);
	public List<String> getOneBestPath(Lattice lattice);
}
