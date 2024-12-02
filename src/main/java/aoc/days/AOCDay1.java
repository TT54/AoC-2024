package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay1 implements AbstractDay {

    private List<Integer> leftList;
    private List<Integer> rightList;

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day1/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day1/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day1/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day1/data1"));
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        // Sorting the lists (incredibly hard)
        Collections.sort(leftList);
        Collections.sort(rightList);

        // Calculating the distance
        int distance = 0;
        for(int i = 0; i < leftList.size(); i++){
            distance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        return distance;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        // Constructing the apparitions map
        Map<Integer, Integer> rightElementsApparitions = new HashMap<>();
        for(int value : rightList){
            rightElementsApparitions.put(value, rightElementsApparitions.getOrDefault(value, 0) + 1);
        }

        // Calculating the similarity score
        int similarityScore = 0;
        for(int value : leftList){
            similarityScore += value * rightElementsApparitions.getOrDefault(value, 0);
        }

        return similarityScore;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.leftList = new ArrayList<>(lines.size());
        this.rightList = new ArrayList<>(lines.size());

        for(String line : lines){
            String[] input = line.split(" {3}");
            this.leftList.add(Integer.parseInt(input[0]));
            this.rightList.add(Integer.parseInt(input[1]));
        }
    }
}
