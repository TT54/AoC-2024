package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay2 implements AbstractDay {

    private List<List<Integer>> reports;

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day2/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day2/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day2/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day2/data1"));
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

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        int validCount = 0;
        for(List<Integer> report : reports){
            if(isValid(report)){
                validCount++;
            }
        }

        return validCount;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        int validCount = 0;
        for(List<Integer> report : this.reports){
            for(int i = 0; i < report.size(); i++){
                List<Integer> reportCopy = new ArrayList<>(report);
                int removed = reportCopy.remove(i);
                if(isValid(reportCopy)){
                    validCount++;
                    break;
                }
            }
        }

        return validCount;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.reports = new ArrayList<>(lines.size());

        for (String line : lines) {
            String[] input = line.split(" ");
            List<Integer> reportsList = new ArrayList<>();

            for (String s : input) {
                reportsList.add(Integer.parseInt(s));
            }
            this.reports.add(reportsList);
        }
    }

}