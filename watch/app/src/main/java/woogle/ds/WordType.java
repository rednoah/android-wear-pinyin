package woogle.ds;

public enum WordType {
    Word(0), Char(1), WordChar(2), Other(3);

    private int id;

    WordType(int id) {
        this.id = id;
    }

    public String toString() {
        switch (id) {
        case 0:
            return "Word";
        case 1:
            return "Char";
        case 2:
            return "WordChar";
        default:
            return "Other";
        }
    }
}
