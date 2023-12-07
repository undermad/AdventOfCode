import java.util.Map;

public class Day06 {

    public static void main(String[] args) {


        //part 1
        Map<Integer, Integer> timeToRecord = Map.of(
                63, 411,
                78, 1274,
                94, 2047,
                68, 1035);
        long resultPartOne = 1;

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

            resultPartOne *= winningOptions;
        }

        System.out.println(resultPartOne);


        //part 2

        long resultPartTwo = 1;


        long time = 63789468;
        long record = 411127420471035L;
        int winningOptions = 0;

        for (long i = time; i >= 0; i--) {

            long timeToHold = time - i;
            long timeForTravel = time - timeToHold;
            if (timeForTravel * timeToHold > record) {
                winningOptions++;
            }
        }

        resultPartTwo *= winningOptions;
        System.out.println(resultPartTwo);
    }

}

