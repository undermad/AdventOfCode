import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day16 {


    public static void main(String[] args) throws IOException {

        List<String> input = Files.readAllLines(Path.of(".\\input\\day16.txt"));
        Panel panel = new Panel(input);
        Beam beam = new Beam(FacingDirection.RIGHT, 0, 0);
        panel.sendBeam(beam);
        panel.printPanel();
        System.out.println("Part 1 answer: " + panel.countEnergizedChunks());


        int largestNumberOfEnergizedChunks = 0;
        for (int i = 0; i < panel.grid[0].length; i++) {
            Panel p = new Panel(input);
            Beam b = new Beam(FacingDirection.DOWN, i, 0);
            p.sendBeam(b);
            int a = p.countEnergizedChunks();
            largestNumberOfEnergizedChunks = Math.max(a, largestNumberOfEnergizedChunks);
            Panel p2 = new Panel(input);
            Beam b2 = new Beam(FacingDirection.TOP, i, 0);
            p2.sendBeam(b2);
            int a2 = p2.countEnergizedChunks();
            largestNumberOfEnergizedChunks = Math.max(a2, largestNumberOfEnergizedChunks);
        }



        for (int i = 0; i < panel.grid.length; i++) {
            Panel p = new Panel(input);
            Beam b = new Beam(FacingDirection.RIGHT, 0, i);
            p.sendBeam(b);
            int a = p.countEnergizedChunks();
            largestNumberOfEnergizedChunks = Math.max(a, largestNumberOfEnergizedChunks);

            Panel p2 = new Panel(input);
            Beam b2 = new Beam(FacingDirection.LEFT, panel.grid[0].length - 1, i);
            p2.sendBeam(b2);
            int a2 = p2.countEnergizedChunks();
            largestNumberOfEnergizedChunks = Math.max(a2, largestNumberOfEnergizedChunks);
        }

        System.out.println("Part 2 answer: " + largestNumberOfEnergizedChunks);


    }

    public static class Panel {

        PanelChunk[][] grid;


        public Panel(List<String> input) {
            this.grid = new PanelChunk[input.size()][input.get(0).length()];

            for (int row = 0; row < input.size(); row++) {
                for (int col = 0; col < input.get(row).length(); col++) {
                    this.grid[row][col] = new PanelChunk(input.get(row).charAt(col));
                }
            }

        }

        public void printPanel() {
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col].energizedWithDirection.isEmpty())
                        System.out.print(grid[row][col].type);
                    else
                        System.out.print("x");
                }
                System.out.println();
            }
        }

        public int countEnergizedChunks() {
            int numberOfEnergizedTiles = 0;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (!grid[row][col].energizedWithDirection.isEmpty())
                        numberOfEnergizedTiles++;
                }
            }
            return numberOfEnergizedTiles;
        }

        public void sendBeam(Beam beam) {
            if (!isBeamInGrid(beam)) {
                return;
            }
            if (grid[beam.y][beam.x].type == '.') {
                dealWithDot(beam);
            } else if (grid[beam.y][beam.x].type == '/') {
                dealWithSlash(beam);
            } else if (grid[beam.y][beam.x].type == '\\') {
                dealWithBackSlash(beam);
            } else if (grid[beam.y][beam.x].type == '|') {
                dealWithVerticalDash(beam);
            } else if (grid[beam.y][beam.x].type == '-') {
                dealWithDash(beam);
            }
        }

        private void dealWithDot(Beam beam) {
            if (panelChunkIsEnergizedWithBeamDirection(beam)) return;
            stepForward(beam);
            sendBeam(beam);
        }

        private void dealWithDash(Beam beam) {
            if (beam.facingDirection == FacingDirection.TOP || beam.facingDirection == FacingDirection.DOWN) {
                if (grid[beam.y][beam.x].energizedWithDirection.contains(FacingDirection.LEFT) || grid[beam.y][beam.x].energizedWithDirection.contains(FacingDirection.RIGHT))
                    return;
                else {
                    grid[beam.y][beam.x].energizedWithDirection.add(FacingDirection.RIGHT);
                    grid[beam.y][beam.x].energizedWithDirection.add(FacingDirection.LEFT);
                }
                sendBeam(new Beam(FacingDirection.RIGHT, beam.x + 1, beam.y));
                sendBeam(new Beam(FacingDirection.LEFT, beam.x - 1, beam.y));
            } else {
                if (panelChunkIsEnergizedWithBeamDirection(beam)) return;
                stepForward(beam);
                sendBeam(beam);
            }
        }

        private void dealWithVerticalDash(Beam beam) {
            if (beam.facingDirection == FacingDirection.LEFT || beam.facingDirection == FacingDirection.RIGHT) {
                if (grid[beam.y][beam.x].energizedWithDirection.contains(FacingDirection.TOP) || grid[beam.y][beam.x].energizedWithDirection.contains(FacingDirection.DOWN))
                    return;
                else {
                    grid[beam.y][beam.x].energizedWithDirection.add(FacingDirection.TOP);
                    grid[beam.y][beam.x].energizedWithDirection.add(FacingDirection.DOWN);

                }
                sendBeam(new Beam(FacingDirection.TOP, beam.x, beam.y - 1));
                sendBeam(new Beam(FacingDirection.DOWN, beam.x, beam.y + 1));
            } else {
                if (panelChunkIsEnergizedWithBeamDirection(beam)) return;
                stepForward(beam);
                sendBeam(beam);
            }
        }

        private void dealWithSlash(Beam beam) {
            if (beam.facingDirection == FacingDirection.RIGHT) {
                beam.facingDirection = FacingDirection.TOP;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.DOWN) {
                beam.facingDirection = FacingDirection.LEFT;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.LEFT) {
                beam.facingDirection = FacingDirection.DOWN;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.TOP) {
                beam.facingDirection = FacingDirection.RIGHT;
                dealWithDot(beam);
            }
        }

        private void dealWithBackSlash(Beam beam) {
            if (beam.facingDirection == FacingDirection.RIGHT) {
                beam.facingDirection = FacingDirection.DOWN;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.DOWN) {
                beam.facingDirection = FacingDirection.RIGHT;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.LEFT) {
                beam.facingDirection = FacingDirection.TOP;
                dealWithDot(beam);
            } else if (beam.facingDirection == FacingDirection.TOP) {
                beam.facingDirection = FacingDirection.LEFT;
                dealWithDot(beam);
            }
        }


        private boolean panelChunkIsEnergizedWithBeamDirection(Beam beam) {
            if (grid[beam.y][beam.x].energizedWithDirection.contains(beam.facingDirection)) {
                return true;
            } else {
                grid[beam.y][beam.x].energizedWithDirection.add(beam.facingDirection);
            }
            return false;
        }

        private void stepForward(Beam beam) {
            if (beam.facingDirection == FacingDirection.RIGHT) {
                beam.x++;
            } else if (beam.facingDirection == FacingDirection.DOWN) {
                beam.y++;
            } else if (beam.facingDirection == FacingDirection.LEFT) {
                beam.x--;
            } else if (beam.facingDirection == FacingDirection.TOP) {
                beam.y--;
            }
        }

        private boolean isBeamInGrid(Beam beam) {
            return beam.x >= 0 && beam.y >= 0 && beam.x < grid.length && beam.y < grid[0].length;
        }


    }

    public static class PanelChunk {
        public char type;
        public Set<FacingDirection> energizedWithDirection = new HashSet<>();

        public PanelChunk(char type) {
            this.type = type;
        }
    }


    public static class Beam {
        public FacingDirection facingDirection;
        public int x;
        public int y;

        public Beam(FacingDirection facingDirection, int x, int y) {
            this.facingDirection = facingDirection;
            this.x = x;
            this.y = y;
        }


    }

    public enum FacingDirection {
        RIGHT,
        LEFT,
        TOP,
        DOWN
    }


}
