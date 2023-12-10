import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day10 {

    public static void main(String[] args) throws IOException {
        Maze maze = new Maze();
        maze.printMaze();
        System.out.println(maze.countSteps() / 2);
    }

    static class Maze {
        List<String> input = extractMaze();
        char[][] maze = new char[input.size()][input.get(0).length()];
        int startX;
        int startY;
        int currentPositionX;
        int currentPositionY;
        int facingDirection;
        int steps;


        public Maze() throws IOException {
            for (int row = 0; row < input.size(); row++) {
                char[] choppedLine = input.get(row).toCharArray();
                for (int col = 0; col < choppedLine.length; col++) {
                    this.maze[row][col] = choppedLine[col];
                    if (choppedLine[col] == 'S') {
                        this.startX = col;
                        this.startY = row;
                    }
                }
            }
            this.currentPositionX = this.startX;
            this.currentPositionY = this.startY;

            this.facingDirection = FacingDirection.EAST;
            this.steps = 0;
        }

        public int countSteps() {
            step();
            while (maze[currentPositionY][currentPositionX] != 'S') {
                step();
            }
            return steps;
        }


        public void step() {
            if (facingDirection == FacingDirection.EAST) {
                char nextPipe = maze[currentPositionY][currentPositionX + 1];
                currentPositionX++;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'J' -> Rotation.ROTATE_LEFT;
                    case '7' -> Rotation.ROTATE_RIGHT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.SOUTH) {
                char nextPipe = maze[currentPositionY + 1][currentPositionX];
                currentPositionY++;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'J' -> Rotation.ROTATE_RIGHT;
                    case 'L' -> Rotation.ROTATE_LEFT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.WEST) {
                char nextPipe = maze[currentPositionY][currentPositionX - 1];
                currentPositionX--;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'F' -> Rotation.ROTATE_LEFT;
                    case 'L' -> Rotation.ROTATE_RIGHT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.NORTH) {
                char nextPipe = maze[currentPositionY - 1][currentPositionX];
                currentPositionY--;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'F' -> Rotation.ROTATE_RIGHT;
                    case '7' -> Rotation.ROTATE_LEFT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            }

            steps++;
        }

        private void rotate(Rotation rotation) {
            if (rotation == Rotation.DONT_ROTATE) return;
            if (rotation == Rotation.ROTATE_RIGHT) {
                if (facingDirection == 4) facingDirection = 1;
                else facingDirection++;
            }
            if (rotation == Rotation.ROTATE_LEFT) {
                if (facingDirection == 1) facingDirection = 4;
                else facingDirection--;
            }
        }

        public void printMaze() {
            for (char[] row : maze) {
                for (char col : row) {
                    System.out.print(col);
                }
                System.out.println();
            }
            System.out.println("Starting position at row: " + startY + " at column: " + startX);
        }

        private List<String> extractMaze() throws IOException {
            return Files.readAllLines(Path.of(".\\input\\day10.txt"));
        }
    }

    static final class FacingDirection {
        public static final int NORTH = 1;
        public static final int EAST = 2;
        public static final int SOUTH = 3;
        public static final int WEST = 4;
    }

    enum Rotation {
        ROTATE_RIGHT,
        ROTATE_LEFT,
        DONT_ROTATE,
    }


}
