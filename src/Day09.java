import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day09 {

    static class Oasis {
        private List<List<Long>> input;

        public Oasis(List<String> report) {
            this.input = report.stream()
                    .map(line -> Arrays.stream(line.split(" "))
                            .map(Long::parseLong)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }

        public long extrapolateOasis(boolean partTwo){
            return input.stream().mapToLong(line -> extrapolateLine(line, partTwo)).sum();
        }

        private long extrapolateLine(List<Long> line, boolean partTwo) {
            if(areAllZeros(line)) return 0;

            List<Long> newLine = IntStream.range(0, line.size() - 1)
                    .mapToObj(i -> line.get(i + 1) - line.get(i)).toList();
            return partTwo ? line.get(0) - extrapolateLine(newLine, partTwo) : line.get(line.size() - 1) + extrapolateLine(newLine, partTwo);
        }

        private boolean areAllZeros(List<Long> line){
            return line.stream().allMatch(v -> v == 0L);
        }

    }

    public static void main(String[] args) throws IOException {
        Oasis oasis = new Oasis(extractReport());
        System.out.println("Part 1: " + oasis.extrapolateOasis(false));
        System.out.println("Part 2 " + oasis.extrapolateOasis(true));
    }

    private static List<String> extractReport() throws IOException {
        return Files.readAllLines(Path.of(".\\input\\day09.txt"));
    }


}
