package woogle.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public class WoogleState {
    Locale locale;

    boolean isActive;

    public StringBuilder inputString;

    public List<WoogleLookupCandidate> cands;

    int currentCandPage;

    public WoogleCompResult result;

    public WoogleState() {
        locale = Locale.SIMPLIFIED_CHINESE;
        isActive = true;
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

    public List<String> getCandString(int num) {
        List<String> can = new ArrayList<>();
        int index = 5 * currentCandPage;
        for (int i = 0; i < num; i++) {
            if (i + index < cands.size())
                can.add(getCand(i).getPathWords());
        }
        return can;
    }


    public List<String> getCandidates() {
        return cands.stream().map(c -> {
            return c.getPathWords();
        }).collect(toList());
    }


    public boolean isChinese() {
        return true;
    }

    public boolean isUS() {
        return false;
    }

    public boolean isInputStringEmpty() {
        return inputString.length() == 0;
    }


}
