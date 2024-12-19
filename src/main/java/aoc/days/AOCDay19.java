package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.*;

public class AOCDay19 implements AbstractDay {

    private List<String> availablePatterns;
    private List<String> goalPatterns;

    private void countPatternPossibilities(String pattern, long[] subPossibilitiesAmount){
        long count = 0;
        for(String candidate : availablePatterns){
            if(pattern.startsWith(candidate)){
                count += subPossibilitiesAmount[pattern.length() - candidate.length()];
            }
        }
        subPossibilitiesAmount[pattern.length()] = count;
    }

    private long countPatternPossibilities(String pattern){
        long[] subPossibilitiesAmount = new long[pattern.length() + 1];
        subPossibilitiesAmount[0] = 1;

        for(int i = pattern.length() - 1; i >= 0; i--){
            countPatternPossibilities(pattern.substring(i), subPossibilitiesAmount);
        }

        return subPossibilitiesAmount[pattern.length()];
    }

    private boolean isPatternPossible(String pattern, List<String> availablePatterns){
        String regex = "(" + availablePatterns.getFirst();
        for(int i = 1; i < availablePatterns.size(); i++){
            regex += "|" + availablePatterns.get(i);
        }
        regex += ")*";

        return pattern.matches(regex);
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        int count = 0;
        for(String pattern : goalPatterns){
            if(isPatternPossible(pattern, this.availablePatterns)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        long count = 0;
        for(String pattern : goalPatterns){
            if(isPatternPossible(pattern, this.availablePatterns)) {
                count += countPatternPossibilities(pattern);
            }
        }

        System.out.println(count);
        return (int) count;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);

        availablePatterns = new ArrayList<>();
        goalPatterns = new ArrayList<>();

        availablePatterns.addAll(Arrays.asList(lines.getFirst().split(", ")));

        for(int i = 2; i < lines.size(); i++){
            goalPatterns.add(lines.get(i));
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day19/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day19/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day19/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day19/data1"));
    }
}
