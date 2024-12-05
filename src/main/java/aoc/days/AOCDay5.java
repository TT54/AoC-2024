package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay5 implements AbstractDay{

    private Map<Integer, Set<Integer>> numbersBefore;
    private List<List<Integer>> instructions;

    private boolean isBefore(int before, int number){
        return numbersBefore.getOrDefault(number,new HashSet<>()).contains(before);
    }

    private boolean isInstructionValid(List<Integer> instruction){
        for(int i = 0; i < instruction.size(); i++){
            if(!isNumberWellPlaced(instruction.get(i), i, instruction)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumberWellPlaced(int number, int numberIndex, List<Integer> instruction){
        for(int i = numberIndex + 1; i < instruction.size(); i++){
            if(isBefore(instruction.get(i), number)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        int count = 0;

        for(List<Integer> instruction : instructions){
            if(isInstructionValid(instruction)){
                count += instruction.get(instruction.size() / 2);
            }
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        int count = 0;

        for(List<Integer> instruction : instructions){
            if(!isInstructionValid(instruction)){
                instruction.sort((i1, i2) -> i1 == i2 ? 0 : (isBefore(i1, i2) ? -1 : 1));
                count += instruction.get(instruction.size() / 2);
            }
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        numbersBefore = new HashMap<>();
        instructions = new ArrayList<>();

        boolean readingInstructions = false;

        for(String line : lines){
            if(line.isEmpty()) {
                readingInstructions = true;
                continue;
            }

            if(!readingInstructions){
                String[] split = line.split("\\|");
                int numberBefore = Integer.parseInt(split[0]);
                int numberAfter = Integer.parseInt(split[1]);
                Set<Integer> rule = numbersBefore.getOrDefault(numberAfter, new HashSet<>());
                rule.add(numberBefore);
                numbersBefore.put(numberAfter, rule);
            } else {
                instructions.add(new ArrayList<>(Arrays.stream(line.split(",")).map(Integer::parseInt).toList()));
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day5/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day5/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day5/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day5/data1"));
    }
}
