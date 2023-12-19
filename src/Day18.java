import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(".\\input\\day18.txt"));
        Map map = new Map(lines);
        map.createBorders();
        map.digger.floodFill();
        map.print();
        System.out.println("Part 1 solution: " + map.calculateLavaHoles());
    }


    public static class Map {
        Tile[][] map;
        List<String> input;
        Digger digger;

        public Map(List<String> input) {
            this.input = input;
            this.map = new Tile[370][500];

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = new Tile('.');
                }
            }
            this.digger = new Digger(this.map);
        }

        public void createBorders() {

            for (String line : input) {
                String[] choppedLine = line.split(" ");
                String direction = choppedLine[0];
                int length = Integer.parseInt(choppedLine[1]);
                String hexColour = choppedLine[2].substring(1, choppedLine[2].length() - 1);

                digger.dig(direction, length, hexColour);
            }


        }

        public void print() {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    System.out.print(map[i][j].type);
                }
                System.out.println();
            }
        }

        public long calculateLavaHoles(){
            long holes = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if(map[i][j].type == '#')
                        holes++;
                }
            }
            return holes;
        }


    }

    public static class Digger {
        int x;
        int y;
        int inX;
        int inY;
        Tile[][] map;

        public Digger(Tile[][] map) {
//            this.x = map[0].length / 2;
//            this.y = map.length / 2;


            this.x = 50;
            this.y = (map.length / 2) + 50;
            this.inX = x + 20;
            this.inY = y + 11;
            this.map = map;
        }

        public void floodFill() {
            Coordinate coordinate = new Coordinate(inX, inY);
            bfs(coordinate, this.map);
        }

        public void bfs(Coordinate root, Tile[][] map) {
            Stack<Coordinate> queue = new Stack<>();
            queue.add(root);

            int height = map.length;
            int width = map[0].length;
            map[root.y][root.x].type = '#';
            boolean[][] visited = new boolean[height][width];
            visited[root.y][root.x] = true;
            while (!queue.isEmpty()) {
                Coordinate c = queue.pop();

                List<Coordinate> neighbours = getNeighbours(c, height, width, map);
                for(Coordinate coordinate : neighbours){
                    if(visited[coordinate.y][coordinate.x]) return;
                    map[coordinate.y][coordinate.x].type = '#';
                    queue.add(coordinate);
                    visited[coordinate.y][coordinate.x] = true;
                }

            }

        }

        private List<Coordinate> getNeighbours(Coordinate node, int height, int width, Tile[][] map) {
            List<Coordinate> neighbours = new ArrayList<>();
            int[] rows = {-1, 0, 1, 0};
            int[] columns = {0, 1, 0, -1};
            for (int i = 0; i < rows.length; i++) {
                int neighbourRow = node.y + rows[i];
                int neighbourCol = node.x + columns[i];
                if(0 <= neighbourCol && neighbourCol < width && 0 <= neighbourRow && neighbourRow < height) {
                    if(map[neighbourRow][neighbourCol].type == '.'){
                        neighbours.add(new Coordinate(neighbourCol, neighbourRow));
                    }
                }
            }
            return neighbours;

        }


        public void dig(String direction, int length, String hexColour) {
            if (direction.equals("L")) {
                for (int i = 1; i <= length; i++) {
                    this.x--;
                    this.map[this.y][this.x].type = '#';
                    this.map[this.y][this.x].hexColour = hexColour;
                }
            } else if (direction.equals("R")) {
                for (int i = 1; i <= length; i++) {
                    this.x++;
                    this.map[this.y][this.x].type = '#';
                    this.map[this.y][this.x].hexColour = hexColour;
                }
            } else if (direction.equals("U")) {
                for (int i = 1; i <= length; i++) {
                    this.y--;
                    this.map[this.y][this.x].type = '#';
                    this.map[this.y][this.x].hexColour = hexColour;
                }
            } else if (direction.equals("D")) {
                for (int i = 1; i <= length; i++) {
                    this.y++;
                    this.map[this.y][this.x].type = '#';
                    this.map[this.y][this.x].hexColour = hexColour;
                }
            }
        }
    }

    public static class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Tile {
        public String hexColour;
        public char type;

        public Tile(char type) {
            this.type = type;
        }
    }
}
