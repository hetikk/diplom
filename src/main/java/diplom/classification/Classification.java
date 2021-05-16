package diplom.classification;

import com.google.gson.reflect.TypeToken;
import diplom.Application;
import diplom.distance.Distance;
import diplom.nlp.FilteredUnigram;
import diplom.utils.Copy;
import diplom.utils.Reader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static diplom.utils.Copy.SEPARATOR;

public class Classification {

    private static final String OTHER_CLASS_NAME = "Прочее (!)";
    private Distance distance;
    private Map<String, double[]> dataMap;
    private List<String> vocabulary;
    private int CLASSES_END;
    private StringBuilder builder;

    private double initTime;
    private double workTime;

    public Classification(List<Class> classes, Distance distance) {
        initTime = System.nanoTime();

        this.distance = distance;
        this.dataMap = new LinkedHashMap<>();
        this.CLASSES_END = classes.size() + 1;
        this.builder = new StringBuilder();

        vocabulary = new ArrayList<>();

        for (Class c : classes) {
            String text = String.join(" ", c.termsFrequency.keySet());
            vocabulary.addAll(FilteredUnigram.get(text));
        }

        vocabulary = vocabulary.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        System.out.printf("%10s - %s\n", "VOCABULARY", vocabulary);
        if (Application.debug)
            builder.append(String.format("%10s - %s\n", "VOCABULARY", vocabulary));

        for (Class c : classes) {
            double[] dataRow = new double[vocabulary.size()];
            Arrays.fill(dataRow, 0.0);
            for (Map.Entry<String, Integer> entry : c.termsFrequency.entrySet()) {
                List<String> terms = FilteredUnigram.get(entry.getKey());
                for (String term : terms) {
                    int index = vocabulary.indexOf(term);
                    dataRow[index] = entry.getValue();
                }
            }
            dataMap.put(c.name, dataRow);
        }

        double[] dataRow = new double[vocabulary.size()];
        dataMap.put(OTHER_CLASS_NAME, dataRow);

        initTime = (System.nanoTime() - initTime) / 1000000;
        System.out.printf("ВРЕМЯ ИНИЦИАЛИЗАЦИИ (classify): %.2f мс\n", initTime);
    }

    public static List<Class> parseClasses(File classesFile) throws IOException {
        String json = FileUtils.readFileToString(classesFile, Charset.defaultCharset());
        return Application.gson.fromJson(json, new TypeToken<List<Class>>() {
        }.getType());
    }

    public Map<String, List<String>> classify(File[] path) throws IOException {
        workTime = System.nanoTime();

        List<File> rawFiles = Arrays.stream(path).collect(Collectors.toList());

        addFilesToMatrix(rawFiles);

        normalize();

        Map<String, List<String>> result = new TreeMap<>();
        List<String> otherFiles = new ArrayList<>();
        dataMap.entrySet().stream().skip(CLASSES_END).forEach(fileRow -> {
            AtomicReference<String> className = new AtomicReference<>("");
            AtomicReference<Double> dist = new AtomicReference<>((double) 0);
            AtomicReference<String> fileName = new AtomicReference<>("");

            dataMap.entrySet().stream().limit(CLASSES_END).forEach(classRow -> {
                double d = distance.calc(fileRow.getValue(), classRow.getValue());
                if (d > dist.get()) {
                    className.set(classRow.getKey());
                    dist.set(d);
                    fileName.set(fileRow.getKey());
                }
            });

            if (dist.get() == 0) {
                otherFiles.add(fileRow.getKey());
            } else {
                String cName = className.get();
                if (!result.containsKey(cName))
                    result.put(cName, new ArrayList<String>() {{
                        add(fileName.get());
                    }});
                else
                    result.get(className.get()).add(fileName.get());

            }
        });

        result.put(OTHER_CLASS_NAME, otherFiles);

        if (Application.debug)
            builder.append("\nРАСПРЕДЕЛЕННЫЕ ФАЙЛЫ:\n");
        else
            builder.append("РАСПРЕДЕЛЕННЫЕ ФАЙЛЫ:\n");
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            System.out.printf("%10s - %s\n", entry.getKey(), entry.getValue());
            builder.append(String.format("%s - %s\n", entry.getKey(), entry.getValue()));
        }

        String dir = "sets" + SEPARATOR + "classification";
        Copy.group(dir, path, result);

        workTime = (System.nanoTime() - workTime) / 1000000;
        System.out.printf("ВРЕМЯ РАБОТЫ (classify): %.2f мс\n", workTime);
        if (Application.time)
            builder.append("\nВРЕМЯ РАБОТЫ: ").append(String.format("%.2f мс", workTime));

        return result;
    }

    private void addFilesToMatrix(List<File> files) throws IOException {
        for (File f : files) {
            String text = Reader.readFile(f);
            List<String> strings = FilteredUnigram.get(text);

            double[] dataRow = new double[vocabulary.size()];
            for (Map.Entry<String, Long> term : strings.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()) {
                int index = vocabulary.indexOf(term.getKey());
                if (index > 0) // на случай, если в словаре нет такого термина
                    dataRow[index] = term.getValue();
            }
            dataMap.put(f.getName(), dataRow);
        }

        System.out.println("\nЧАСТОТЫ:");
        if (Application.debug)
            builder.append("\nЧАСТОТЫ:\n");
        for (Map.Entry<String, double[]> e : dataMap.entrySet()) {
            System.out.printf("%10s - %s\n", e.getKey(), Arrays.toString(e.getValue()));
            if (Application.debug)
                builder.append(String.format("%10s - %s\n", e.getKey(), Arrays.toString(e.getValue())));
        }
    }

    private void normalize() {
        for (Map.Entry<String, double[]> row : dataMap.entrySet()) {
            double[] items = row.getValue();
            double min = Arrays.stream(items).min().getAsDouble();
            double max = Arrays.stream(items).max().getAsDouble();
            for (int i = 0; i < items.length; i++) {
                items[i] = (items[i] - min) / (max - min);
                if (Double.isNaN(items[i])) {
                    items[i] = 0;
                }
            }
            dataMap.put(row.getKey(), items);
        }

        System.out.println("\nНОРМАЛИЗОВАННЫЕ ДАННЫЕ:");
        if (Application.debug)
            builder.append("\nНОРМАЛИЗОВАННЫЕ ДАННЫЕ:\n");
        for (Map.Entry<String, double[]> e : dataMap.entrySet()) {
            System.out.printf("%10s - %s\n", e.getKey(), Arrays.toString(e.getValue()));
            if (Application.debug)
                builder.append(String.format("%10s - %s\n", e.getKey(), Arrays.toString(e.getValue())));
        }
    }

    public String getBuilder() {
        return builder.toString();
    }
}
