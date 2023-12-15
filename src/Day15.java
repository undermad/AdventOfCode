import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day15 {

    public static void main(String[] args) throws IOException {

        List<String> initializationSequence = List.of(Files.readString(Path.of(".\\input\\day15.txt")).split(","));
        int sum = initializationSequence.stream().mapToInt(Day15::hash).sum();
        System.out.println(sum);
    }

    public static int hash(String sequence) {
        int currentValue = 0;
        char[] characters = sequence.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            currentValue += (int) characters[i];
            currentValue *= 17;
            currentValue %= 256;
        }
        return currentValue;
    }
}
