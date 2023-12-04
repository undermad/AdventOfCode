import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Day03 {
    public static void main(String[] args) {


        char[][] schematic = drawSchematic();
        long result = 0;
        long gearRatioResult = 0;

        for (int row = 0; row < schematic.length; row++) {
            for (int column = 0; column < schematic[row].length; column++) {

                if (schematic[row][column] == '.' ||
                        schematic[row][column] == ',' ||
                        Character.isDigit(schematic[row][column])) {

                } else {
//                    result += checkAround(schematic, row, column);
                    if (schematic[row][column] == '*') {
                        gearRatioResult += checkGearRatio(schematic, row, column);
                    }


                }


            }
        }
        // uncomment line 22 to get result
        System.out.println(result);
        System.out.println(gearRatioResult);

    }


    private static long checkAround(char[][] schematic, int row, int column) {
        long result = 0;


        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                result += extractNumberFromSchematic(schematic, r, c);
            }
        }


        return result;
    }

    private static long checkGearRatio(char[][] schematic, int row, int column) {

        long first = 0;
        long second = 0;
        int numberOfConnections = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (numberOfConnections == 0) {
                    first = extractNumberFromSchematic(schematic, r, c);
                    if (first != 0) {
                        numberOfConnections++;
                    }
                } else if (numberOfConnections == 1) {
                    second = extractNumberFromSchematic(schematic, r, c);
                    if (second != 0) {
                        numberOfConnections++;
                    }
                }
            }
        }
        if (numberOfConnections == 2) {
            return first * second;
        }

        return 0;
    }

    private static long extractNumberFromSchematic(char[][] schematic, int row, int column) {
        StringBuilder sb = new StringBuilder();

        if (Character.isDigit(schematic[row][column])) {
            while (column >= 0 && Character.isDigit(schematic[row][column])) {
                column--;
            }
            column++;

            while (column < 140 && Character.isDigit(schematic[row][column])) {
                sb.append(schematic[row][column]);
                schematic[row][column] = ',';
                column++;
            }
            return Long.parseLong(sb.toString());
        } else {
            return 0;
        }

    }

    private static char[][] drawSchematic() {
        try (FileReader fr = new FileReader(".\\input\\day03.txt");
             BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();
            char[][] schematic = new char[140][140];
            int row = 0;
            while (line != null) {
                char[] lineAsChars = line.toCharArray();

                for (int i = 0; i < schematic[row].length; i++) {
                    schematic[row][i] = lineAsChars[i];
                    System.out.print(schematic[row][i]);
                }
                System.out.println();
                row++;
                line = br.readLine();
            }
            return schematic;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
