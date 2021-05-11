package diplom.clustering;

import java.util.List;
import java.util.TreeMap;

public class Vocabulary extends TreeMap<String, Integer> {

    private int nextIndex = 0;

    public void add(String word) {
        if (!this.containsKey(word)) {
            this.put(word, nextIndex++);
        }
    }

    public void addAll(List<String> words) {
        for (String word : words) {
            this.add(word);
        }
    }

}
