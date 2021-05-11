package diplom.nlp;

import diplom.nlp.stemming.StemmerPorterRU;
import diplom.nlp.stopwords.StopWords;

import java.util.*;

public class FilteredUnigram {

    public static Map<String, String> origin = new HashMap<>();

    public static List<String> get(String text) {
        if (text == null) return Collections.singletonList("");

        String[] words = text.toLowerCase()
                .replaceAll("[^a-zа-я\\s]", "")
                .replaceAll("[\\s]+", " ")
                .split("\\s");

        for (int i = 0; i < words.length; i++) {
            String stem = StemmerPorterRU.stem(words[i]);
            origin.put(stem, words[i]);
            words[i] = stem;
        }

        List<String> uniqueValues = new ArrayList<>(Arrays.asList(words));
        // при вычислении тематического сходства стоп-слова можно игнорировать
        uniqueValues.removeIf(word -> word.equals("") || StopWords.contains(word));
        return uniqueValues;
    }

}