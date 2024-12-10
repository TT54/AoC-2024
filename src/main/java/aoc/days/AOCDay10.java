package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AOCDay10 implements AbstractDay{

    private static final int[][] neighbours = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private int[][] map;
    private List<Pair<Integer, Integer>> zeroLocations;

    private boolean isValid(int x, int y){
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        List<Pair<Integer, Integer>> toVisit = new ArrayList<>();
        int count = 0;

        for(Pair<Integer, Integer> zeroLocation : zeroLocations) {
            Set<Pair<Integer, Integer>> reached9 = new HashSet<>();
            toVisit.add(zeroLocation);
            while (!toVisit.isEmpty()) {
                Pair<Integer, Integer> pos = toVisit.removeLast();
                int currentValue = map[pos.getFirstElement()][pos.getSecondElement()];
                if (currentValue == 9) {
                    reached9.add(pos);
                    continue;
                }

                for (int[] neighbour : neighbours) {
                    int nextX = pos.getFirstElement() + neighbour[0];
                    int nextY = pos.getSecondElement() + neighbour[1];
                    Pair<Integer, Integer> nextPos = new Pair<>(nextX, nextY);
                    if (isValid(nextX, nextY) && !toVisit.contains(nextPos) && map[nextX][nextY] == currentValue + 1) {
                        toVisit.add(nextPos);
                    }
                }
            }
            count += reached9.size();
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        List<Pair<Integer, Integer>> toVisit = new ArrayList<>();
        int count = 0;

        for(Pair<Integer, Integer> zeroLocation : zeroLocations) {
            toVisit.add(zeroLocation);
            while (!toVisit.isEmpty()) {
                Pair<Integer, Integer> pos = toVisit.removeLast();
                int currentValue = map[pos.getFirstElement()][pos.getSecondElement()];
                if (currentValue == 9) {
                    count++;
                    continue;
                }

                for (int[] neighbour : neighbours) {
                    int nextX = pos.getFirstElement() + neighbour[0];
                    int nextY = pos.getSecondElement() + neighbour[1];
                    Pair<Integer, Integer> nextPos = new Pair<>(nextX, nextY);
                    if (isValid(nextX, nextY) && !toVisit.contains(nextPos) && map[nextX][nextY] == currentValue + 1) {
                        toVisit.add(nextPos);
                    }
                }
            }
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);

        map = new int[lines.size()][lines.getFirst().length()];
        zeroLocations = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                char c = lines.get(i).charAt(j);
                map[i][j] = c == '.' ? -1 : (c - '0');
                if(map[i][j] == 0){
                    zeroLocations.add(new Pair<>(i, j));
                }
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day10/exampleData1"));
        System.out.println("Part 1 example result is " + getPart1Result("/day10/exampleData2"));
        System.out.println("Part 1 result is " + getPart1Result("/day10/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day10/exampleData3"));
        System.out.println("Part 2 example result is " + getPart2Result("/day10/exampleData2"));
        System.out.println("Part 2 result is " + getPart2Result("/day10/data1"));
    }
}
