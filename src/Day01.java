import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day01 {
    public static void main(String[] args) {

        int lineNo = 1;

        try (FileReader fr = new FileReader(".\\day01.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();


            Map<String, Integer> digits = Map.of(
                    "one", 1,
                    "two", 2,
                    "three", 3,
                    "four", 4,
                    "five", 5,
                    "six", 6,
                    "seven", 7,
                    "eight", 8,
                    "nine", 9);

            int result = 1;
            int indexOfFirstDigit = 0;
            int indexOfLastDigit = line.length();

            while (line != null) {
                // extract the first and last digit
                char[] characters = line.toCharArray();
                String firstDigit = "";
                String lastDigit = "";
                boolean first = false;
                for (int i = 0; i < characters.length; i++) {
                    if (Character.isDigit(characters[i])) {
                        if (!first) {
                            firstDigit = String.valueOf(characters[i]);
                            indexOfFirstDigit = i;
                        } else {
                            lastDigit = String.valueOf(characters[i]);
                            indexOfLastDigit = i;
                        }
                        first = true;
                    }
                }



                if(lastDigit.isEmpty()){
                    indexOfLastDigit = indexOfFirstDigit;
                    lastDigit = firstDigit;
                }
                System.out.println("FIRST DIGIT: " + firstDigit);
                System.out.println("LAST DIGIT: "+ lastDigit);

                String prefix = line.substring(0, indexOfFirstDigit);
                String suffix = line.substring(indexOfLastDigit + 1);

                System.out.println("PREFIX: " + prefix);
                System.out.println(lineNo + ". WHOLE LINE: " + line);
                System.out.println("SUFFIX: " + suffix);

                int firstTrueDigit = extractNumber(digits, prefix, true);
                if (firstTrueDigit != -1) {
                    firstDigit = String.valueOf(firstTrueDigit);
                    System.out.println("FIRST TRUE DIGIT IS: " + firstTrueDigit);
                }

                int lastTrueDigit = extractNumber(digits, suffix, false);
                if (lastTrueDigit != -1) {
                    lastDigit = String.valueOf(lastTrueDigit);
                    System.out.println("LAST TRUE DIGIT IS : " + lastTrueDigit);
                }
                int lineAnswer = Integer.parseInt(firstDigit + lastDigit);
                result += lineAnswer;
                System.out.println("LINE ANSWER IS: " + lineAnswer);
                System.out.println("WHOLE RESULT IS: " + result);



                System.out.println("***");
                System.out.println("***");
                System.out.println("***");
                System.out.println("***");

                // jump to next line
                lineNo++;
                line = br.readLine();
            }
            System.out.println(result);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static int extractNumber(Map<String, Integer> digits, String sequence, boolean isItPrefix) {
        Map<Integer, Integer> indexes = new HashMap<>();
        for (String digitAsWorld : digits.keySet()) {
            int prefixBeginIndex = sequence.indexOf(digitAsWorld);
            if (prefixBeginIndex != -1) indexes.put(prefixBeginIndex, digits.get(digitAsWorld));
        }
        if (!indexes.isEmpty()) {
            if (isItPrefix) {
                int smallest = Integer.MAX_VALUE;
                for (Integer i : indexes.keySet()) {
                    if (i < smallest) smallest = i;
                }
                return indexes.get(smallest);
            } else {
                int largest = Integer.MIN_VALUE;
                for (Integer i : indexes.keySet()) {
                    if (i > largest) largest = i;
                }
                return indexes.get(largest);
            }
        }
        return -1;
    }
}
