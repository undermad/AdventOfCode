import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Day14 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of(".\\input\\day14.txt"));
        Platform platform = new Platform(lines);
//        System.out.println(platform.calculateWeight());
        platform.printMap();


        for (int i = 0; i < 1000000000; i++) {
            if (i % 1000000 == 0) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println(timeStamp);
                System.out.println(i);
            }
            platform.performCycle();
        }
        System.out.println(platform.calculateWeight());

    }


    public static class Platform {

        private char[][] map;

        public Platform(List<String> lines) {
            this.map = new char[lines.size()][lines.get(0).length()];

            for (int i = 0; i < map.length; i++) {
                char[] choppedLine = lines.get(i).toCharArray();
                for (int j = 0; j < choppedLine.length; j++) {
                    this.map[i][j] = choppedLine[j];
                }
            }
//            this.map = tiltPlatformNorth();
        }



        public int calculateWeight() {
            int sum = 0;
            int rowWeight = 0;
            for (int i = map.length - 1; i >= 0; i--) {
                rowWeight++;
                int n = 0;
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] == 'O') n++;
                }
                sum = sum + (rowWeight * n);
            }
            return sum;
        }

        private char[] selectColumn(int index) {
            char[] column = new char[map.length];
            for (int i = 0; i < map.length; i++) {
                column[i] = map[i][index];
            }
            return column;
        }

        private char[] tiltArray(char[] arr) {
            int j = 0;
            for (int i = 1; i < arr.length; i++) {
                while (arr[j] == 'O' || arr[j] == '#') {
                    if (j == arr.length - 1) break;
                    j++;
                    i = j + 1;
                }
                if (i > arr.length - 1) break;

                if (arr[i] == 'O') {
                    arr[j] = 'O';
                    arr[i] = '.';
                    j++;
                }
                if (arr[i] == '#') {
                    j = i;
                }
            }
            return arr;
        }

        private char[][] tiltPlatformNorth() {
            char[][] tiltedPlatform = new char[map.length][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                char[] arr = tiltArray(selectColumn(i));
                for (int row = 0; row < arr.length; row++) {
                    tiltedPlatform[row][i] = arr[row];
                }
            }
            return tiltedPlatform;
        }


        public void rotate90DegRight() {
            char[][] afterRotationMap = new char[map.length][map[0].length];
            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[0].length; col++) {
                    afterRotationMap[row][col] = map[map.length - 1 - col][row];
                }
            }
            map = afterRotationMap;
        }

        public void performCycle() {
            tiltPlatformNorth();
            rotate90DegRight();
            tiltPlatformNorth();
            rotate90DegRight();
            tiltPlatformNorth();
            rotate90DegRight();
            tiltPlatformNorth();
            rotate90DegRight();
        }


        public void printMap() {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    System.out.print(this.map[i][j]);
                }
                System.out.println();
            }
        }


    }
}
