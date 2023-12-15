import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day13 {


    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of(".\\input\\day13.txt"));
        LavaIsland lavaIsland = new LavaIsland(input);
//        lavaIsland.printFields();
        System.out.println(lavaIsland.calculateAnswer());

    }

    static class LavaIsland {

        private List<IslandField> fields;

        public LavaIsland(List<String> input) {
            this.fields = new ArrayList<>();
            List<String> choppedInput = new ArrayList<>();
            for (String s : input) {
                if (!s.isEmpty())
                    choppedInput.add(s);
                else {
                    extractField(choppedInput);
                    choppedInput = new ArrayList<>();
                }
            }
            extractField(choppedInput);
        }

        private void extractField(List<String> choppedInput) {
            char[][] map = new char[choppedInput.size()][choppedInput.get(0).length()];
            for (int j = 0; j < choppedInput.size(); j++) {
                char[] choppedLine = choppedInput.get(j).toCharArray();
                System.arraycopy(choppedLine, 0, map[j], 0, choppedLine.length);
            }
            IslandField islandField = new IslandField(map);
            fields.add(islandField);
        }

        public void printFields() {

            for (IslandField field :
                    fields) {

                for (int i = 0; i < field.map.length; i++) {
                    for (int j = 0; j < field.map[i].length; j++) {
                        System.out.print(field.map[i][j]);
                    }
                    System.out.println();
                }
            }
        }

        public int calculateAnswer() {
            int sum = 0;
            for (IslandField field : fields) {
                List<Integer> reflectionIndexesHorizontally = findReflections(field, false);
                List<Integer> reflectionIndexesVertically = findReflections(field, true);

                field.printField();


                int longestH = 0;
                int longestV = 0;
                int indexH = 0;
                int indexV = 0;
                for (Integer index : reflectionIndexesVertically) {
                    int length = calculateReflectionLength(field, index, true);
                    if (length > longestV) {
                        longestV = length;
                        indexV = index;
                    }
//                    if(index + 1 > longestV) longestV = index + 1;
                }
                if (reflectionIndexesVertically.isEmpty()) {

                    for (Integer index :
                            reflectionIndexesHorizontally) {
                        int length = calculateReflectionLength(field, index, false);
                        if (length > longestH) {
                            longestH = length;
                            indexH = index;
                        }
//                    if(index + 1 > longestH) longestH = index +1;
                    }
                }

                if (longestH > longestV) {
                    sum = sum + ((indexH + 1) * 100);
                } else {
                    sum += indexV + 1;
                }

            }
            return sum;
        }

        public List<Integer> findReflections(IslandField field, boolean vertical) {

            int outer;
            int inner;
            if (vertical) {
                outer = field.map[0].length - 1;
                inner = field.map.length;
            } else {
                outer = field.map.length - 1;
                inner = field.map[0].length;
            }

            List<Integer> reflectionBeginIndexes = new ArrayList<>();
            for (int i = 0; i < outer; i++) {
                char[] row;
                char[] nextRow;
                if (vertical) {
                    row = getColumn(i, field.map);
                    nextRow = getColumn(i + 1, field.map);
                } else {
                    row = field.map[i];
                    nextRow = field.map[i + 1];
                }
                boolean reflection = true;

                for (int j = 0; j < inner; j++) {
                    if (row[j] != nextRow[j]) {
                        reflection = false;
                        break;
                    }
                }
                if (reflection) {
                    reflectionBeginIndexes.add(i);
                }
            }
            return reflectionBeginIndexes;

        }

        public int calculateReflectionLength(IslandField field, int index, boolean vertical) {
            int outer;
            if (vertical) {
                outer = field.map[0].length;
            } else {
                outer = field.map.length;
            }

            int i = index;
            int j = index + 1;
            int length = 0;

            while (i >= 0 && j < outer) {

                char[] row;
                if (vertical)
                    row = getColumn(i, field.map);
                else
                    row = field.map[i];

                char[] nextRow;
                if (vertical)
                    nextRow = getColumn(j, field.map);
                else
                    nextRow = field.map[j];

                boolean reflection = true;
                for (int y = 0; y < row.length; y++) {
                    if (row[y] != nextRow[y]) {
                        reflection = false;
                        break;
                    }
                }
                if (!reflection) {
                    return length;
                }


                length++;
                i--;
                j++;
            }
            return length;
        }

        private char[] getColumn(int index, char[][] map) {
            char[] col = new char[map.length];
            for (int i = 0; i < map.length; i++) {
                col[i] = map[i][index];
            }
            return col;
        }

    }

    static class IslandField {
        char[][] map;

        public IslandField(char[][] map) {
            this.map = map;
        }

        public void printField() {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    System.out.print(map[i][j]);
                }
                System.out.println();
            }
        }
    }


}
