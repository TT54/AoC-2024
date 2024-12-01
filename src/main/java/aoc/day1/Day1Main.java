package aoc.day1;

import aoc.utils.DataUtils;

import java.util.*;

public class Day1Main {

    public static void main(String[] args) {
        System.out.println("Part 1 example result is " + getPart1Result("/day1/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day1/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day1/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day1/data1"));
    }

    public static int getPart1Result(String dataPath){
        // Getting the lists from datas
        List<String> lines = DataUtils.readResourceFile(dataPath);
        List<Integer> leftList = new ArrayList<>(lines.size());
        List<Integer> rightList = new ArrayList<>(lines.size());
        readDatas(lines, leftList, rightList);

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

    public static int getPart2Result(String dataPath){
        // Getting the lists from datas
        List<String> lines = DataUtils.readResourceFile(dataPath);
        List<Integer> leftList = new ArrayList<>(lines.size());
        List<Integer> rightList = new ArrayList<>(lines.size());
        readDatas(lines, leftList, rightList);

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

    private static void readDatas(List<String> lines, List<Integer> leftList, List<Integer> rightList){
        for(String line : lines){
            String[] input = line.split(" {3}");
            leftList.add(Integer.parseInt(input[0]));
            rightList.add(Integer.parseInt(input[1]));
        }
    }
}
