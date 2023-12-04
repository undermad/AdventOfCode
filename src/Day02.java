import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Day02 {

    public static void main(String[] args) {


        try (FileReader fr = new FileReader(".\\input\\day02.txt");
             BufferedReader br = new BufferedReader(fr)) {


            String line = br.readLine();
            int result = 0;
            int partTwoResult = 0;

            while (line != null) {

                int gameId = extractGameId(line);
                System.out.println("GAME ID: " + gameId);

                int green = 0;
                int maxGreen = 0;
                int blue = 0;
                int maxBlue = 0;
                int red = 0;
                int maxRed = 0;
                boolean isGamePossible = true;

                List<String> gameSets = extractGameSets(line);
                for (int i = 0; i < gameSets.size(); i++) {
                    String[] elements = gameSets.get(i).strip().split(",");

                    for (int j = 0; j < elements.length; j++) {
                        System.out.println("____________");
                        System.out.println("SET NO: " + j);
                        String cube = elements[j].strip();
                        green = 0;
                        red = 0;
                        blue = 0;
                        if (cube.contains("green")) {
                            green = getIntFromCube(cube);
                            System.out.println("GREEN: " + green);
                        } else if (cube.contains("red")) {
                            red = getIntFromCube(cube);
                            System.out.println("RED: " + red);
                        } else if (cube.contains("blue")) {
                            blue = getIntFromCube(cube);
                            System.out.println("BLUE: " + blue);
                        }

                        if (red > maxRed) maxRed = red;
                        if (blue > maxBlue) maxBlue = blue;
                        if (green > maxGreen) maxGreen = green;


                        if (!validateGame(green, red, blue)) {
                            System.out.println("GAME WITH ID: " + gameId + " IS NOT POSSIBLE DUE TO THE SET NO: " + j);
                            isGamePossible = false;
                        }
                    }


                }

                if (isGamePossible) {
                    result += gameId;
                }
                partTwoResult += maxRed * maxBlue * maxGreen;

                System.out.println("******************");
                System.out.println("******************");
                System.out.println();
                line = br.readLine();
            }
            System.out.println(result);
            System.out.println(partTwoResult);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static boolean validateGame(int green, int red, int blue) {
        if (green > 13 || red > 12 || blue > 14) {
            return false;
        }
        return true;
    }

    private static int getIntFromCube(String cube) {
        return Integer.parseInt(cube.substring(0, 2).strip());
    }

    private static List<String> extractGameSets(String line) {
        List<String> choppedLine = List.of(line.split(":"));
        return List.of(choppedLine.get(1).split(";"));

    }

    private static int extractGameId(String line) {
        List<String> choppedLine = List.of(line.split(":"));
        return Integer.parseInt(choppedLine.get(0).substring(5));
    }

}
