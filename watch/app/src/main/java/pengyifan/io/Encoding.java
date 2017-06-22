package pengyifan.io;

public enum Encoding {
    GBK(0), UTF8(1);

    private int value;

    Encoding(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (value) {
        case 0:
            return "gbk";
        case 1:
            return "utf8";
        }
        return "gbk";
    }
}
