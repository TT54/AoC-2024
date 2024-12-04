package aoc.days;

import aoc.utils.DataUtils;

import javax.swing.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AOCDay4 implements AbstractDay{

    private List<String> input;

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        final int[][] searchDirections = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        final char[] searchedChars = new char[] {'X', 'M', 'A', 'S'};

        int count = 0;
        for(int i = 0; i < input.size(); i++){
            for(int j = 0; j < input.get(i).length(); j++){
                char searchedChar = searchedChars[0];
                char c = input.get(i).charAt(j);

                if(c == searchedChar){
                    for(int[] direction : searchDirections){
                        int row = i;
                        int column = j;
                        boolean found = true;

                        for(int k = 1; k < searchedChars.length; k++){
                            row += direction[0];
                            column += direction[1];

                            if(row < 0 || column < 0 || row >= input.size() || column >= input.get(row).length() || input.get(row).charAt(column) != searchedChars[k]){
                                found = false;
                                break;
                            }
                        }

                        if(found){
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        final int[][] simplifiedSearchDirections = new int[][]{{1, 1}, {1, -1}};

        int count = 0;
        for(int i = 0; i < input.size(); i++){
            for(int j = 0; j < input.get(i).length(); j++){
                char c = input.get(i).charAt(j);

                try {
                    if(c == 'A'){
                        int masFound = 0;
                        for(int[] direction : simplifiedSearchDirections){
                            if(input.get(i + direction[0]).charAt(j + direction[1]) == 'M' && input.get(i - direction[0]).charAt(j - direction[1]) == 'S'
                            || input.get(i + direction[0]).charAt(j + direction[1]) == 'S' && input.get(i - direction[0]).charAt(j - direction[1]) == 'M'){
                                masFound++;
                            }
                        }
                        if(masFound == 2){
                            count++;
                        }
                    }
                } catch (IndexOutOfBoundsException ignored){}
            }
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        this.input = DataUtils.readResourceFile(dataPath);
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day4/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day4/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day4/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day4/data1"));
    }
}
