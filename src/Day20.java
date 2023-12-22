import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day20 {

    private static Map<String, Module> parseModules() throws IOException {

        List<String> input = Files.readAllLines(Path.of(".\\input\\day20.txt"));
        List<Conjunction> conjunctionModules = new ArrayList<>();
        Map<String, Module> modules = new HashMap<>();
        input.forEach(line -> {
            String moduleType = "";
            String moduleName = "";
            if (line.contains("broadcaster")) {
                modules.put("broadcaster", new Broadcaster("broadcaster"));
            } else {
                moduleType = line.substring(0, 1);
                moduleName = line.substring(1, 3);
            }

            if (moduleType.equals("&")) {
                Conjunction module = new Conjunction(moduleName);
                modules.put(moduleName, module);
                conjunctionModules.add(module);
            } else if (moduleType.equals("%"))
                modules.put(moduleName, new FlipFlop(moduleName));
        });

        input.forEach(line -> {
            String choppedLine = line.split(">")[1].strip();
            String[] destinations = choppedLine.split(",");
            List<Module> dest = new ArrayList<>();
            for (String destination : destinations) {
                dest.add(modules.get(destination.strip().substring(0, 2)));
            }

            if (!line.contains("broadcaster")) {
                String moduleName = line.substring(1, 3);
                modules.get(moduleName).addConnections(dest);
            } else {
                modules.get("broadcaster").addConnections(dest);
            }

        });


        conjunctionModules.forEach(conjunctionModule -> {
            List<Module> inputs = new ArrayList<>();
            modules.forEach((moduleName, module) -> {
                if (module.destinations.contains(conjunctionModule)) {
                    inputs.add(module);
                }
            });
            conjunctionModule.addInputs(inputs);
        });


        return modules;
    }

    public static void main(String[] args) throws IOException {

        Map<String, Module> modules = parseModules();
        Headquarter hq = new Headquarter(modules);
        for (int i = 0; i < 999; i++) {
            hq.pressButton();
        }
        System.out.println("Part 1 answer: " + hq.multiplyHighByLow());

    }

    public static class Headquarter {
        Map<String, Module> modules;
        int lowPulses;
        int highPulses;

        public Headquarter(Map<String, Module> modules) {
            this.modules = modules;
            this.lowPulses = 0;
            this.highPulses = 0;
        }

        void pressButton() {
            int low = 1;
            int high = 0;

            List<Pulse> pulses = modules.get("broadcaster").receivePulse(new Pulse(Signal.LOW, null, null));
                low+= 4;
            ArrayDeque<Pulse> queue = new ArrayDeque<>(pulses);
            while (!queue.isEmpty()) {
                Pulse pulse = queue.poll();
                if (pulse.signal == Signal.HIGH) high++;
                if (pulse.signal == Signal.LOW) low++;
                if(pulse.receiver == null) continue;
                List<Pulse> newPulses = pulse.receiver.receivePulse(pulse);
                if (!newPulses.isEmpty()) {
                    queue.addAll(newPulses);
                }

            }

            this.lowPulses += low;
            this.highPulses += high;

        }

        public long multiplyHighByLow() {
            long high = (long) highPulses;
            long low = (long) lowPulses;
            return high * low;
        }

    }


    public static abstract class Module {
        String name;

        abstract List<Pulse> receivePulse(Pulse pulse);

        List<Module> destinations;

        public Module(String name) {
            this.name = name;
        }

        public void addConnections(List<Module> modules) {
            destinations = new ArrayList<>(modules);
        }
    }

    public static class Broadcaster extends Module {

        public Broadcaster(String name) {
            super(name);
        }

        @Override
        List<Pulse> receivePulse(Pulse pulse) {
            List<Pulse> pulses = new ArrayList<>();
            for (Module destination : destinations) {
                pulses.add(new Pulse(pulse.signal, this, destination));
            }
            return pulses;
        }
    }

    public static class FlipFlop extends Module {
        boolean isOn;

        public FlipFlop(String name) {
            super(name);
            this.isOn = false;
        }

        @Override
        List<Pulse> receivePulse(Pulse pulse) {
            List<Pulse> signals = new ArrayList<>();
            if (pulse.signal == Signal.LOW) {
                if (isOn) {
                    isOn = false;
                    for (Module destination : destinations)
                        signals.add(new Pulse(Signal.LOW, this, destination));
                } else {
                    isOn = true;
                    for (Module destination : destinations) {
                        signals.add(new Pulse(Signal.HIGH, this, destination));
                    }
                }
            }
            return signals;
        }
    }

    public static class Conjunction extends Module {
        Map<Module, Signal> inputs;

        public Conjunction(String name) {
            super(name);
        }

        @Override
        List<Pulse> receivePulse(Pulse pulse) {

            List<Pulse> signals = new ArrayList<>();
            inputs.put(pulse.sender, pulse.signal);
            boolean isAllHigh = true;
            Set<Module> keys = inputs.keySet();
            for (Module key : keys) {
                Signal signal = inputs.get(key);
                if(signal == Signal.LOW) isAllHigh = false;
            }

            if (isAllHigh) {
                destinations.forEach(destination -> {
                    signals.add(new Pulse(Signal.LOW, this, destination));
                });
            } else {
                destinations.forEach(destination -> {
                    signals.add(new Pulse(Signal.HIGH, this, destination));
                });
            }

            return signals;
        }

        void addInputs(List<Module> modules) {
            inputs = new HashMap<>();
            modules.forEach(module -> inputs.put(module, Signal.LOW));
        }
    }

    public record Pulse(Signal signal, Module sender, Module receiver) {
    }

    enum Signal {
        HIGH,
        LOW,
    }


}

