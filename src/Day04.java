import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day04 {

    public static void main(String[] args) {


        try (FileReader fr = new FileReader(".\\input\\day04.txt");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            int resultPartOne = 0;
            int resultPartTwo = 0;

            Map<Integer, Integer> cardToCopies = fillCardToCopiesMap();


            while (line != null) {

                List<Integer> winningNumbers = List.of(extractWinningNumbers(line));
                List<Integer> myNumbers = List.of(extractMyNumbers(line));

                // part 1
                int pointsPart1 = extractPointFromScratchCard(winningNumbers, myNumbers);
                resultPartOne += pointsPart1;

                // part 2
                int cardNumber = extractCardNumber(line);
                int matchNumber = extractMatchNumberFromScratchCard(winningNumbers, myNumbers);
                int numberOfCopies = cardToCopies.get(cardNumber);

                for (int i = 0; i < numberOfCopies; i++) {
                    addCopies(cardNumber, matchNumber, cardToCopies);
                }

                line = br.readLine();
            }

            for (Integer key :
                    cardToCopies.keySet()) {
                resultPartTwo += cardToCopies.get(key);
            }

            System.out.println("Part one result: " + resultPartOne);
            System.out.println("Part two result: " + resultPartTwo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static void addCopies(int cardNumber, int matchNumber, Map<Integer, Integer> cardToCopies) {
        if (matchNumber != 0) {
            for (int i = 1; i <= matchNumber; i++) {
                int numberOfCopies = cardToCopies.get(cardNumber + i);
                cardToCopies.put(cardNumber + i, numberOfCopies + 1);
            }
        }
    }

    private static Map<Integer, Integer> fillCardToCopiesMap() {

        Map<Integer, Integer> cardToCopies = new HashMap<>();

        try (FileReader fr = new FileReader(".\\input\\day04.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();

            while (line != null) {
                int cardNumber = extractCardNumber(line);
                cardToCopies.put(cardNumber, 1);
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return cardToCopies;
    }

    private static int extractCardNumber(String line) {
        return Integer.parseInt(line.split(":")[0]
                .replaceAll("\\s{2,}", " ")
                .split(" ")[1]);
    }

    private static int extractMatchNumberFromScratchCard(List<Integer> winningNumbers, List<Integer> myNumbers) {

        int points = 0;
        for (Integer winningNumber :
                winningNumbers) {
            if (myNumbers.contains(winningNumber)) {
                points++;
            }
        }
        return points;
    }

    private static int extractPointFromScratchCard(List<Integer> winningNumbers, List<Integer> myNumbers) {
        int points = 0;
        boolean streak = false;

        for (Integer winningNumber :
                winningNumbers) {
            if (myNumbers.contains(winningNumber)) {
                if (!streak) {
                    points++;
                    streak = true;
                } else {
                    points *= 2;
                }
            }
        }
        return points;
    }

    private static Integer[] extractWinningNumbers(String line) {
        String[] choppedLine = line.split("\\|")[0]
                .split(":")[1]
                .strip()
                .replaceAll("\\s{2,}", " ")
                .split(" ");

        Integer[] winningNumbers = new Integer[choppedLine.length];
        for (int i = 0; i < winningNumbers.length; i++) {
            winningNumbers[i] = Integer.parseInt(choppedLine[i]);
        }
        return winningNumbers;
    }

    private static Integer[] extractMyNumbers(String line) {
        String[] choppedLine = line.split("\\|")[1]
                .strip()
                .replaceAll("\\s{2,}", " ")
                .split(" ");

        Integer[] myNumbers = new Integer[choppedLine.length];
        for (int i = 0; i < myNumbers.length; i++) {
            myNumbers[i] = Integer.parseInt(choppedLine[i]);
        }
        return myNumbers;


    }


}
