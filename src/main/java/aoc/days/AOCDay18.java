package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.*;

public class AOCDay18 implements AbstractDay {

    private static final int[][] directions = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    private int beginX = 0, beginY = 0;
    private int maxPos;
    private int positionsToCorrupt;

    private int[][] maze;
    private List<Pair<Integer, Integer>> corruptedPositions;

    private void corruptPositions(int amount){
        for(int i = 0; i < Math.min(amount, corruptedPositions.size()); i++){
            maze[corruptedPositions.get(i).getSecondElement()][corruptedPositions.get(i).getFirstElement()] = -1;
        }
    }

    private void printMaze(){
        for(int x = 0; x < maze.length; x++){
            for(int y = 0; y < maze[0].length; y++){
                System.out.print(maze[x][y] == -1 ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isValidPosition(int x, int y){
        return x >= 0 && x <= maxPos && y >= 0 && y <= maxPos;
    }

    private boolean isValidPosition(Pair<Integer, Integer> pos){
        return isValidPosition(pos.getFirstElement(), pos.getSecondElement());
    }

    private Pair<Integer, Integer> getNextPosition(Map<Pair<Integer, Integer>, Integer> nextValues){
        Pair<Integer, Integer> nextValue = null;
        int nextDistance = Integer.MAX_VALUE;
        for(Map.Entry<Pair<Integer, Integer>, Integer> entry : nextValues.entrySet()){
            if(entry.getValue() < nextDistance){
                nextValue = entry.getKey();
                nextDistance = entry.getValue();
            }
        }
        return nextValue;
    }

    private void doDijkstra(){
        Map<Pair<Integer, Integer>, Integer> nextValues = new HashMap<>();
        nextValues.put(new Pair<>(beginX, beginY), 0);

        while(!nextValues.isEmpty()){
            Pair<Integer, Integer> position = getNextPosition(nextValues);
            int distance = nextValues.remove(position);

            maze[position.getFirstElement()][position.getSecondElement()] = distance;

            for(int[] direction : directions){
                int nextX = position.getFirstElement() + direction[0];
                int nextY = position.getSecondElement() + direction[1];
                Pair<Integer, Integer> nextPos = new Pair<>(nextX, nextY);

                if(isValidPosition(nextX, nextY) && maze[nextX][nextY] == 0 && !nextValues.containsKey(nextPos)){
                    nextValues.put(nextPos, distance + 1);
                }
            }
        }
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        corruptPositions(positionsToCorrupt);
        doDijkstra();

        return maze[maxPos][maxPos];
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        int amountOfCorruptedPositions = 0;
        do{
            amountOfCorruptedPositions++;
            maze = new int[maxPos + 1][maxPos + 1];
            corruptPositions(amountOfCorruptedPositions);
            doDijkstra();
        } while(maze[maxPos][maxPos] != 0);

        System.out.println(corruptedPositions.get(amountOfCorruptedPositions - 1).toString());

        return amountOfCorruptedPositions;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        maze = new int[maxPos + 1][maxPos + 1];
        corruptedPositions = new ArrayList<>();

        for(String line : lines){
            String[] pos = line.split(",");
            corruptedPositions.add(new Pair<>(Integer.parseInt(pos[0]), Integer.parseInt(pos[1])));
        }
    }

    private void setParameters(int maxPos, int positionsToCorrupt) {this.maxPos = maxPos; this.positionsToCorrupt = positionsToCorrupt;}

    @Override
    public void showResults() {
        this.setParameters(6, 12);
        System.out.println("Part 1 example result is " + getPart1Result("/day18/exampleData1"));
        this.setParameters(70, 1024);
        System.out.println("Part 1 result is " + getPart1Result("/day18/data1"));

        this.setParameters(6, 12);
        System.out.println("Part 2 example result is " + getPart2Result("/day18/exampleData1"));
        this.setParameters(70, 1024);
        System.out.println("Part 2 result is " + getPart2Result("/day18/data1"));
    }
}
