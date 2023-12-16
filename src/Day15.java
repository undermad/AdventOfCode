import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Day15 {

    public static void main(String[] args) throws IOException {

        List<String> initializationSequence = List.of(Files.readString(Path.of(".\\input\\day15.txt")).split(","));
        int sum = initializationSequence.stream().mapToInt(Day15::hash).sum();
        System.out.println("Part 1 answer: " + sum);

        HashMap<Integer, LinkedHashMap<String, Integer>> boxes = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            boxes.put(i, new LinkedHashMap<>());
        }

        initializationSequence.forEach(sequence -> {
            if (sequence.contains("=")) {
                String[] chopped = sequence.split("=");
                int boxNumber = hash(chopped[0]);
                int quantity = Integer.parseInt(chopped[1]);
                boxes.get(boxNumber).computeIfPresent(chopped[0], (k, v) -> quantity);
                boxes.get(boxNumber).putIfAbsent(chopped[0], quantity);
            } else {
                String key = sequence.substring(0, sequence.length() - 1);
                int boxNumber = hash(key);
                boxes.get(boxNumber).remove(key);
            }
        });


        int focusingPowerSum = 0;
        for (int i = 0; i < boxes.size(); i++) {
            LinkedHashMap<String, Integer> box = boxes.get(i);

            Set<String> keySet = box.keySet();
            int j = 0;
            for (String key : keySet) {
                focusingPowerSum += ((i+1) * (j+1) * box.get(key));
                j++;
            }
        }
        System.out.println(focusingPowerSum);


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
