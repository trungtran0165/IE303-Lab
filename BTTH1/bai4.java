import java.util.*;
import java.io.*;

public class bai4 {

    public static Map<String, Integer> vocab = new HashMap<String, Integer>();
    public static Map<String, Integer> corpus = new HashMap<String, Integer>();
    public static Map<String, Integer> pairCorpus = new HashMap<String, Integer>();
    public static Double[] probs;
    public static Double[][] conditionalProbs;

    public static void readFile() {
        try {
            Vector<String> lines = new Vector<String>();
            File file = new File("UIT-ViOCD.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                lines.addElement(line);
            }
            fileScanner.close();

            for (String line : lines) {
                // remove line break \n, \r and tab \t
                line = line.replace("\n", "").replace("\r", "").replace("\t", "");
                // remove all leading spaces
                line = line.replaceAll("^\\s+", "");
                // remove all ending spaces
                line = line.replaceAll("\\s+$", "");
                // lowering
                line = line.toLowerCase();

                // collecting words
                int wordId = 0;
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (corpus.containsKey(word)) {
                        corpus.put(word, corpus.get(word) + 1);
                    } else {
                        vocab.put(word, wordId);
                        corpus.put(word, 1);
                        wordId += 1;
                    }
                }

                // collecting pairs of words
                for (int i = 0; i < words.length - 1; i++) {
                    String words_ij = words[i] + "_" + words[i + 1];
                    if (pairCorpus.containsKey(words_ij)) {
                        pairCorpus.put(words_ij, pairCorpus.get(words_ij) + 1);
                    } else {
                        pairCorpus.put(words_ij, 1);
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found!");
            fileNotFoundException.printStackTrace();
        }
    }

    public static void constructSingleProb() {
        // determine the total number of words in the dataset
        int totalWords = 0;
        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            totalWords += item.getValue();
        }

        // calculating the probability of each word
        probs = new Double[vocab.size()];
        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            String word = item.getKey();
            Integer wordCount = corpus.get(word);
            Integer wordId = vocab.get(word);

            // Bước 2: Xác định P(w) ≈ #w / tổng số từ
            probs[wordId] = (double) wordCount / totalWords;
        }
    }

    public static void constructConditionalProb() {
        // determine the total number of words in the dataset
        int totalPairsOfWords = 0;
        for (Map.Entry<String, Integer> item : pairCorpus.entrySet()) {
            totalPairsOfWords += item.getValue();
        }

        // calculating the probability of each pair of words
        Double[][] jointProbs = new Double[vocab.size()][vocab.size()];
        for (Map.Entry<String, Integer> item_i : corpus.entrySet())
            for (Map.Entry<String, Integer> item_j : corpus.entrySet()) {
                // get the information of word i
                String word_i = item_i.getKey();
                Integer wordId_i = vocab.get(word_i);

                // get the information of word j
                String word_j = item_j.getKey();
                Integer wordId_j = vocab.get(word_j);

                if (word_i == word_j) {
                    jointProbs[wordId_i][wordId_j] = 1e-20;
                    continue;
                }

                String wordKey_ij = word_i + "_" + word_j;
                if (pairCorpus.containsKey(wordKey_ij)) {
                    Integer wordCount_ij = pairCorpus.get(wordKey_ij);
                    jointProbs[wordId_i][wordId_j] = ((double) wordCount_ij / totalPairsOfWords);
                } else {
                    jointProbs[wordId_i][wordId_j] = 1e-20;
                }

                String wordKey_ji = word_j + "_" + word_i;
                if (pairCorpus.containsKey(wordKey_ji)) {
                    Integer wordCount_ji = pairCorpus.get(wordKey_ji);
                    jointProbs[wordId_j][wordId_i] = ((double) wordCount_ji / totalPairsOfWords);
                } else {
                    jointProbs[wordId_j][wordId_i] = 1e-20;
                }
            }

        // calculating the conditional probability of each pair of words
        conditionalProbs = new Double[vocab.size()][vocab.size()];
        for (Map.Entry<String, Integer> item_i : corpus.entrySet())
            for (Map.Entry<String, Integer> item_j : corpus.entrySet()) {
                // get the information of word i
                String word_i = item_i.getKey();
                Integer wordId_i = vocab.get(word_i);

                // get the information of word j
                String word_j = item_j.getKey();
                Integer wordId_j = vocab.get(word_j);

                // Bước 4: Xác định P(w_i | w_j) = P(w_i, w_j) / P(w_j)
                conditionalProbs[wordId_i][wordId_j] = jointProbs[wordId_i][wordId_j] / probs[wordId_j];

                // Xác định P(w_j | w_i) = P(w_j, w_i) / P(w_i)
                conditionalProbs[wordId_j][wordId_i] = jointProbs[wordId_j][wordId_i] / probs[wordId_i];
            }
    }

    public static void training() {
        constructSingleProb();
        constructConditionalProb();
    }

    public static Vector<String> inferring(String w0) {
        Vector<String> words = new Vector<String>();
        words.add(w0);

        // Kiểm tra từ có tồn tại trong từ điển không
        Integer w0Idx = vocab.get(w0);
        if (w0Idx == null) {
            System.out.println("Error: Từ '" + w0 + "' không tồn tại trong từ điển!");
            // Lấy một từ ngẫu nhiên từ từ điển
            w0 = vocab.keySet().iterator().next();
            w0Idx = vocab.get(w0);
            System.out.println("Sử dụng từ thay thế: " + w0);
            words.set(0, w0);
        }

        Double logProbs = -Math.log(probs[w0Idx]);
        for (int t = 1; t <= 5; t++) {
            // determine the word that gives the highest probability P(w0, w1, ..., wt)
            String maxWord = new String();
            Double maxLogProb = 0.0;
            Integer w1Idx = new Integer(0);
            for (Map.Entry<String, Integer> item : vocab.entrySet()) {
                w1Idx = item.getValue();
                Double prob = conditionalProbs[w0Idx][w1Idx];
                if ((-Math.log(prob)) < maxLogProb || maxLogProb == 0.0) {
                    maxWord = item.getKey();
                    maxLogProb = -Math.log(prob);
                }
            }
            logProbs += maxLogProb;
            words.add(maxWord);
            w0Idx = w1Idx;
        }

        return words;
    }

    public static void main(String[] args) throws Exception {
        readFile();
        training();
        Vector<String> predicted_words = inferring("áo");
        String sentence = String.join(" ", predicted_words);
        System.out.println(sentence);
    }
}
