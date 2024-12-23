package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.text.DecimalFormat;
import java.util.*;

public class AOCDay23 implements AbstractDay {

    private Map<String, Set<String>> connectedComputers;

    private boolean areComputersConnected(String computer1, String computer2){
        return connectedComputers.get(computer1).contains(computer2);
    }

    private Set<String> getConnectedComputers(String computer){
        return connectedComputers.get(computer);
    }

    private void printConnectedComputers(Set<String> computers){
        List<String> sortedComputers = computers.stream().sorted().toList();
        String result = "";
        for(String computer : sortedComputers){
            result += computer + ",";
        }
        System.out.println(result.substring(0, result.length() - 1));
    }

    private Set<String> createBiggestCompleteGraph(String startingComputer){
        Set<String> connectedComputers = new HashSet<>();
        Set<String> alreadyVisitedComputers = new HashSet<>();
        List<String> toVisit = new ArrayList<>();
        toVisit.add(startingComputer);

        while(!toVisit.isEmpty()){
            String computer = toVisit.removeLast();
            if(alreadyVisitedComputers.contains(computer)){
                continue;
            }

            alreadyVisitedComputers.add(computer);

            boolean isConnected = true;
            for(String connectedComputer : connectedComputers){
                if(!areComputersConnected(computer, connectedComputer)) {
                    isConnected = false;
                    break;
                }
            }

            if(isConnected){
                connectedComputers.add(computer);

                for(String nextComputer : getConnectedComputers(computer)){
                    if(!alreadyVisitedComputers.contains(nextComputer)){
                        toVisit.add(nextComputer);
                    }
                }
            }
        }

        return connectedComputers;
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        Set<Set<String>> solutionComputers = new HashSet<>();
        for(String computer : connectedComputers.keySet()){
            for(String computer2 : getConnectedComputers(computer)){
                for(String computer3 : getConnectedComputers(computer2)){
                    if(!computer3.equalsIgnoreCase(computer) && (computer.startsWith("t") || computer2.startsWith("t") || computer3.startsWith("t")) && areComputersConnected(computer3, computer)){
                        solutionComputers.add(Set.of(computer, computer2, computer3));
                    }
                }
            }
        }

        return solutionComputers.size();
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        long count = 0;

        Set<String> biggestConnectedComputersGraph = new HashSet<>();
        for(String computer : connectedComputers.keySet()){
            Set<String> connectedComputers = createBiggestCompleteGraph(computer);
            if(biggestConnectedComputersGraph.size() < connectedComputers.size()){
                biggestConnectedComputersGraph = connectedComputers;
            }
        }

        printConnectedComputers(biggestConnectedComputersGraph);

        return (int) count;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        connectedComputers = new HashMap<>();

        for(String line : lines){
            String[] computers = line.split("-");

            Set<String> firstComputerConnections = connectedComputers.getOrDefault(computers[0], new HashSet<>());
            firstComputerConnections.add(computers[1]);
            connectedComputers.put(computers[0], firstComputerConnections);

            Set<String> secondComputerConnections = connectedComputers.getOrDefault(computers[1], new HashSet<>());
            secondComputerConnections.add(computers[0]);
            connectedComputers.put(computers[1], secondComputerConnections);
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day23/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day23/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day23/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day23/data1"));
    }
}
