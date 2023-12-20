import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {


    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of(".\\input\\day19.txt"));
        ElvesSystem system = new ElvesSystem(input);
        system.sortParts();
        System.out.println("Part 1 answer: " + system.calculatePart1Answer());

    }

    public static class ElvesSystem {

        Map<String, Workflow> workflows;
        List<Part> parts;
        List<Part> accepted;
        List<Part> rejected;

        public ElvesSystem(List<String> input) {
            this.workflows = new HashMap<>();
            this.parts = new ArrayList<>();
            this.accepted = new ArrayList<>();
            this.rejected = new ArrayList<>();

            for (int i = 0; i < 576; i++) {
                String[] choppedLine = input.get(i).split("\\{");
                String workflowName = choppedLine[0];
                String flowsLine = choppedLine[1].substring(0, choppedLine[1].length() - 1);
                String[] flows = flowsLine.split(",");
                List<Condition> conditions = new ArrayList<>();
                for (String flow : flows) {
                    if (flow.length() > 3) {
                        String[] choppedFlow = flow.split(":");
                        char param = choppedFlow[0].charAt(0);
                        char condition = choppedFlow[0].charAt(1);
                        int limit = Integer.parseInt(choppedFlow[0].substring(2));
                        String destination = choppedFlow[1];
                        Condition cond = new Condition(param, condition, limit, destination);
                        conditions.add(cond);
                    } else {
                        conditions.add(new Condition('w', 'w', -1, flow));
                    }
                }
                workflows.put(workflowName, new Workflow(workflowName, conditions));
            }

            for (int i = 577; i < input.size(); i++) {
                String line = input.get(i).substring(1, input.get(i).length() - 1);
                String[] choppedLine = line.split(",");
                int[] values = new int[4];
                for (int j = 0; j < choppedLine.length; j++) {
                    String chop = choppedLine[j];
                    String[] chopChop = chop.split("=");
                    values[j] = Integer.parseInt(chopChop[1]);
                }
                Part part = new Part(values[0], values[1], values[2], values[3]);
                parts.add(part);
            }


        }

        public int calculatePart1Answer() {
            int answer = 0;
            for (Part part : accepted) {
                answer += part.value();
            }
            return answer;
        }

        public void sortParts() {
            for (Part part :
                    parts) {
                sendPartThroughSystem("in", part);
            }
        }

        public void sendPartThroughSystem(String workflowName, Part part) {
            Workflow workflow = workflows.get(workflowName);
            for (int i = 0; i < workflow.conditions.size(); i++) {

                Condition condition = workflow.conditions.get(i);
                if (condition.param == 'w') {
                    if (condition.destination.equals("A")) {
                        accepted.add(part);
                        break;
                    } else if (condition.destination.equals("R")) {
                        rejected.add(part);
                        break;
                    } else {
                        sendPartThroughSystem(condition.destination, part);
                        break;
                    }


                } else if (condition.param == 'x') {
                    if (condition.condition == '>') {
                        if (part.x > condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    } else {
                        if (part.x < condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    }
                } else if (condition.param == 'm') {
                    if (condition.condition == '>') {
                        if (part.m > condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    } else {
                        if (part.m < condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    }
                } else if (condition.param == 'a') {
                    if (condition.condition == '>') {
                        if (part.a > condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    } else {
                        if (part.a < condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    }
                } else if (condition.param == 's') {
                    if (condition.condition == '>') {
                        if (part.s > condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    } else {
                        if (part.s < condition.limit) {
                            if (condition.destination.equals("A")) {
                                accepted.add(part);
                                break;
                            } else if (condition.destination.equals("R")) {
                                rejected.add(part);
                                break;
                            } else {
                                sendPartThroughSystem(condition.destination, part);
                                break;
                            }
                        }
                    }
                }

            }


        }

    }

    public static class Workflow {

        String name;
        List<Condition> conditions;

        public Workflow(String name, List<Condition> conditions) {
            this.name = name;
            this.conditions = conditions;
        }
    }

    public static class Condition {
        char param;
        char condition;
        int limit;
        String destination;

        public Condition(char param, char condition, int limit, String destination) {
            this.param = param;
            this.condition = condition;
            this.limit = limit;
            this.destination = destination;
        }
    }

    public static class Part {
        int x, m, a, s;

        public Part(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public int value(){
            return x + m + a + s;
        }
    }


}
