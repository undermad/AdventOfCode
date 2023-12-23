import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day23 {


    public static void main(String[] args) throws IOException {
        Graph graph = new Graph(parseInput());
        graph.findPath();

        int steps = 0;
        Node node = graph.graph[0][1];
        while (node != null) {
            steps++;
            node = node.parentNode;
        }
        System.out.println( steps);


    }

    public static Node[][] parseInput() throws IOException {

        List<String> input = Files.readAllLines(Path.of(".\\input\\day23.txt"));
        Node[][] graph = new Node[input.size()][input.get(0).length()];

        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).length(); col++) {
                boolean walkable = input.get(row).charAt(col) != '#';
                Node node = new Node(walkable, new Coordinates(col, row), input.get(row).charAt(col));
                graph[row][col] = node;
            }
        }

        for (int row = 0; row < graph.length; row++) {
            for (int col = 0; col < graph[0].length; col++) {
                if (graph[row][col].walkable) {
                    graph[row][col].addNeighbours(getNeighbours(graph, graph[row][col]));
                }
            }
        }

        return graph;
    }

    public static List<Node> getNeighbours(Node[][] graph, Node node) {
        List<Node> neighbours = new ArrayList<>();
        Coordinates nodeCoordinates = node.coordinates;
        int[] deltaY = {1, 0, -1, 0};
        int[] deltaX = {0, -1, 0, 1};
        for (int i = 0; i < deltaX.length; i++) {
            int x = nodeCoordinates.x + deltaX[i];
            int y = nodeCoordinates.y + deltaY[i];

            if (x >= 0 && x < graph.length && y >= 0 && y < graph[0].length) {
                Node neighbour = graph[y][x];
                if (neighbour.walkable) {
                    if (i == 0 && neighbour.type != '^') {
                        neighbours.add(neighbour);
                    } else if (i == 1 && neighbour.type != '>') {
                        neighbours.add(neighbour);
                    } else if (i == 2 && neighbour.type != 'v') {
                        neighbours.add(neighbour);
                    } else if (i == 3 && neighbour.type != '<') {
                        neighbours.add(neighbour);
                    }
                }
            }
        }
        return neighbours;
    }

    public static class Graph {
        Node[][] graph;

        public Graph(Node[][] graph) {
            this.graph = graph;
        }


        public void findPath() {

            List<Node> opened = new ArrayList<>();
            List<Node> closed = new ArrayList<>();

            Node root = graph[0][1];
            Node destination = graph[140][139];

            Node currentNode = root;
            calculateGHF(root, destination, currentNode);


            opened.add(currentNode);
            while (!opened.isEmpty()) {
                currentNode = bestNode(opened);
                closed.add(currentNode);
                opened.remove(currentNode);
                List<Node> currentNodeNeighbours = currentNode.neighbours;

                for (Node neighbour : currentNodeNeighbours) {
                    if (neighbour.coordinates.y == destination.coordinates.y && neighbour.coordinates.x == destination.coordinates.x) {
                        System.out.println("Destination found!");
                        currentNode.parentNode = neighbour;
                        return;
                    }
                    System.out.println(neighbour.coordinates.x + " " + neighbour.coordinates.y);
                    calculateGHF(root, destination, neighbour);
                    if (!closed.contains(neighbour)) {
                        opened.add(neighbour);
                    }
                }
                currentNode.parentNode = bestNode(opened);

            }

        }

        private void calculateGHF(Node root, Node destination, Node currentNode) {
            currentNode.distFromRoot = Math.abs(currentNode.coordinates.x - root.coordinates.x) + Math.abs(currentNode.coordinates.y - root.coordinates.y) * 10;
            currentNode.distFromDestination = Math.abs(currentNode.coordinates.x - destination.coordinates.x) + Math.abs(currentNode.coordinates.y - destination.coordinates.y) * 10;
            currentNode.distCost = (currentNode.distFromRoot + currentNode.distFromDestination) * 10;
        }

        private Node bestNode(List<Node> nodes) {
            int bestNodeIndex = 0;
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).distCost > nodes.get(bestNodeIndex).distCost) {
                    bestNodeIndex = i;
                } else if (nodes.get(i).distCost == nodes.get(bestNodeIndex).distCost) {
                    if (nodes.get(i).distFromDestination > nodes.get(bestNodeIndex).distFromDestination) {
                        bestNodeIndex = i;
                    }
                }
            }
            return nodes.get(bestNodeIndex);
        }


    }

    public static class Node {
        private final boolean walkable;
        private final Coordinates coordinates;
        private List<Node> neighbours;
        private final char type;

        private Node parentNode;
        private int distFromRoot;
        private int distFromDestination;
        private int distCost;

        public Node(boolean walkable, Coordinates coordinates, char type) {
            this.walkable = walkable;
            this.coordinates = coordinates;
            this.type = type;
            this.neighbours = new ArrayList<>();
        }

        public void addNeighbours(List<Node> neighbours) {
            this.neighbours = neighbours;
        }
    }

    public static class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


}

