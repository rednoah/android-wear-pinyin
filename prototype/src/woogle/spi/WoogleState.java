package woogle.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WoogleState {
    Locale                             locale;

    boolean                            isActive;

    public StringBuilder               inputString;

    public List<WoogleLookupCandidate> cands;

    int                                currentCandPage;

    public WoogleCompResult            result;

    public WoogleState() {
        locale = Locale.SIMPLIFIED_CHINESE;
        isActive = false;
        inputString = new StringBuilder();
        cands = new ArrayList<WoogleLookupCandidate>();
        currentCandPage = 0;
        result = new WoogleCompResult();
    }

    public boolean isLastPage() {
        return (currentCandPage + 1) * 5 >= cands.size();
    }

    public boolean isFirstPage() {
        return currentCandPage <= 0;
    }

    public void pageUp() {
        currentCandPage--;
    }

    public void pageDown() {
        currentCandPage++;
    }

    public WoogleLookupCandidate getCand(int index) {
        return cands.get(currentCandPage * 5 + index);
    }

    public String[] getCandString(int num) {
        String[] can = new String[num];
        int index = 5 * currentCandPage;
        for (int i = 0; i < num; i++) {
            if (i + index < cands.size())
                can[i] = (i + 1) + "." + getCand(i).getPathWords();
            else
                can[i] = (i + 1) + ".  ";
        }
        return can;
    }

    public boolean isSimplefiedChinese() {
        return locale.equals(Locale.SIMPLIFIED_CHINESE);
    }

    public boolean isUS() {
        return locale.equals(Locale.US);
    }

    public boolean isInputStringEmpty() {
        return inputString.length() == 0;
    }

    public String getLocaleName() {
        return locale == null ? "нч" : locale.getDisplayName();
    }
}
