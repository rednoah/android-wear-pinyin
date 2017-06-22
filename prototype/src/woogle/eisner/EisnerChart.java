package woogle.eisner;

public class EisnerChart {

    double         c[][][][];

    DependencyTree a[][][][];

    int            size;

    public EisnerChart() {
        c = new double[100][100][2][2];
        a = new DependencyTree[100][100][2][2];
    }

    public void init(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                c[i][j][0][0] = 0;
                c[i][j][0][1] = 0;
                c[i][j][1][0] = 0;
                c[i][j][1][1] = 0;
                a[i][j][0][0] = new DependencyTree(size);
                a[i][j][0][1] = new DependencyTree(size);
                a[i][j][1][0] = new DependencyTree(size);
                a[i][j][1][1] = new DependencyTree(size);
            }
        }
    }

    public DependencyTree getTree(int row, int column, int dir, int con) {
        return a[row][column][dir][con];
    }

    public double getScore(int row, int column, int dir, int con) {
        return c[row][column][dir][con];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String line = String.format("[%d,%d]: %f, %f, %f, %f\n", i, j,
                        c[i][j][0][0], c[i][j][0][1], c[i][j][1][0],
                        c[i][j][1][1]);
                sb.append(line);
                sb.append(a[i][j][1][0]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
