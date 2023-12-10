import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day10 {

    public static void main(String[] args) throws IOException {
        Maze maze = new Maze();
        maze.createBorders();
        maze.printMaze();
        System.out.println(maze.countInnerTiles());


//        int farthest = maze.countSteps() / 2;
//        System.out.println("Part 1: " + farthest);
    }

    static class Maze {
        List<String> input = extractMaze();
        Tile[][] maze = new Tile[input.size()][input.get(0).length()];
        int startX, startY;
        int currentPositionX, currentPositionY;
        int facingDirection;
        int steps;


        public Maze() throws IOException {
            for (int row = 0; row < input.size(); row++) {
                char[] choppedLine = input.get(row).toCharArray();
                for (int col = 0; col < choppedLine.length; col++) {
                    this.maze[row][col] = new Tile(choppedLine[col]);
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

        public int countInnerTiles() {
            boolean isIn = false;
            int innerTiles = 0;

            for (int row = 0; row < maze.length; row++) {
                isIn = false;

                for (int col = 0; col < maze[row].length; col++) {
                    Tile currentTile = maze[row][col];

                    if (isIn && !currentTile.isBorder) innerTiles++;
                    else if (isIn && currentTile.isBorder && currentTile.pipeType == '|')
                        isIn = false;
                    else if (isIn && currentTile.isBorder && currentTile.pipeType == '7' && currentTile.facingDirection == FacingDirection.WEST)
                        isIn = false;
                    else if(isIn && currentTile.isBorder && currentTile.pipeType == 'J' && currentTile.facingDirection == FacingDirection.NORTH)
                        isIn = false;
                    else if (!isIn && currentTile.isBorder) isIn = true;
                }
            }
            return innerTiles;
        }

        public void createBorders() {
            step();
            while (maze[currentPositionY][currentPositionX].pipeType != 'S') {
                maze[currentPositionY][currentPositionX].isBorder = true;
                step();
            }
            maze[currentPositionY][currentPositionX].isBorder = true;
        }


        public int countSteps() {
            this.steps = 0;
            step();
            while (maze[currentPositionY][currentPositionX].pipeType != 'S') {
                step();
            }
            return steps;
        }


        public void step() {
            if (facingDirection == FacingDirection.EAST) {
                char nextPipe = maze[currentPositionY][currentPositionX + 1].pipeType;
                maze[currentPositionY][currentPositionX].facingDirection = facingDirection;
                currentPositionX++;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'J' -> Rotation.ROTATE_LEFT;
                    case '7' -> Rotation.ROTATE_RIGHT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.SOUTH) {
                char nextPipe = maze[currentPositionY + 1][currentPositionX].pipeType;
                maze[currentPositionY][currentPositionX].facingDirection = facingDirection;
                currentPositionY++;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'J' -> Rotation.ROTATE_RIGHT;
                    case 'L' -> Rotation.ROTATE_LEFT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.WEST) {
                char nextPipe = maze[currentPositionY][currentPositionX - 1].pipeType;
                maze[currentPositionY][currentPositionX].facingDirection = facingDirection;
                currentPositionX--;
                Rotation rotationDirection = switch (nextPipe) {
                    case 'F' -> Rotation.ROTATE_LEFT;
                    case 'L' -> Rotation.ROTATE_RIGHT;
                    default -> Rotation.DONT_ROTATE;
                };
                rotate(rotationDirection);
            } else if (facingDirection == FacingDirection.NORTH) {
                char nextPipe = maze[currentPositionY - 1][currentPositionX].pipeType;
                maze[currentPositionY][currentPositionX].facingDirection = facingDirection;
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
            for (Tile[] row : maze) {
                for (Tile col : row) {
                    if (col.isBorder)
                        System.out.print("*");
                    else
                        System.out.print(col.pipeType);

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

    static class Tile {
        public char pipeType;
        public boolean isBorder;
        public int facingDirection;

        public Tile(char pipeType) {
            this.pipeType = pipeType;
        }
    }


}
