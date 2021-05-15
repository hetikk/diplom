package diplom.clustering;

import diplom.Application;
import diplom.distance.Distance;
import diplom.utils.Copy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static diplom.utils.Copy.SEPARATOR;

public class Clustering {

    private double separateValue;
    private Distance distance;

    private Vocabulary vocabulary;
    private List<Document> documents;
    private List<String> docNames;

    private double[][] similarityMatrix;

    private StringBuilder builder;

    public Clustering(double separateValue, Distance distance) {
        this.separateValue = separateValue;
        this.distance = distance;
        this.builder = new StringBuilder();

        this.vocabulary = new Vocabulary();
        this.docNames = new ArrayList<>();
        this.documents = new ArrayList<>();
    }

    public Map<String, List<String>> cluster(File[] files) throws Exception {
        builder.setLength(0);

        if (files == null || files.length == 0) {
            throw new Exception("docs == null || docs.length == 0");
        }

        for (File file : files) {
            documents.add(new Document(file, vocabulary));
            docNames.add(file.getName());
        }

        this.similarityMatrix = new double[files.length][files.length];
        documents.forEach(Document::calcTermsFrequency);

        initSimMatrix();

        List<Prim.Edge> edges = Prim.solve(similarityMatrix);

        if (Application.debug)
            builder.append(docNames).append("\n");
        System.out.println(docNames + "\n");

        for (double[] matrix : similarityMatrix) {
            for (int j = 0; j < similarityMatrix.length; j++) {
                System.out.printf("%.2f  ", matrix[j]);
                if (Application.debug)
                    builder.append(String.format("%.2f  ", matrix[j]));
            }
            if (Application.debug)
                builder.append("\n");
            System.out.println();
        }

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
            if (edge.weigh >= separateValue) {
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
            builder.append(String.format("Группа №%d: %s\n", i++, entry.getKey()));
            builder.append(String.format("Файлы: %s\n\n", entry.getValue()));
//            builder.append(String.format("Файлы, принадлежащие группе №%d:\n%s\n\n", i++, entry.getValue()));
        }

        String dir = "sets" + SEPARATOR + "clustering";
        Copy.group(dir, files, collect, true);

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

}
