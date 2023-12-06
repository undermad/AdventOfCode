import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day05 {
    static class Interval {
        long start;
        long end;
        long destination;

        public Interval(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public Interval(long start, long end, long destination) {
            this.start = start;
            this.end = end;
            this.destination = destination;
        }
    }

    private static long checkMapping(long seed, String line) {
        String[] destinationSourceRange = line.split(" ");
        long destination = Long.parseLong(destinationSourceRange[0]);
        long source = Long.parseLong(destinationSourceRange[1]);
        long range = Long.parseLong(destinationSourceRange[2]);

        if (source + range > seed && seed > source) {
            long offset = destination - source;
            return seed + offset;
        }
        return seed;
    }

    private static long checkMapping(long seed, Interval interval) {
        long destination = interval.destination;
        long source = interval.start;
        long range = interval.end;

        if (source + range > seed && seed > source) {
            long offset = destination - source;
            return seed + offset;
        }
        return seed;
    }

    public static void main(String[] args) {
        List<Long> seeds = extractSeeds();
        List<Interval> intervals = getIntervals(seeds);
        Stack<Interval> seedsTrueIntervals = mergeIntervals(intervals);
        List<List<Interval>> sections = extractMapping();

        for (Interval interval :
                seedsTrueIntervals) {
            System.out.println("START: " + interval.start + " END: " + interval.end);
        }

//        long smallestLocation = Long.MAX_VALUE;
//        for (Interval interval :
//                seedsTrueIntervals) {
//            System.out.println("STARTING INTERVAL: " + interval.start);
//
//            for (long a = interval.start; a < interval.end; a++) {
//                long currentSeed = a;
//
//
//                for (int i = 0; i < sections.size(); i++) {
//                    for (int j = 0; j < sections.get(i).size(); j++) {
//
//                        long newSeed = checkMapping(currentSeed, sections.get(i).get(j));
//                        if (newSeed != currentSeed) {
//                            currentSeed = newSeed;
//                            break;
//                        }
//                    }
//                }
//
//                if (currentSeed < smallestLocation) smallestLocation = currentSeed;
//            }
//        }


        long smallestLocation = Long.MAX_VALUE;
        for (Interval interval : seedsTrueIntervals) {
            System.out.println("STARTING INTERVAL: " + interval.start);
            for (long i = interval.start; i < interval.end; i++) {
                long result = getFinalSeedLocation(i);
                if (result < smallestLocation) {
                    smallestLocation = result;
                }
            }
            System.out.println("ENDING INTERVAL: " + interval.end);

        }


        List<Long> finalSeedsLocations = new ArrayList<>();

        for (Long seed : seeds) {
            finalSeedsLocations.add(getFinalSeedLocation(seed));
        }
        Long closestLocation = Collections.min(finalSeedsLocations);
        System.out.println(closestLocation);
        System.out.println(smallestLocation);


    }

    private static List<List<Interval>> extractMapping() {

        List<List<Interval>> sections = new ArrayList<>();
        try (FileReader fr = new FileReader(".\\input\\day05.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            line = br.readLine();

            List<Interval> section = new ArrayList<>();
            while (line != null) {
                if (line.isEmpty()) {
                    line = skipEmptyLine(br);
                    sections.add(section);
                    section = new ArrayList<>();
                }

                String[] destinationSourceRange = line.split(" ");
                section.add(new Interval(Long.parseLong(destinationSourceRange[1]),
                        Long.parseLong(destinationSourceRange[2]),
                        Long.parseLong(destinationSourceRange[0])));


                line = br.readLine();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sections;
    }

    private static List<Interval> getIntervals(List<Long> seeds) {
        List<Interval> intervals = new ArrayList<>();

        for (int i = 0; i < seeds.size(); i += 2) {
            long temp = seeds.get(i);
            intervals.add(new Interval(temp, temp + seeds.get(i + 1)));
        }
        return intervals;
    }

    private static Stack<Interval> mergeIntervals(List<Interval> intervals) {
        intervals.sort((o1, o2) -> Long.signum(o1.start - o2.start));
        Stack<Interval> stack = new Stack<>();
        stack.push(intervals.get(0));
        for (int i = 1; i < intervals.size(); i++) {
            Interval top = stack.peek();
            if (top.end < intervals.get(i).start)
                stack.push(intervals.get(i));
            else {
                top.end = intervals.get(i).end;
                stack.pop();
                stack.push(top);
            }
        }
        return stack;
    }

    private static long getFinalSeedLocation(Long seed) {
        long currentSeed = seed;

        try (FileReader fr = new FileReader(".\\input\\day05.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {
                if (line.isEmpty()) {
                    line = skipEmptyLine(br);
                }

                long newSeed = checkMapping(currentSeed, line);

                if (newSeed != currentSeed) {
                    skipToNextSection(line, br);
                    currentSeed = newSeed;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentSeed;
    }

    private static void skipToNextSection(String line, BufferedReader br) throws IOException {
        while (line != null && !line.isEmpty()) {
            line = br.readLine();
        }
        line = br.readLine();
    }

    private static String skipEmptyLine(BufferedReader br) throws IOException {
        String line;
        line = br.readLine();
        line = br.readLine();
        return line;
    }


    private static List<Long> extractSeeds() {
        try (FileReader fr = new FileReader(".\\input\\day05.txt");
             BufferedReader br = new BufferedReader(fr)) {
            String seedLine = br.readLine();


            String[] choppedLine = seedLine.split(":")[1].strip().split(" ");
            List<Long> seeds = new ArrayList<>();
            for (String s : choppedLine) {
                seeds.add(Long.parseLong(s));
            }
            return seeds;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
