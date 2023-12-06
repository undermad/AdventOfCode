import java.util.Map;

public class Day06 {

    public static void main(String[] args) {


        Map<Integer, Integer> timeToRecord = Map.of(
                63, 411,
                78, 1274,
                94, 2047,
                68, 1035);
        long result = 1;

        for (Integer t :
                timeToRecord.keySet()) {

            int time = t;
            int record = timeToRecord.get(time);
            int winningOptions = 0;

            for (int i = t; i >= 0; i--) {

                int timeToHold = time - i;
                int timeForTravel = time - timeToHold;
                if (timeForTravel * timeToHold > record) {
                    winningOptions++;
                }
            }

            result *= winningOptions;
        }

        System.out.println(result);

    }
}
