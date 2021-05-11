package diplom.nlp.stopwords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class StopWords {

    private static Set<String> words;

    static {
        words = new HashSet<>();

        ClassLoader loader = StopWords.class.getClassLoader();
        InputStream[] iss = {
                loader.getResourceAsStream("stop-words/fergiemcdowall_stopwords_ru.txt"),
                loader.getResourceAsStream("stop-words/geonetwork-rus.txt"),
                loader.getResourceAsStream("stop-words/stop-words-russian.txt")
        };

        String line;
        BufferedReader br;
        try {
            for (InputStream is : iss) {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    words.add(line);
                }
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("ошибка чтения стоп-слов");
            e.printStackTrace();
        }
    }

    public static boolean contains(String word) {
        return words.contains(word);
    }

}
