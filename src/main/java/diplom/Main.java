package diplom;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        int[] c = new int[]{5_000, 10_000, 15_000, 20_000, 25_000, 30_000};


        File f = new File("C:\\Users\\hetikk\\IdeaProjects\\diplom\\sets\\clustering\\5\\Новая папка — копия");
        File[] files = f.listFiles();
        List<String> words = new ArrayList<>();
//        Set<String> words = new HashSet<>();
        for (File file : files) {
            String[] s = FileUtils.readFileToString(file, Charset.defaultCharset())
                    .toLowerCase()
                    .replaceAll("[^a-zа-я\\s]", "")
                    .replaceAll("[\\s]+", " ")
                    .split("\\s");
            words.addAll(Arrays.asList(s));
        }

        System.out.println(words.size());
    }

}
