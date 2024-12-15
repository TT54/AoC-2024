package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay15 implements AbstractDay {

    private List<Move> moves;
    private int[][] warehouse;
    private int robotX;
    private int robotY;
    private boolean isPart2 = false;

    private boolean moveObject(int x, int y, Move move){
        int nextX = x + move.x;
        int nextY = y + move.y;

        if(warehouse[nextX][nextY] == 0){
            if(warehouse[x][y] == 10){
                robotX = nextX;
                robotY = nextY;
            }
            warehouse[nextX][nextY] = warehouse[x][y];
            warehouse[x][y] = 0;
            return true;
        }

        if(warehouse[nextX][nextY] == 1 && moveObject(nextX, nextY, move)){
            if(warehouse[x][y] == 10){
                robotX = nextX;
                robotY = nextY;
            }
            warehouse[nextX][nextY] = warehouse[x][y];
            warehouse[x][y] = 0;
            return true;
        }

        return false;
    }

    private boolean canMoveObject2(int x, int y, Move move, boolean checkOtherBoxSide){
        int nextX = x + move.x;
        int nextY = y + move.y;

        int currentObject = warehouse[x][y];
        int nextObject = warehouse[nextX][nextY];

        if(nextObject == -1) return false;

        if(move.isMovingVertically){
            if(checkOtherBoxSide) {
                if (currentObject == 1) {
                    // Check right box
                    if (!canMoveObject2(x, y + 1, move, false)) return false;
                } else if (currentObject == 2) {
                    // Check left box
                    if (!canMoveObject2(x, y - 1, move, false)) return false;
                }
            }
        }

        if(nextObject == 0) return true;
        return canMoveObject2(nextX, nextY, move, true);
    }

    private void moveObject2(int x, int y, Move move, boolean moveOtherBoxSide){
        int nextX = x + move.x;
        int nextY = y + move.y;

        int currentObject = warehouse[x][y];

        if(move.isMovingVertically){
            if(moveOtherBoxSide) {
                if (currentObject == 1) {
                    // Check right box
                    moveObject2(x, y + 1, move, false);
                } else if (currentObject == 2) {
                    // Check left box
                    moveObject2(x, y - 1, move, false);
                }
            }
        }

        int nextObject = warehouse[nextX][nextY];
        if(nextObject == 1 || nextObject == 2) {
            moveObject2(nextX, nextY, move, true);
        }

        warehouse[nextX][nextY] = currentObject;
        warehouse[x][y] = 0;

        if(currentObject == 10){
            robotX = nextX;
            robotY = nextY;
        }
    }

    private void printWarehouse(){
        for(int x = 0; x < warehouse.length; x++){
            for(int y = 0; y < warehouse[0].length; y++){
                System.out.print(warehouse[x][y] == -1 ? "#" : (warehouse[x][y] == 0 ? "." : (warehouse[x][y] == 1 ? "O" : "@")));
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printWarehouse2(){
        for(int x = 0; x < warehouse.length; x++){
            for(int y = 0; y < warehouse[0].length; y++){
                System.out.print(warehouse[x][y] == -1 ? "#" : (warehouse[x][y] == 0 ? "." : (warehouse[x][y] == 1 ? "[" : (warehouse[x][y] == 2 ? "]" : "@"))));
            }
            System.out.println();
        }
        System.out.println();
    }

    private int calculateGPS(){
        int count = 0;
        for(int x = 0; x < warehouse.length; x++){
            for(int y = 0; y < warehouse[0].length; y++){
                if(warehouse[x][y] == 1){
                    count += x * 100 + y;
                }
            }
        }
        return count;
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        for(Move move : moves){
            moveObject(robotX, robotY, move);
        }
        printWarehouse();

        return calculateGPS();
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        for(Move move : moves){
            //printWarehouse2();
            if(canMoveObject2(robotX, robotY, move, true)){
                //System.out.println("Doing move " + move.name());
                moveObject2(robotX, robotY, move, true);
            }
        }
        printWarehouse2();

        return calculateGPS();
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        if(this.isPart2){
            this.warehouse = new int[lines.size()][lines.getFirst().length() * 2];
        } else {
            this.warehouse = new int[lines.size()][lines.getFirst().length()];
        }
        this.moves = new ArrayList<>();
        boolean movesData = false;

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);

            if(!movesData){
                for(int j = 0; j < line.length(); j++){
                    if(isPart2){
                        switch (line.charAt(j)){
                            case '#': {
                                this.warehouse[i][2 * j] = -1;
                                this.warehouse[i][2 * j + 1] = -1;
                                break;
                            }
                            case 'O': {
                                this.warehouse[i][2 * j] = 1;
                                this.warehouse[i][2 * j + 1] = 2;
                                break;
                            }
                            case '@': {
                                this.robotX = i;
                                this.robotY = 2 * j;
                                this.warehouse[i][2 * j] = 10;
                                break;
                            }
                        }
                    } else {
                        switch (line.charAt(j)){
                            case '#': {
                                this.warehouse[i][j] = -1; break;
                            }
                            case 'O': {
                                this.warehouse[i][j] = 1; break;
                            }
                            case '@': {
                                this.robotX = i; this.robotY = j; this.warehouse[i][j] = 10; break;
                            }
                        }
                    }
                }
            } else {
                for(int j = 0; j < line.length(); j++) {
                    this.moves.add(Move.getMove(line.charAt(j)));
                }
            }

            if(line.isEmpty()){
                movesData = true;
            }
        }

    }

    @Override
    public void showResults() {
        //System.out.println("Part 1 example result is " + getPart1Result("/day15/exampleData1"));
        //System.out.println("Part 1 example result is " + getPart1Result("/day15/exampleData2"));
        //System.out.println("Part 1 result is " + getPart1Result("/day15/data1"));

        this.isPart2 = true;
        System.out.println("Part 2 example result is " + getPart2Result("/day15/exampleData3"));
        System.out.println("Part 2 example result is " + getPart2Result("/day15/exampleData2"));
        System.out.println("Part 2 result is " + getPart2Result("/day15/data1"));
    }

    private enum Move{

        UP(-1, 0, '^', true),
        LEFT(0, -1, '<', false),
        DOWN(1, 0, 'v', true),
        RIGHT(0, 1, '>', false);

        private final int x;
        private final int y;
        private final char symbol;
        private final boolean isMovingVertically;

        Move(int x, int y, char symbol, boolean isMovingVertically) {
            this.x = x;
            this.y = y;
            this.symbol = symbol;
            this.isMovingVertically = isMovingVertically;
        }

        private static final Map<Character, Move> movesMap = new HashMap<>();

        static {
            for(Move move : values()) {
                movesMap.put(move.symbol, move);
            }
        }

        public static Move getMove(char symbol){
            return movesMap.get(symbol);
        }
    }
}
