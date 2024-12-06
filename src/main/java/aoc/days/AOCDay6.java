package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.*;

public class AOCDay6 implements AbstractDay{

    private static final int[][] directions = new int[][] {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int[][] room;
    private int guardRow;
    private int guardColumn;
    private int directionIndex;
    private Set<Pair<Integer, Integer>> visitedSquares;

    private boolean isInRoom(int row, int column){
        return row >= 0 && column >= 0 && row < room.length && column < room[0].length;
    }

    private boolean doesGuardLoop(int guardRow, int guardColumn){
        int[][][] visitedSquares = new int[room.length][room[0].length][4];
        int directionIndex = 0;

        while(isInRoom(guardRow, guardColumn)){
            if(visitedSquares[guardRow][guardColumn][directionIndex] == 1) {
                return true;
            }
            visitedSquares[guardRow][guardColumn][directionIndex] = 1;

            int newRow = guardRow + directions[directionIndex][0];
            int newColumn = guardColumn + directions[directionIndex][1];

            if(isInRoom(newRow, newColumn) && room[newRow][newColumn] == 1){
                directionIndex = (directionIndex + 1) % 4;
            } else{
                guardRow = newRow;
                guardColumn = newColumn;
            }
        }
        return false;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        while(isInRoom(guardRow, guardColumn)){
            visitedSquares.add(new Pair<>(this.guardRow, this.guardColumn));
            int newRow = guardRow + directions[directionIndex][0];
            int newColumn = guardColumn + directions[directionIndex][1];

            if(isInRoom(newRow, newColumn) && this.room[newRow][newColumn] == 1){
                this.directionIndex = (this.directionIndex + 1) % 4;
            } else{
                this.guardRow = newRow;
                this.guardColumn = newColumn;
            }
        }

        return this.visitedSquares.size();
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        int count = 0;
        for(int i = 0; i < this.room.length; i++){
            for(int j = 0; j < this.room[0].length; j++){
                if(this.room[i][j] == 0 && (i != this.guardRow || j != this.guardColumn)){
                    this.room[i][j] = 1;

                    if(doesGuardLoop(this.guardRow, this.guardColumn)) {
                        count++;
                    }

                    this.room[i][j] = 0;
                }
            }
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.visitedSquares = new HashSet<>();
        this.room = new int[lines.size()][lines.getFirst().length()];

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            for(int j = 0; j < line.length(); j++){
                switch (line.charAt(j)) {
                    case '.': this.room[i][j] = 0; break;
                    case '^': this.guardRow = i; this.guardColumn = j; this.directionIndex = 0; break;
                    case '#': this.room[i][j] = 1;
                }
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day6/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day6/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day6/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day6/data1"));
    }
}
