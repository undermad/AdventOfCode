import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day11 {
    public static void main(String[] args) throws IOException {

        List<String> input = Files.readAllLines(Path.of(".\\input\\day11.txt"));
        Space space = new Space(input);
        space.printSpace();
        space.adjustGalaxiesPositions();
        space.galaxies.forEach(glaxy -> System.out.println(glaxy.x + " " + glaxy.y));

        System.out.println(space.calculateAnswer());

    }

    static class Space {

        SpaceChunk[][] spaceMap;
        List<SpaceChunk> galaxies;


        public Space(List<String> input) {
            this.galaxies = new ArrayList<>();
            this.spaceMap = new SpaceChunk[input.size()][input.get(0).length()];

            for (int row = 0; row < input.size(); row++) {
                char[] line = input.get(row).toCharArray();
                for (int col = 0; col < input.get(row).length(); col++) {
                    if (line[col] == '#') {
                        SpaceChunk galaxy = new SpaceChunk(row, col, true);
                        spaceMap[row][col] = galaxy;
                        galaxies.add(galaxy);
                    } else spaceMap[row][col] = new SpaceChunk(row, col, false);
                }
            }
        }

        private long calculateAnswer() {

            long result = 0;

            for (int i = 0; i < galaxies.size() - 1; i++) {
                for (int j = i + 1; j < galaxies.size(); j++) {
                    SpaceChunk start = galaxies.get(i);
                    SpaceChunk destination = galaxies.get(j);

                    long distanceX = Math.abs(start.x - destination.x);
                    long distanceY = Math.abs(start.y - destination.y);

                    result += distanceY + distanceX;
                }
            }
            return result;
        }


        public void adjustGalaxiesPositions() {
            adjustGalaxiesRowPositions();
            adjustGalaxiesColPositions();
        }

        private void adjustGalaxiesColPositions() {
            for (int col = 0; col < spaceMap[0].length; col++) {
                boolean hasGalaxy = false;
                for (int row = 0; row < spaceMap.length; row++) {
                    if (spaceMap[row][col].hasGalaxy) {
                        hasGalaxy = true;
                        break;
                    }
                }
                if (!hasGalaxy) adjustGalaxiesAfterColumn(col);
            }
        }

        private void adjustGalaxiesRowPositions() {
            for (int row = 0; row < spaceMap.length; row++) {
                boolean hasGalaxy = false;
                for (int col = 0; col < spaceMap[row].length; col++) {
                    if (spaceMap[row][col].hasGalaxy) {
                        hasGalaxy = true;
                        break;
                    }
                }
                if (!hasGalaxy) adjustGalaxiesAfterRow(row);
            }
        }

        private void adjustGalaxiesAfterColumn(int col) {
            for (int c = col + 1; c < spaceMap[0].length; c++) {
                for (int r = 0; r < spaceMap.length; r++) {
                    spaceMap[r][c].x = spaceMap[r][c].x + 1000000 - 1;
                }
            }
        }

        private void adjustGalaxiesAfterRow(int row) {
            for (int r = row + 1; r < spaceMap.length; r++) {
                for (int c = 0; c < spaceMap[r].length; c++) {
                    spaceMap[r][c].y = spaceMap[r][c].y + 1000000 - 1;
                }
            }
        }


        public void printSpace() {
            for (int i = 0; i < spaceMap.length; i++) {
                for (int j = 0; j < spaceMap[i].length; j++) {
                    if (spaceMap[i][j].hasGalaxy) System.out.print("#");
                    else System.out.print(".");
                }
                System.out.println();
            }
        }
    }

    static class SpaceChunk {
        long x;
        long y;
        boolean hasGalaxy;


        public SpaceChunk(long y, long x, boolean hasGalaxy) {
            this.x = x;
            this.y = y;
            this.hasGalaxy = hasGalaxy;
        }
    }
}
