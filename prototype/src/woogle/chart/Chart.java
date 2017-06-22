package woogle.chart;

public class Chart {

    public ChartCell table[][];

    public int       num;

    public Chart() {
        table = new ChartCell[100][100];
        num = 0;
    }

    public void clear() {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j <= i; j++) {
                this.table[i][j] = null;
            }
        }
        this.num = 0;
    }

    public void setCell(ChartCell cell, int row, int column) {
        table[row][column] = cell;
        num = column + 1;
    }

    public ChartCell getCell(int row, int column) {
        if (row < num && row <= column)
            return table[row][column];
        else
            return null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.num; i++) {
            sb.append(table[0][i]);
            sb.append('\n');
        }
        return sb.toString();
    }
}