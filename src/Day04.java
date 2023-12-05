import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Day04 {

    public static void main(String[] args) {


        try (FileReader fr = new FileReader(".\\input\\day04.txt");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            int result = 0;

            while (line != null) {

                List<Integer> winningNumbers = List.of(extractWinningNumbers(line));
                List<Integer> myNumbers = List.of(extractMyNumbers(line));
                boolean streak = false;
                int points = 0;

                for (Integer winningNumber :
                        winningNumbers) {
                    if (myNumbers.contains(winningNumber)) {
                        if(!streak) {
                            points++;
                            streak = true;
                        } else {
                            points *= 2;
                        }
                    }
                }

                result += points;


                line = br.readLine();
            }

            System.out.println(result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
