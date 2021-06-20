package diplom.clustering;

import diplom.Application;
import diplom.ExperimentsInterface;
import diplom.distance.Distance;
import diplom.utils.Copy;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static diplom.utils.Copy.SEPARATOR;

public class Clustering implements ExperimentsInterface {

    private double separateValue;
    private Distance distance;

    private Vocabulary vocabulary;
    private List<Document> documents;
    private List<String> docNames;

    private double[][] similarityMatrix;

    private StringBuilder builder;

    private double initTime;
    private double workTime;

    public Clustering(double separateValue, Distance distance) {
        initTime = System.nanoTime();

        this.separateValue = separateValue;
        this.distance = distance;
        this.builder = new StringBuilder();

        this.vocabulary = new Vocabulary();
        this.docNames = new ArrayList<>();
        this.documents = new ArrayList<>();

        initTime = (System.nanoTime() - initTime) / 1000000;
        System.out.printf("ВРЕМЯ ИНИЦИАЛИЗАЦИИ (cluster): %.2f мс\n", initTime);
    }

    public Map<String, List<String>> run(File[] files) throws Exception {
        workTime = System.nanoTime();

        builder.setLength(0);

        if (files == null || files.length == 0) {
            throw new Exception("docs == null || docs.length == 0");
        }

        for (File file : files) {
            documents.add(new Document(file, vocabulary));
            docNames.add(file.getName());
        }

        System.out.println("vocabulary: " + vocabulary);
        if (Application.debug)
            builder.append("vocabulary: ").append(vocabulary);

        this.similarityMatrix = new double[files.length][files.length];
        documents.forEach(Document::calcTermsFrequency);

        initSimMatrix();

        if (Application.debug)
            builder.append(docNames).append("\n");
        System.out.println(docNames + "\n");

        System.out.println("\nМАТРИЦА СХОЖЕСТИ ДОКУМЕНТОВ:");
        if (Application.debug)
            builder.append("\nМАТРИЦА СХОЖЕСТИ ДОКУМЕНТОВ:\n");

        for (double[] matrix : similarityMatrix) {
            System.out.printf("%s\n", Arrays.toString(matrix));
            if (Application.debug)
                builder.append(String.format("%s\n", Arrays.toString(matrix)));
        }

        System.out.println();
        if (Application.debug)
            builder.append("\n");

        normalize();

        List<Prim.Edge> edges = Prim.solve(similarityMatrix);

        if (Application.debug) {
            builder.append("\n");
            edges.forEach(e -> builder.append(e.toString()).append("\n"));
            builder.append("\n");
        }

        System.out.println();
        edges.forEach(System.out::println);
        System.out.println();

        List<List<Integer>> clusters = new ArrayList<>();
        clusters.add(new ArrayList<>(Collections.singletonList(edges.get(0).s)));

        for (Prim.Edge edge : edges) {
            if (edge.weigh > separateValue) {
                clusters.add(new ArrayList<>(Collections.singletonList(edge.t)));
            } else {
                int clusterID = relevantCluster(clusters, edge);
                clusters.get(clusterID).add(edge.t);
            }
        }

//        List<List<String>> collect = clusters.stream()
//                .map(docIDs -> docIDs.stream()
//                        .map(docID -> docNames.get(docID))
//                        .collect(Collectors.toList()))
//                .collect(Collectors.toList());

        Map<String, List<String>> collect = clusters.stream()
                .map(docIDs -> docIDs.stream()
                        .map(docID -> docNames.get(docID))
                        .collect(Collectors.toList()))
                .collect(Collectors.toMap(
                        this::getDocsKeyWords,
                        i -> i
                ));

        int i = 1;
        for (Map.Entry<String, List<String>> entry : collect.entrySet()) {
//            builder.append(String.format("Группа №%d: %s\n", i++, entry.getKey()));
            builder.append(String.format("Группа №%d:\n", i++));
            builder.append(String.format("Файлы: %s\n\n", entry.getValue()));
//            builder.append(String.format("Файлы, принадлежащие группе №%d:\n%s\n\n", i++, entry.getValue()));
        }

        String dir = "sets" + SEPARATOR + "clustering";
        Copy.group(dir, files, collect, true);

        workTime = (System.nanoTime() - workTime) / 1000000;
        System.out.printf("ВРЕМЯ РАБОТЫ (cluster): %.2f мс\n", workTime);
        if (Application.time)
            builder.append("ВРЕМЯ РАБОТЫ: ").append(String.format("%.2f мс", workTime));

        return collect;
    }

    public String getBuilder() {
        return builder.toString();
    }

    private int relevantCluster(List<List<Integer>> clusters, Prim.Edge edge) {
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).contains(edge.s))
                return i;
        }
        return -1;
    }

    private void initSimMatrix() {
        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < documents.size(); j++) {
                similarityMatrix[i][j] = 1 - distance.calc(tfidf(documents.get(i)), tfidf(documents.get(j)));
            }
        }
    }

    private double[] tfidf(Document doc) {
        double[] tf = tf(doc);
        double[] idf = idf(doc);
        double[] tfidf = new double[vocabulary.size()];

        for (int i = 0; i < vocabulary.size(); i++) {
            tfidf[i] = tf[i] * idf[i];
        }

        return tfidf;
    }

    private double[] tf(Document doc) {
        double[] tf = new double[vocabulary.size()];
        double termsCount = doc.termsCount();
        doc.termsFrequencyMap().forEach((k, v) -> tf[k] = v / termsCount);
        return tf;
    }

    private double[] idf(Document doc) {
        double[] idf = new double[vocabulary.size()];
        doc.termsFrequencyMap().forEach((k, v) -> idf[k] = Math.log(documents.size() / (double) docsWithWord(k)));
        return idf;
    }

    private int docsWithWord(int termID) {
//        int count = 0;
//        for (Document doc : documents) {
//            if (doc.containsWord(termID))
//                count++;
//        }
//        return count;
        return (int) documents.stream()
                .filter(d -> d.containsWord(termID))
                .count();
    }

    private String getDocsKeyWords(List<String> docsName) {
        return docsName.stream()
                .limit(3)
                .map(name -> documents.get(docNames.indexOf(name))
                        .getTheHardestWord())
                .distinct()
                .collect(Collectors.joining(", "));
    }

    public double getInitTime() {
        return initTime;
    }

    public double getWorkTime() {
        return workTime;
    }

    private void normalize() {
        for (int i = 0; i < similarityMatrix.length; i++) {
            double min = Arrays.stream(similarityMatrix[i]).min().getAsDouble();
            double max = Arrays.stream(similarityMatrix[i]).max().getAsDouble();

            for (int j = 0; j < similarityMatrix[i].length; j++) {
                similarityMatrix[i][j] = (similarityMatrix[i][j] - min) / (max - min);
                if (Double.isNaN(similarityMatrix[i][j])) {
                    similarityMatrix[i][j] = 0;
                }
            }
        }

        System.out.println("НОРМАЛИЗОВАННЫЕ ДАННЫЕ:");
        if (Application.debug)
            builder.append("НОРМАЛИЗОВАННЫЕ ДАННЫЕ:\n");
        for (double[] matrix : similarityMatrix) {
            System.out.printf("%s\n", Arrays.toString(matrix));
            if (Application.debug)
                builder.append(String.format("%s\n", Arrays.toString(matrix)));
        }

        System.out.println();
        if (Application.debug)
            builder.append("\n");
    }
}
