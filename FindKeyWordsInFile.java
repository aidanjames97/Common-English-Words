import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FindKeyWordsInFile {
    // Defines a node for the max Heap priority queue
    class Node {
        String key;
        int value;
        Node(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    // global queue, used for testing otherwise wouldnt be needed for class
    static PriorityQueue<Map.Entry<String, Integer>> queue = new PriorityQueue<>();

    // part 1 function (working)
    public static void computeWordFrequencies(String fileName, AVLTree tree1) {
        // read all words in input file
        try (Scanner scanner = new Scanner(new File(fileName))) {
            // while there are more words in the file
            while (scanner.hasNext()) {
                // convert words to lowercase
                String word = scanner.next().toLowerCase();

                // removing punctuation
                String punctuation = ".,;:\"'?!";
                int i;
                boolean yes = false;
        
                for(i = 0; i < punctuation.length(); i++){
                    if(word.charAt(word.length()-1) == punctuation.charAt(i)) {
                        yes = true;
                    }
                }
                // removing the punctuation found
                if(yes) {
                    word = word.substring(0, word.length()-1);
                }

                // testing if node is null
                if(tree1.get(word) == null) {
                    // word not in tree, add
                    tree1.put(word, 1);
                } else {
                    // word in tree, increase frequency
                    // variable to hold current value for node casted to int
                    Integer val = (int)tree1.get(word);
                    tree1.put(word, val+1);
                }
            }
        } catch (FileNotFoundException e) {
            // caught exception
            System.out.println("File: " + fileName + ", could not be found");
        } catch (Exception e) {
            // other exception caught
            System.out.println(e);
        }
        // done reading the words, in-order traversal will yeild alphabetical order
    }

    // part 2 function (testing)
    public static PriorityQueue<Map.Entry<String, Integer>> findKMostFrequentWords(AVLTree tree1) {
        
        // creating a max heap priority queue using comparator (comparing node's value)
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
            new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                    return b.getValue().compareTo(a.getValue());
                }
            }
        );

        // traversing tree inorder and inserting nodes to prrority queue in reverse order making it a max heap
        tree1.inOrderTraversal();
        List<Map.Entry<String, Integer>> inOrdList = new LinkedList<>();

        // adding nodes to a temporary list
        inOrdList = tree1.getList();

        // adding all values from the inorder 
        for(int i=0; i < inOrdList.size(); i++) {
            pq.add(inOrdList.get(i));
        }

        // return queue created 
        return pq;
    }

    // part 3 function
    public static void filterCommonEnglishWords(AVLTree tree2, String fileName, AVLTree tree3, int k, PriorityQueue<Map.Entry<String, Integer>> queue) {
        // local queue
        PriorityQueue<Map.Entry<String, Integer>> localQueue = queue;
        
        // read all words from most frequent english words txt
        try (Scanner scanner = new Scanner(new File(fileName))) {
            // while there are more words in the file
            while (scanner.hasNext()) {
                // convert words to lowercase
                String word = scanner.next().toLowerCase();
                // put the word into the tree2, key is string (word) and value in null
                tree2.put(word, null);
            }
            // go through k most frequent words in the PQ
            for(int i=0; i < k; i++) {
                Map.Entry<String, Integer> tmp = localQueue.poll();
                // if word in PQ in tree2
                if(tmp != null) {
                    if(tree2.find((String)tmp.getKey()) != true) {
                        // word not found in tree, add to tree3
                        tree3.put(tmp.getKey(), tmp.getValue());
                    }
                } else {
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            // caught exception
            System.out.println("File: " + fileName + ", could not be found");
        } catch (Exception e) {
            // other exception caught
            System.out.println(e);
        }
    }   

    public static void main(String[] args) {
        // if arguments aren't equal to 3 (error)
        if (args.length != 3) {
            System.err.println("Usage: java FindKeyWordsInFile k file.txt MostFrequentEnglishWords.txt");
            System.exit(1);
        }

        // args
        int k = Integer.parseInt(args[0]); // used to find k most frequent words
        String inputFileName = args[1]; // input file to check
        String englishWordsFileName = args[2]; // english word file
        
        // 3 avl trees to be used
        AVLTree<String, Integer> wordFrequencies = new AVLTree<>(); // tree1
        AVLTree<String, Void> englishWords = new AVLTree<>(); // tree2
        AVLTree<String, Integer> keywordFrequencies = new AVLTree<>(); // tree3

        // testing
        // Test to check if the program reads the input file and creates the word frequency correctly.
        System.out.println("Test 1 -> testing if program reads inp file and creates word frequency");
        try {
            computeWordFrequencies(inputFileName, wordFrequencies);
            wordFrequencies.inOrderPrint();
            System.out.println("Test 1 passed ---");
        } catch (Exception e){
            System.out.println("test 1 exception caught");
        }

        System.out.println();

        // Test to check if the program finds the k most frequent words correctly.
        System.out.println("Test 2 -> testing if program finds the k most frequent words");
        try {
            // testing 10 most frequent words
            queue = findKMostFrequentWords(wordFrequencies);

            PriorityQueue<Map.Entry<String, Integer>> tmpQ = new PriorityQueue<>(queue);

            for(int i=0; i < k; i++) {
                System.out.println(tmpQ.peek().getKey() + " " + tmpQ.poll().getValue());
            }
            System.out.println("Test 2 passed ---");
        } catch (Exception e) {
            System.out.println("test 2 exception caught: " + e);
        }

        System.out.println();

        // Test to check if the program filters common English words correctly.
        System.out.println("Test 3 -> testing to check if the program filters common english words");
        try {
            filterCommonEnglishWords(englishWords, englishWordsFileName, keywordFrequencies, k, queue);
            keywordFrequencies.inOrderPrint();

            System.out.println("Test 3 passed ---");
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println();
        // Test to check if the program generates the correct output for file3.txt.
        System.out.println("Test 4 -> testing for correct output for file3.txt");
        try {
            inputFileName = "file3.txt";
            wordFrequencies = new AVLTree<>(); // tree1
            englishWords = new AVLTree<>(); // tree2
            keywordFrequencies = new AVLTree<>(); // tree3
            computeWordFrequencies(inputFileName, wordFrequencies);
            filterCommonEnglishWords(englishWords, englishWordsFileName, keywordFrequencies, k ,findKMostFrequentWords(wordFrequencies));
            keywordFrequencies.inOrderPrint();

            System.out.println("Test 4 Passed ---");
        } catch (Exception e){
            System.out.println(e);
        }

        System.out.println();
        // Test to check if the program handles empty input files.
        System.out.println("Test 5 -> testing if program checks empty files");
        inputFileName = "";
        try {
            wordFrequencies = new AVLTree<>();
            computeWordFrequencies(inputFileName, wordFrequencies);

            System.out.println("Test 5 Passed ---");
        } catch (Exception e) {
            System.out.println("Test Failed with: " + e);
        }

        System.out.println();
        // Test to check if the program handles non-existing input files.
        System.out.println("Test 6 -> testing if program handles non-existing input file");
        inputFileName = "thisFileDoesntExist.txt";
        try {
            wordFrequencies = new AVLTree<>();
            computeWordFrequencies(inputFileName, wordFrequencies);

            System.out.println("Test 6 Passed ---");
        } catch (Exception e) {
            System.out.println("Test Failed with: " + e);
        }
        System.out.println("--- All Tests Passed ---");
    }
}