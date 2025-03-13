package BTTH1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class bai4 {
    private Map<String, Integer> wordCount; // Đếm số lần xuất hiện của từng từ
    private Map<String, Map<String, Integer>> bigramCount; // Đếm số lần xuất hiện của từng cặp từ
    private Set<String> vocabulary; // Tập từ vựng (xuất hiện ít nhất 5 lần)
    private int totalWords; // Tổng số từ
    private int totalBigrams; // Tổng số cặp từ
    
    public bai4() {
        wordCount = new HashMap<>();
        bigramCount = new HashMap<>();
        vocabulary = new HashSet<>();
        totalWords = 0;
        totalBigrams = 0;
    }
    
    // Phương thức đọc và xử lý dữ liệu từ tập UIT-ViOCD
    public void processCorpus(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            
            while ((line = reader.readLine()) != null) {
                // Tách câu thành các từ
                String[] words = line.trim().split("\\s+");
                
                for (String word : words) {
                    // Bỏ qua từ rỗng
                    if (word.isEmpty()) continue;
                    
                    // Cập nhật số lần xuất hiện của từ
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    totalWords++;
                    
                    // Cập nhật số lần xuất hiện của cặp từ
                    if (previousWord != null) {
                        if (!bigramCount.containsKey(previousWord)) {
                            bigramCount.put(previousWord, new HashMap<>());
                        }
                        Map<String, Integer> nextWords = bigramCount.get(previousWord);
                        nextWords.put(word, nextWords.getOrDefault(word, 0) + 1);
                        totalBigrams++;
                    }
                    
                    previousWord = word;
                }
                
                // Reset previousWord ở cuối mỗi câu
                previousWord = null;
            }
            
            // Xây dựng từ điển chỉ với từ xuất hiện ít nhất 5 lần
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                if (entry.getValue() >= 5) {
                    vocabulary.add(entry.getKey());
                }
            }
            
            System.out.println("Đã xử lý xong corpus với " + vocabulary.size() + " từ trong từ điển.");
            
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc tập dữ liệu: " + e.getMessage());
        }
    }
    
    // Tính xác suất P(word)
    private double wordProbability(String word) {
        if (!vocabulary.contains(word)) return 0.0;
        return (double) wordCount.getOrDefault(word, 0) / totalWords;
    }
    
    // Tính xác suất P(word1, word2) - xác suất đồng thời
    private double jointProbability(String word1, String word2) {
        if (!vocabulary.contains(word1) || !vocabulary.contains(word2)) return 0.0;
        
        Map<String, Integer> nextWords = bigramCount.getOrDefault(word1, Collections.emptyMap());
        int count = nextWords.getOrDefault(word2, 0);
        
        return (double) count / totalBigrams;
    }
    
    // Tính xác suất có điều kiện P(word2|word1)
    private double conditionalProbability(String word2, String word1) {
        double pw1 = wordProbability(word1);
        if (pw1 == 0) return 0.0;
        
        double jointP = jointProbability(word1, word2);
        return jointP / pw1;
    }
    
    // Sinh ra câu với tối đa 5 từ
    public List<String> generateSentence(String startWord) {
        List<String> sentence = new ArrayList<>();
        sentence.add(startWord);
        
        String currentWord = startWord;
        
        // Sinh thêm tối đa 4 từ nữa
        for (int i = 0; i < 4; i++) {
            String nextWord = findBestNextWord(currentWord);
            if (nextWord == null) break; // Không tìm thấy từ tiếp theo phù hợp
            
            sentence.add(nextWord);
            currentWord = nextWord;
        }
        
        return sentence;
    }
    
    // Tìm từ tiếp theo tốt nhất dựa vào xác suất có điều kiện
    private String findBestNextWord(String currentWord) {
        String bestWord = null;
        double highestProb = 0.0;
        
        Map<String, Integer> nextWordsCount = bigramCount.getOrDefault(currentWord, Collections.emptyMap());
        
        for (String candidate : nextWordsCount.keySet()) {
            if (!vocabulary.contains(candidate)) continue;
            
            double prob = conditionalProbability(candidate, currentWord);
            if (prob > highestProb) {
                highestProb = prob;
                bestWord = candidate;
            }
        }
        
        return bestWord;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        bai4 model = new bai4();
        
        // Sử dụng đường dẫn cố định đến tập dữ liệu UIT-ViOCD
        String datasetPath = "D:\\Uni\\HK2 Năm 3\\Java\\BTTH1\\UIT-ViOCD.txt";
        
        System.out.println("Đang xử lý tập dữ liệu...");
        model.processCorpus(datasetPath);
        
        System.out.print("Nhập từ bắt đầu: ");
        String startWord = scanner.nextLine();
        
        List<String> generatedSentence = model.generateSentence(startWord);
        
        System.out.println("\nCâu được sinh ra:");
        System.out.println(String.join(" ", generatedSentence));
        
        scanner.close();
    }
}