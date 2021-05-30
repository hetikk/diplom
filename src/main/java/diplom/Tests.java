package diplom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws Exception {

        Files.walk(Paths.get("C:\\Users\\hetikk\\IdeaProjects\\diplom\\src\\main\\java\\diplom"))
                .filter(p -> p.getFileName().toString().endsWith("java"))
                .forEach(f -> {
                    System.out.println(f.getFileName());
                    try {
                        System.out.println(FileUtils.readFileToString(f.toFile(), Charset.defaultCharset()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println();
                });

//        final int[] wc = new int[]{5_000, 10_000, 15_000, 20_000, 25_000, 30_000};
//        final int[] fc = new int[]{5, 10, 15, 20};
//
//        List<String> list = readDictionary();
//
//        // При фикс числе доков меняю число слов
//        for (int wci : wc) {
//            String dir = "C:\\Users\\hetikk\\IdeaProjects\\diplom\\sets\\clustering\\6\\6.1\\" + wci + "\\";
//            for (int i = 0; i < 15; i++) {
//                Collections.shuffle(list);
//                String s = list.stream().limit(wci).collect(Collectors.joining(" "));
//                FileUtils.write(new File(dir + (i + 1) + ".txt"), s, Charset.defaultCharset());
//            }
//        }
//
//        // При фикс числе слов меняю колво доков
//        for (int fci : fc) {
//            String dir = "C:\\Users\\hetikk\\IdeaProjects\\diplom\\sets\\clustering\\6\\6.2\\" + fci + "\\";
//            for (int i = 0; i < fci; i++) {
//                Collections.shuffle(list);
//                String s = list.stream().limit(15_000).collect(Collectors.joining(" "));
//                FileUtils.write(new File(dir + fci + "." + (i + 1) + ".txt"), s, Charset.defaultCharset());
//            }
//        }
    }

    static void createDictionary() throws IOException {
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

        String json = gson.toJson(words);
        FileUtils.write(new File("C:\\Users\\hetikk\\IdeaProjects\\diplom\\sets\\dictionary.txt"), json, Charset.defaultCharset());
    }

    static List<String> readDictionary() throws IOException {
        File f = new File("C:\\Users\\hetikk\\IdeaProjects\\diplom\\sets\\dictionary.txt");
        String json = FileUtils.readFileToString(f, Charset.defaultCharset());
        return gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

}
