package diplom.utils;

import diplom.nlp.FilteredUnigram;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Copy {

    public static final String SEPARATOR = File.separator;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss.S");

    public static void group(String baseDir, File[] files, Map<String, List<String>> groups) throws IOException {
        group(baseDir, files, groups, false);
    }

    public static void group(String baseDir, File[] files, Map<String, List<String>> groups, boolean replace) throws IOException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        baseDir += SEPARATOR + "results" + SEPARATOR + currentDateTime.format(FORMATTER);
        System.out.printf("\nbase dir: %s\n", baseDir);

        Files.createDirectories(Paths.get(baseDir));

        int groupIndex = 1;
        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
            String dirName;
            if (replace) {
                StringBuilder nameBuilder = new StringBuilder();
                String[] names = entry.getKey().split(", ");
                for (int i = 0; i < names.length; i++) {
                    nameBuilder.append(FilteredUnigram.origin.get(names[i]));
                    if (i < names.length - 1) {
                        nameBuilder.append(", ");
                    }
                }

                dirName = firstUpperCase(nameBuilder.toString());
            } else {
                dirName = entry.getKey();
            }

            String dirPath = String.format("%s%sГруппа №%d (%s)", baseDir, SEPARATOR, groupIndex++, dirName);
            Files.createDirectory(Paths.get(dirPath));

            for (String fileName : entry.getValue()) {
                Optional<File> file = Arrays.stream(files).filter(f -> f.getName().equals(fileName)).findFirst();
                if (file.isPresent()) {
                    copy(dirPath, file.get());
                }
            }
        }
    }

    private static void copy(String dir, File file) throws IOException {
        String copy = dir + SEPARATOR + file.getName();
        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(copy), StandardCopyOption.REPLACE_EXISTING);
    }

    private static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";//или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
