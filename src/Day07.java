import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day07 {


    public static void main(String[] args) {


        Map<String, Integer> handToBid = extractHandToBidMap();
        List<List<String>> handTypes = initHandTypes();

        for (String hand :
                handToBid.keySet()) {
            Map<Character, Integer> handType = new HashMap<>();
            char[] figures = hand.toCharArray();
            int score = 0;
            for (int i = 0; i < figures.length; i++) {
                Integer current = handType.get(figures[i]);
                if (current != null) {
                    handType.put(figures[i], current + 1);
                    score = addScore(current, score);
                } else handType.put(figures[i], 1);
            }
            addToHandTypeGroup(hand, score, handTypes);

            handType.forEach((key, val) -> System.out.print("Key: " + key + " Val: " + val + " |||"));
            System.out.println();

        }

        Map<Character, Character> winningOrderToAlphabet = getWinningOrderToAlphabet();
        Map<Character, Character> alphabetToWinningOrder = getAlphabetToWinningOrder();
        List<String> ranking = new ArrayList<>();

        for (int i = 0; i < handTypes.size(); i++) {
            List<String> inOrder = new ArrayList<>();
            for (String hand :
                    handTypes.get(i)) {
                inOrder.add(fromHandToAlphabet(hand, winningOrderToAlphabet));
            }
            inOrder.sort((o1, o2) -> {
                if (o1.compareTo(o2) > 0) return -1;
                else if (o1.compareTo(o2) < 0) return 1;
                else return 0;
            });
            inOrder.forEach(h -> ranking.add(fromAlphabetToHand(h, alphabetToWinningOrder)));

        }

        ranking.forEach(System.out::println);

        long result = 0;
        for (int i = 0; i < ranking.size(); i++) {
            int bid = handToBid.get(ranking.get(i));
            int val = bid * (i + 1);
            result += val;
        }
        System.out.println(result);

    }

    private static List<List<String>> initHandTypes() {
        List<String> highCard = new ArrayList<>();
        List<String> onePair = new ArrayList<>();
        List<String> twoPair = new ArrayList<>();
        List<String> threeOfKind = new ArrayList<>();
        List<String> full = new ArrayList<>();
        List<String> fourOfKind = new ArrayList<>();
        List<String> fiveOfKind = new ArrayList<>();


        List<List<String>> handTypes = new ArrayList<>();
        handTypes.add(highCard);
        handTypes.add(onePair);
        handTypes.add(twoPair);
        handTypes.add(threeOfKind);
        handTypes.add(full);
        handTypes.add(fourOfKind);
        handTypes.add(fiveOfKind);
        return handTypes;
    }

    private static String fromAlphabetToHand(String h, Map<Character, Character> alphabetToWinningOrder) {
        char[] choppedLine = h.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < choppedLine.length; j++) {
            sb.append(alphabetToWinningOrder.get(choppedLine[j]));
        }
        return sb.toString();
    }

    private static Map<Character, Character> getAlphabetToWinningOrder() {
        Map<Character, Character> alphabetToWinningOrder = new HashMap<>();
        alphabetToWinningOrder.put('a', 'A');
        alphabetToWinningOrder.put('b', 'K');
        alphabetToWinningOrder.put('c', 'Q');
        alphabetToWinningOrder.put('d', 'J');
        alphabetToWinningOrder.put('e', 'T');
        alphabetToWinningOrder.put('f', '9');
        alphabetToWinningOrder.put('g', '8');
        alphabetToWinningOrder.put('h', '7');
        alphabetToWinningOrder.put('i', '6');
        alphabetToWinningOrder.put('j', '5');
        alphabetToWinningOrder.put('k', '4');
        alphabetToWinningOrder.put('l', '3');
        alphabetToWinningOrder.put('m', '2');
        return alphabetToWinningOrder;
    }

    private static Map<Character, Character> getWinningOrderToAlphabet() {
        Map<Character, Character> winningOrderToAlphabet = new HashMap<>();
        winningOrderToAlphabet.put('A', 'a');
        winningOrderToAlphabet.put('K', 'b');
        winningOrderToAlphabet.put('Q', 'c');
        winningOrderToAlphabet.put('J', 'd');
        winningOrderToAlphabet.put('T', 'e');
        winningOrderToAlphabet.put('9', 'f');
        winningOrderToAlphabet.put('8', 'g');
        winningOrderToAlphabet.put('7', 'h');
        winningOrderToAlphabet.put('6', 'i');
        winningOrderToAlphabet.put('5', 'j');
        winningOrderToAlphabet.put('4', 'k');
        winningOrderToAlphabet.put('3', 'l');
        winningOrderToAlphabet.put('2', 'm');
        return winningOrderToAlphabet;
    }

    private static String fromHandToAlphabet(String hand, Map<Character, Character> cardWinningOrder) {
        char[] choppedHand = hand.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < choppedHand.length; j++) {
            sb.append(cardWinningOrder.get(choppedHand[j]));
        }
        return sb.toString();
    }

    private static void addToHandTypeGroup(String hand, int score, List<List<String>> handTypes) {
        if (score == 0) handTypes.get(0).add(hand);
        else if (score == 5) handTypes.get(1).add(hand);
        else if (score == 10) handTypes.get(2).add(hand);
        else if (score == 25) handTypes.get(3).add(hand);
        else if (score == 60 || score == 75) handTypes.get(4).add(hand);
        else if (score == 125) handTypes.get(5).add(hand);
        else if (score > 500) handTypes.get(6).add(hand);
    }

    private static int addScore(Integer current, int score) {
        if (current == 1 && score == 25) score += 50;  // full = 75score
        else if (current == 1 && score == 5) score += 5; // two pairs = 10score
        else if (current == 1 && score == 0) score += 5; // one pair  = 5score
        else if (current == 2 && score == 10) score += 50; // full = 60score
        else if (current == 2 && score == 5) score += 20; // three of kind = 25score
        else if (current == 3) score += 100; // four of kind = 125score
        else if (current == 4) score += 500; // five of kind = 500+
        return score;
    }

    private static Map<String, Integer> extractHandToBidMap() {
        Map<String, Integer> handToBid = new HashMap<>();
        try (FileReader fr = new FileReader(".\\input\\day07.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();

            while (line != null) {
                String[] choppedLine = line.split(" ");
                handToBid.put(choppedLine[0], Integer.parseInt(choppedLine[1]));
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return handToBid;
    }
}
