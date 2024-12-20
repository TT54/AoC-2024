package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AOCDay20 implements AbstractDay {

    private static final int[][] directions = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static final DecimalFormat format = new DecimalFormat("00");

    private int[][] raceMap;
    private int[] startingPoint;
    private int[] endPoint;

    private void printRace(){
        for(int x = 0; x < raceMap.length; x++){
            for(int y = 0; y < raceMap[0].length; y++){
                System.out.print(raceMap[x][y] == -10 ? " # " : format.format(this.raceMap[x][y]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void fillMap(int[] point, int value){
        raceMap[point[0]][point[1]] = value;
        if(point[0] != startingPoint[0] || point[1] != startingPoint[1]){
            for(int[] direction : directions){
                int nextX = point[0] + direction[0];
                int nextY = point[1] + direction[1];
                if(raceMap[nextX][nextY] == -1){
                    fillMap(new int[] {nextX, nextY}, value + 1);
                }
            }
        }
    }

    private boolean isInMap(int[] position){
        return position[0] >= 0 && position[1] >= 0 && position[0] < this.raceMap.length && position[1] < this.raceMap[0].length;
    }

    private int getNewRaceTime(int[] currentPosition, int[] endPosition){
        return this.raceMap[currentPosition[0]][currentPosition[1]] - this.raceMap[endPosition[0]][endPosition[1]] - 2;
    }

    private Map<List<Pair<Integer, Integer>>, Integer> getCheats(){
        Map<List<Pair<Integer, Integer>>, Integer> cheatsTime = new HashMap<>();
        for(int i = 0; i < this.raceMap.length; i++){
            for(int j = 0; j < this.raceMap[0].length; j++){
                if(this.raceMap[i][j] >= 0){
                    for(int[] wallGhostDirection : directions){
                        int endX = i + 2 *wallGhostDirection[0];
                        int endY = j + 2 * wallGhostDirection[1];
                        int[] initialPos = new int[] {i, j};
                        int[] endPos = new int[] {endX, endY};

                        if(isInMap(endPos) && this.raceMap[endX][endY] >= 0) {
                            int time = getNewRaceTime(initialPos, endPos);
                            if (time > 0) {
                                cheatsTime.put(Arrays.asList(new Pair<>(i, j), new Pair<>(endX, endY)), time);
                            }
                        }
                    }
                }
            }
        }
        return cheatsTime;
    }

    private int getCaseDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x2 - x1) + Math.abs((y2 - y1));
    }

    private Map<List<Pair<Integer, Integer>>, Integer> getLongCheats(){
        Map<List<Pair<Integer, Integer>>, Integer> cheatsTime = new HashMap<>();
        for(int x1 = 0; x1 < this.raceMap.length; x1++){
            for(int y1 = 0; y1 < this.raceMap[0].length; y1++){
                if(this.raceMap[x1][y1] >= 0){
                    for(int x2 = 0; x2 < this.raceMap.length; x2++){
                        for(int y2 = 0; y2 < this.raceMap[0].length; y2++){
                            if(this.raceMap[x2][y2] >= 0){
                                if(getCaseDistance(x1, y1, x2, y2) <= 20){
                                    int savedTime = this.raceMap[x1][y1] - this.raceMap[x2][y2] - getCaseDistance(x1, y1, x2, y2);
                                    if(savedTime > 0){
                                        cheatsTime.put(Arrays.asList(new Pair<>(x1, y1), new Pair<>(x2, y2)), savedTime);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return cheatsTime;
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        int count = 0;

        fillMap(endPoint, 0);
        Map<List<Pair<Integer, Integer>>, Integer> cheats = getCheats();
        Map<Integer, Integer> cheatCount = new HashMap<>();

        for(List<Pair<Integer, Integer>> cheat : cheats.keySet()){
            cheatCount.put(cheats.get(cheat), cheatCount.getOrDefault(cheats.get(cheat), 0) + 1);
        }

        for(int i : cheatCount.keySet()){
            if(i >= 100){
                count += cheatCount.get(i);
            }
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        long count = 0;

        fillMap(endPoint, 0);
        Map<List<Pair<Integer, Integer>>, Integer> cheats = getLongCheats();
        Map<Integer, Integer> cheatCount = new HashMap<>();

        for(List<Pair<Integer, Integer>> cheat : cheats.keySet()){
            cheatCount.put(cheats.get(cheat), cheatCount.getOrDefault(cheats.get(cheat), 0) + 1);
        }

        for(int i : cheatCount.keySet()){
            if(i >= 50){
                System.out.println(i + ": " + cheatCount.get(i));
            }
            if(i >= 100){
                count += cheatCount.get(i);
            }
        }

        return (int) count;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.raceMap = new int[lines.size()][lines.getFirst().length()];
        
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            for(int j = 0; j < line.length(); j++){
                switch (line.charAt(j)){
                    case '#': 
                        this.raceMap[i][j] = -10;
                        break;
                    case 'S':
                        this.startingPoint = new int[] {i, j};
                        this.raceMap[i][j] = -1;
                        break;
                    case 'E':
                        this.endPoint = new int[] {i, j};
                        this.raceMap[i][j] = -1;
                        break;
                    case '.':
                        this.raceMap[i][j] = -1;
                        break;
                }
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day20/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day20/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day20/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day20/data1"));
    }
}
