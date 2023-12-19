import java.util.*;
import java.util.Map;

public class Day17 {
    public static void main(String[] args) {
    }

    public static class Map {
        int[][] map;

        public Map(List<String> input) {
            this.map = new int[input.size()][input.get(0).length()];

            for (int row = 0; row < input.size(); row++) {
                for (int col = 0; col < input.get(row).length(); col++) {
                }
            }

        }
    }

    public static class Tile {
        int heat;
        List<Tile> adjectiveTiles;

        public Tile(int heat) {
            this.heat = heat;
            this.adjectiveTiles = new ArrayList<>();
        }
    }
}
