package diplom.clustering;

import diplom.nlp.FilteredUnigram;
import diplom.utils.Reader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Document {

    private Vocabulary vocabulary;
    private List<String> terms;
    private Map<Integer, Integer> termsFrequency;
    private int termWithMaxFrequency;

    public Document(File doc, Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
        this.termsFrequency = new TreeMap<>();
        this.termWithMaxFrequency = 0;

        try {
            String text = Reader.readFile(doc);
            this.terms = FilteredUnigram.get(text);
            vocabulary.addAll(terms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calcTermsFrequency() {
        for (String term : vocabulary.keySet()) {
            termsFrequency.put(vocabulary.get(term), 0);
        }

        int termID;
        for (String term : terms) {
            termID = vocabulary.get(term);
            Integer termsFreq = termsFrequency.get(termID);
            termsFrequency.put(termID, termsFreq + 1);

            if (termsFrequency.get(termWithMaxFrequency) < termsFreq) {
                termWithMaxFrequency = termID;
            }
        }
    }

    public int termsCount() {
        return terms.size();
    }

    public Map<Integer, Integer> termsFrequencyMap() {
        return termsFrequency;
    }

    public boolean containsWord(int termID) {
        return termsFrequency.get(termID) > 0;
    }

    public String getTheHardestWord() {
        return terms.get(termsFrequency.get(termWithMaxFrequency));
    }

}
