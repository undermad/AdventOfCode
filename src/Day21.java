import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Day21 {

    public static Tile[][] parseInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(".\\input\\day21.txt"));
        Tile[][] map = new Tile[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                map[i][j] = new Tile(lines.get(i).charAt(j), i, j);
            }
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        Maze maze = new Maze(parseInput());
        maze.bfs(65);
        System.out.println("Part 1 answer: " + maze.countEven());

    }


    public static class Maze {

        Tile[][] map;
        int startX;
        int startY;

        public Maze(Tile[][] map) {
            this.map = map;
            startX = 65;
            startY = 65;

            System.out.println("Starting position: X:" + startX + " Y:" + startY);
            System.out.println("Map width: " + map[0].length);
            System.out.println("Map height: " + map.length);
        }

        public void bfs(int steps) {

            ArrayDeque<Tile> queue = new ArrayDeque<>();
            Tile root = map[startX][startY];
            root.step = 0;
            queue.add(root);

            while (!queue.isEmpty()) {
                Tile t = queue.poll();
                if (t.visited) continue;
                if (t.step == steps + 1) break;
                t.visited = true;
                if (t.step % 2 == 0) t.parity = Parity.EVEN;
                else t.parity = Parity.ODD;
                List<Tile> neighbours = t.getNeighbours(map);
                queue.addAll(neighbours);
            }
        }

        public int countEven() {
            int answer = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if(map[i][j].parity == Parity.EVEN) answer++;
                }
            }
            return answer;
        }

    }

    public static class Tile {
        boolean visited;
        char type;
        Parity parity;
        int positionX;
        int positionY;
        int step;

        public Tile(char type, int y, int x) {
            this.type = type;
            this.positionY = y;
            this.positionX = x;
        }

        public List<Tile> getNeighbours(Tile[][] map) {
            List<Tile> neighbours = new ArrayList<>();
            int[] deltaX = {-1, 0, 1, 0};
            int[] deltaY = {0, 1, 0, -1};
            for (int i = 0; i < deltaX.length; i++) {
                int neighbourX = this.positionX + deltaX[i];
                int neighbourY = this.positionY + deltaY[i];

                if (neighbourX >= 0 && neighbourY >= 0 && neighbourX < map[0].length && neighbourY < map.length) {
                    Tile t = map[neighbourY][neighbourX];
                    if (t.type == '.') {
                        t.step = this.step + 1;
                        neighbours.add(t);
                    }
                }
            }
            return neighbours;

        }

    }

    public enum Parity {
        ODD,
        EVEN
    }


}
