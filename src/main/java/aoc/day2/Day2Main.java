package aoc.day2;

import aoc.utils.DataUtils;

import java.util.*;

public class Day2Main {

    public static void main(String[] args) {
        System.out.println("Part 1 example result is " + getPart1Result("/day2/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day2/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day2/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day2/data1"));
    }
    public static int getPart1Result(String dataPath){
        // Getting the array from datas
        List<String> lines = DataUtils.readResourceFile(dataPath);
        List<List<Integer>> reports = readDatas(lines);

        int validCount = 0;
        for(List<Integer> report : reports){
            int previousValue = report.getFirst();
            int previousSign = 0;
            boolean valid = true;
            for(int i = 1; i < report.size(); i++){
                int distance = Math.abs(report.get(i) - previousValue);

                if(distance == 0 || distance > 3){
                    valid = false;
                    break;
                }

                int sign = (report.get(i) - previousValue) / distance;

                if(previousSign == 0){
                    previousSign = sign;
                } else if(previousSign != sign){
                    valid = false;
                    break;
                }

                previousValue = report.get(i);
            }

            if(valid){
                validCount++;
            }
        }

        return validCount;
    }

    private static boolean isValid(List<Integer> report){
        int previousValue = report.getFirst();
        int previousSign = 0;
        for(int i = 1; i < report.size(); i++){
            int distance = Math.abs(report.get(i) - previousValue);

            if(distance == 0 || distance > 3){
                return false;
            }

            int sign = (report.get(i) - previousValue) / distance;

            if(previousSign == 0){
                previousSign = sign;
            } else if(previousSign != sign){
                return false;
            }

            previousValue = report.get(i);
        }

        return true;
    }

    public static int getPart2Result(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        List<List<Integer>> reports = readDatas(lines);

        int validCount = 0;
        for(List<Integer> report : reports){
            for(int i = 0; i < report.size(); i++){
                List<Integer> reportCopy = new ArrayList<>(report);
                reportCopy.remove(i);
                if(isValid(reportCopy)){
                    validCount++;
                    break;
                }
            }
        }

        return validCount;
    }

    private static List<List<Integer>> readDatas(List<String> lines){
        List<List<Integer>> datas = new ArrayList<>(lines.size());

        for(int i = 0; i < lines.size(); i++){
            String[] input = lines.get(i).split(" ");
            List<Integer> reportsList = new ArrayList<>();

            for(int j = 0; j < input.length; j++){
                reportsList.add(Integer.parseInt(input[j]));
            }
            datas.add(reportsList);
        }

        return datas;
    }

}