import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Day08 {

    static class Location implements Comparable<Location> {
        public String location;
        public String left;
        public String right;

        public Location(String location, String left, String right) {
            this.location = location;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Location o) {
            return this.location.compareTo(o.location);
        }
    }


    public static void main(String[] args) {


        try (FileReader fr = new FileReader(".\\input\\day08.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String dirLine = br.readLine();

            char[] directions = dirLine.toCharArray();

            br.readLine();
            String line = br.readLine();

            List<Location> locations = new ArrayList<>();

            while (line != null) {
                String[] choppedLine = line.split("=");
                String location = choppedLine[0].strip();
                String left = choppedLine[1].strip().substring(1, 4);
                String right = choppedLine[1].strip().substring(6, 9);
                locations.add(new Location(location, left, right));

                line = br.readLine();
            }

            Collections.sort(locations);

            Location currentLocation = locations.get(0);
            long result = 0;
            for (int i = 0; i < directions.length; i++) {
                char currentDirection = directions[i];
                int nextLocationIndex;

                if (currentDirection == 'L') {
                    nextLocationIndex = Collections.binarySearch(locations, currentLocation);
                    String nextLocation = locations.get(nextLocationIndex).left;
                    currentLocation = new Location(nextLocation, null, null);
                } else if (currentDirection == 'R') {
                    nextLocationIndex = Collections.binarySearch(locations, currentLocation);
                    String nextLocation = locations.get(nextLocationIndex).right;
                    currentLocation = new Location(nextLocation, null, null);
                }

                System.out.println(currentLocation.location);


                result++;
                if (currentLocation.location.equals("ZZZ")) break;
                if (i == directions.length - 1) i = -1;
            }


            System.out.println(result);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
