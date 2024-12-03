package aoc.days;

import aoc.utils.DataUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AOCDay3 implements AbstractDay{

    private String input = "";

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        Pattern pattern = Pattern.compile("mul\\((\\d|\\d\\d|\\d\\d\\d),(\\d|\\d\\d|\\d\\d\\d)\\)");
        Matcher matcher = pattern.matcher(this.input);

        int count = 0;
        while(matcher.find()){
            String instruction = this.input.substring(matcher.start(), matcher.end());
            String[] numbers = instruction.split("\\(")[1].split("\\)")[0].split(",");
            count += Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        Pattern pattern = Pattern.compile("(mul\\((\\d|\\d\\d|\\d\\d\\d),(\\d|\\d\\d|\\d\\d\\d)\\))|(do\\(\\))|(don't\\(\\))");
        Matcher matcher = pattern.matcher(this.input);

        int count = 0;
        boolean shouldDo = true;
        while(matcher.find()){
            String instruction = this.input.substring(matcher.start(), matcher.end());

            if(instruction.equalsIgnoreCase("do()")){
                shouldDo = true;
            } else if (instruction.equalsIgnoreCase("don't()")) {
                shouldDo = false;
            } else if(shouldDo){
                String[] numbers = instruction.split("\\(")[1].split("\\)")[0].split(",");
                count += Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
            }
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.input = "";
        for(String line : lines){
            input += line + "\n";
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day3/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day3/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day3/exampleData2"));
        System.out.println("Part 2 result is " + getPart2Result("/day3/data1"));
    }
}
