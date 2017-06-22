package woogle.eisner;

public class DependencyTree {
    
    /**
     * dependents[i][j]: i --> j
     */
    public boolean[][]           dependents;

    private static final boolean NO_DEP = false;

    public DependencyTree(int size) {
        dependents = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dependents[i][j] = NO_DEP;
            }
        }
    }

    /**
     * head --> dependent
     * 
     * @param indexOfHead
     * @param indexOfDependent
     */
    public void add(int indexOfHead, int indexOfDependent) {
        dependents[indexOfHead][indexOfDependent] = true;
    }

    public int getSize() {
        return dependents.length;
    }

    public void add(DependencyTree tree) {
        int size = tree.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tree.dependents[i][j] != NO_DEP) {
                    add(i, j);
                }
            }
        }
    }

    public DependencyTree clone() {
        DependencyTree tree = new DependencyTree(getSize());
        tree.add(this);
        return tree;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(i + ":");
            for (int j = 0; j < getSize(); j++) {
                if (dependents[i][j] != NO_DEP)
                    sb.append("\t" + j);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toString(String sentence[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(i + "\t" + sentence[i] + ":");
            for (int j = 0; j < getSize(); j++) {
                if (dependents[i][j] != NO_DEP)
                    sb.append("\t" + j);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
