package aoc.days;

import aoc.utils.DataUtils;

import java.util.List;

public class AOCDay4 implements AbstractDay{

    private List<String> input;
    private final int[][] searchDirections = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
    private final char[] searchedChars = new char[] {'X', 'M', 'A', 'S'};
    private final int[][] simplifiedSearchDirections = new int[][]{{1, 1}, {1, -1}};

    private int countXMAS(int row, int column){
        int count = 0;
        for(int[] direction : searchDirections) {
            int currentRow = row, currentColumn = column;
            for (char c : searchedChars) {
                if(currentRow < 0 || currentColumn < 0 || currentRow >= input.size() || currentColumn >= input.get(currentRow).length()
                        || input.get(currentRow).charAt(currentColumn) != c){
                    break;
                }

                if(c == 'S') count++;
                currentRow += direction[0];
                currentColumn += direction[1];
            }
        }
        return count;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        int count = 0;
        for(int i = 0; i < input.size(); i++){
            for(int j = 0; j < input.get(i).length(); j++){
                count += countXMAS(i, j);
            }
        }

        return count;
    }

    public boolean isThereX_MAS(int row, int column){
        try {
            if(input.get(row).charAt(column) != 'A') return false;
            int masFound = 0;
            for(int[] direction : simplifiedSearchDirections){
                if(input.get(row + direction[0]).charAt(column + direction[1]) == 'M' && input.get(row - direction[0]).charAt(column - direction[1]) == 'S'
                        || input.get(row + direction[0]).charAt(column + direction[1]) == 'S' && input.get(row - direction[0]).charAt(column - direction[1]) == 'M'){
                    masFound++;
                }
            }
            return masFound == 2;
        } catch (IndexOutOfBoundsException ignored){
            return false;
        }
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        int count = 0;
        for(int i = 0; i < input.size(); i++){
            for(int j = 0; j < input.get(i).length(); j++){
                if(isThereX_MAS(i, j)) count++;
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
