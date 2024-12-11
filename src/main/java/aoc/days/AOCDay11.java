package aoc.days;

import aoc.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AOCDay11 implements AbstractDay{

    List<Long> stones;
    Map<Long, Long>[] encounteredValues;

    private void changeStonesAfterBlink(){
        List<Long> newStones = new ArrayList<>(stones.size() * 2);
        for(int i = 0; i < stones.size(); i++){
            long stone = stones.get(i);
            String stoneStr = String.valueOf(stone);
            if(stone == 0){
                newStones.add(1L);
            } else if(stoneStr.length() % 2 == 0){
                newStones.add(Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2)));
                newStones.add(Long.parseLong(stoneStr.substring(stoneStr.length() / 2)));
            } else{
                newStones.add(stone * 2024L);
            }
        }
        this.stones = newStones;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        for(int i = 0; i < 25; i++){
            changeStonesAfterBlink();
        }

        return this.stones.size();
    }

    public long changeStone(long stone, int amount){
        if(amount == 0) return 1;

        if(encounteredValues[amount].containsKey(stone)){
            return encounteredValues[amount].get(stone);
        }

        String stoneStr = String.valueOf(stone);
        if(stone == 0){
            long value = changeStone(1, amount - 1);
            encounteredValues[amount].put(stone, value);
            return value;
        } else if(stoneStr.length() % 2 == 0){
            long value = changeStone(Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2)), amount - 1) + changeStone(Long.parseLong(stoneStr.substring(stoneStr.length() / 2)), amount - 1);
            encounteredValues[amount].put(stone, value);
            return value;
        } else{
            long value = changeStone(stone * 2024L, amount - 1);
            encounteredValues[amount].put(stone, value);
            return value;
        }
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);
        long count = 0;

        for(long stone : this.stones){
            count += changeStone(stone, 75);
        }

        System.out.println(count);
        return (int) count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.stones = new ArrayList<>();
        this.encounteredValues = new Map[76];

        for(int i = 0; i < encounteredValues.length; i++){
            this.encounteredValues[i] = new HashMap<>();
        }

        String[] values = lines.getFirst().split(" ");

        for(String stone : values){
            this.stones.add(Long.parseLong(stone));
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day11/exampleData1"));
        long time = System.currentTimeMillis();
        System.out.println("Part 1 result is " + getPart1Result("/day11/data1"));
        System.out.println("Elapsed Time : " + (System.currentTimeMillis() - time));

        System.out.println("Part 2 example result is " + getPart2Result("/day11/exampleData1"));
        time = System.currentTimeMillis();
        System.out.println("Part 2 result is " + getPart2Result("/day11/data1"));
        System.out.println("Elapsed Time : " + (System.currentTimeMillis() - time));
    }
}
