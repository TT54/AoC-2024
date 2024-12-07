package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class AOCDay7 implements AbstractDay{

    private List<Long> equationsResult;
    private List<List<Long>> equationsMembers;

    private boolean canSolve(long result, List<Long> members, int currentIndex, long currentResult, boolean shouldTryConcatenation){
        if(currentIndex == members.size() || currentResult > result) return result == currentResult;

        long currentNumber = members.get(currentIndex);
        if(canSolve(result, members, currentIndex + 1, currentResult * currentNumber, shouldTryConcatenation)
        || (shouldTryConcatenation && canSolve(result, members, currentIndex + 1, Long.parseLong(currentResult + "" + currentNumber), true))){
            return true;
        }
        return canSolve(result, members, currentIndex + 1, currentResult + currentNumber, shouldTryConcatenation);
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        long count = 0;
        for(int i = 0; i < equationsResult.size(); i++){
            if(canSolve(equationsResult.get(i), equationsMembers.get(i), 0, 0, false)){
                count += equationsResult.get(i);
            }
        }

        System.out.println(count);

        // Result not accurate
        return (int) count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        long count = 0;
        for(int i = 0; i < equationsResult.size(); i++){
            if(canSolve(equationsResult.get(i), equationsMembers.get(i), 0, 0, true)){
                count += equationsResult.get(i);
            }
        }

        System.out.println(count);

        // Result not accurate
        return (int) count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        equationsResult = new ArrayList<>();
        equationsMembers = new ArrayList<>();

        for(String line : lines){
            String[] result = line.split(":");
            String[] membersStr = result[1].substring(1).split(" ");
            equationsResult.add(Long.parseLong(result[0]));
            equationsMembers.add(Arrays.stream(membersStr).map(Long::parseLong).toList());
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day7/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day7/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day7/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day7/data1"));
    }
}
