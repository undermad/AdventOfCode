import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Day22 {
    public static Cube[][][] parseInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of(".\\input\\day22.txt"));

        Cube[][][] grid = new Cube[10][10][1000];

        input.forEach(line -> {
            String[] choppedLineLeft = line.split("~")[0].split(",");
            Coordinates startingCoordinates = new Coordinates(
                    Integer.parseInt(choppedLineLeft[0]),
                    Integer.parseInt(choppedLineLeft[1]),
                    Integer.parseInt(choppedLineLeft[2]));
            Brick brick = new Brick();
            String[] choppedLineRight = line.split("~")[1].split(",");
            int spanX = Integer.parseInt(choppedLineRight[0]) - startingCoordinates.x;
            int spanY = Integer.parseInt(choppedLineRight[1]) - startingCoordinates.y;
            int spanZ = Integer.parseInt(choppedLineRight[2]) - startingCoordinates.z;

            if (spanX != 0) {
                for (int i = 0; i <= spanX; i++) {
                    Coordinates nextCubeCoordinates = new Coordinates(
                            startingCoordinates.x + i,
                            startingCoordinates.y,
                            startingCoordinates.z);
                    Cube c = new Cube(nextCubeCoordinates, brick);
                    brick.addCube(c);
                    grid[nextCubeCoordinates.x][nextCubeCoordinates.y][nextCubeCoordinates.z] = c;
                }
            } else if (spanY != 0) {
                for (int i = 0; i <= spanY; i++) {
                    Coordinates nextCubeCoordinates = new Coordinates(
                            startingCoordinates.x,
                            startingCoordinates.y + i,
                            startingCoordinates.z);
                    Cube c = new Cube(nextCubeCoordinates, brick);
                    brick.addCube(c);
                    grid[nextCubeCoordinates.x][nextCubeCoordinates.y][nextCubeCoordinates.z] = c;
                }
            } else if (spanZ != 0) {
                for (int i = 0; i <= spanZ; i++) {
                    Coordinates nextCubeCoordinates = new Coordinates(
                            startingCoordinates.x,
                            startingCoordinates.y,
                            startingCoordinates.z + i);
                    Cube c = new Cube(nextCubeCoordinates, brick);
                    brick.addCube(c);
                    brick.vertical = true;
                    grid[nextCubeCoordinates.x][nextCubeCoordinates.y][nextCubeCoordinates.z] = c;
                }
            } else {
                Cube cube = new Cube(startingCoordinates, brick);
                brick.addCube(cube);
                grid[startingCoordinates.x][startingCoordinates.y][startingCoordinates.z] = cube;
            }

        });

        return grid;
    }

    public static void main(String[] args) throws IOException {

        BrickTower brickTower = new BrickTower(parseInput());
        brickTower.addAllBricks();
        brickTower.letBricksFall();
        brickTower.addBrickBases();
        System.out.println("Part 1 answer: " + brickTower.safeToDisintegrateBricks());

    }

    public static class BrickTower {

        Cube[][][] grid;
        Set<Brick> bricks;

        public BrickTower(Cube[][][] grid) {
            this.grid = grid;
            this.bricks = new HashSet<>();
        }

        public void letBricksFall() {
            for (int z = 1; z < grid[0][0].length; z++) {
                for (int x = 0; x < grid.length; x++) {
                    for (int y = 0; y < grid[x].length; y++) {
                        if (grid[x][y][z] != null) {
                            grid[x][y][z].brick.fallDown(grid);
                        }
                    }
                }
            }

        }

        public void addBrickBases() {
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    for (int z = 0; z < grid[x][y].length; z++) {
                        if (grid[x][y][z] != null) {
                            grid[x][y][z].brick.addBaseBricks(grid);
                            grid[x][y][z].brick.addAboveBricks(grid);
                        }
                    }
                }
            }
        }

        public void addAllBricks() {
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    for (int z = 0; z < grid[x][y].length; z++) {
                        if (grid[x][y][z] != null) {
                            Brick brick = grid[x][y][z].brick;
                            bricks.add(brick);
                        }
                    }
                }
            }
        }

        public int safeToDisintegrateBricks() {
            int result = 0;
            for (Brick brick : bricks) {
                if (brick.checkIfCanBeRemoved()) {
                    result++;
                }
            }

            return result;
        }


    }

    public static class Cube {
        Coordinates coordinates;
        Brick brick;

        public Cube(Coordinates coordinates, Brick brick) {
            this.coordinates = coordinates;
            this.brick = brick;
        }
    }


    public static class Brick {
        List<Cube> cubes;
        Set<Brick> belowBricks;
        Set<Brick> aboveBricks;
        boolean vertical;

        public Brick() {
            this.cubes = new ArrayList<>();
            this.belowBricks = new HashSet<>();
            this.aboveBricks = new HashSet<>();
            this.vertical = false;
        }

        public void addCube(Cube cube) {
            cubes.add(cube);
        }

        public void fallDown(Cube[][][] grid) {
            if (!canFallDown(grid)) return;

            for (Cube cube : cubes) {
                cube.coordinates.z--;
                grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z] = cube;
                grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z + 1] = null;
            }
            fallDown(grid);
        }

        public boolean canFallDown(Cube[][][] grid) {

            if (vertical) {
                Cube bottomCube = cubes.get(0);
                if (grid[bottomCube.coordinates.x][bottomCube.coordinates.y][bottomCube.coordinates.z - 1] != null || bottomCube.coordinates.z - 1 == 0) {
                    return false;
                }
            } else {
                for (Cube cube : cubes) {
                    if (grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z - 1] != null || cube.coordinates.z - 1 == 0) {
                        return false;
                    }
                }
            }
            return true;
        }


        public boolean checkIfCanBeRemoved() {

            boolean safe = true;
            for (Brick aboveBrick : aboveBricks) {
                if (aboveBrick.belowBricks.size() == 1) {
                    safe = false;
                }
            }
            return safe;
        }

        public void addBaseBricks(Cube[][][] grid) {
            cubes.forEach(cube -> {
                if (cube.coordinates.z != 0 && grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z - 1] != null) {
                    Brick belowBrick = grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z - 1].brick;
                    if (belowBrick != this) {
                        this.addBaseBrick(belowBrick);
                    }
                }
            });
        }

        public void addAboveBricks(Cube[][][] grid) {
            cubes.forEach(cube -> {
                if (grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z + 1] != null) {
                    Brick aboveBrick = grid[cube.coordinates.x][cube.coordinates.y][cube.coordinates.z + 1].brick;
                    if (aboveBrick != this) {
                        this.addAboveBrick(aboveBrick);
                    }
                }
            });
        }

        public void addBaseBrick(Brick brick) {
            belowBricks.add(brick);
        }

        public void addAboveBrick(Brick brick) {
            aboveBricks.add(brick);
        }

    }

    public static class Coordinates {
        int x;
        int y;
        int z;

        public Coordinates(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}
