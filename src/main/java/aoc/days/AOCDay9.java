package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Vector2DInt;

import java.util.*;

public class AOCDay9 implements AbstractDay{

    List<Integer> datas;

    private int getFirstEmpty(){
        for(int i = 0; i < datas.size();i++){
            if(datas.get(i) < 0) return i;
        }
        return -1;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        for(int i = 0; i < datas.size(); i++){
            int firstEmpty = getFirstEmpty();
            if(firstEmpty < 0) break;

            int last = datas.removeLast();
            datas.set(firstEmpty, last);
        }

        long count = 0;
        for(int i = 0; i < datas.size(); i++){
            count += datas.get(i) * i;
        }

        System.out.println("True value : " + count);
        return (int) count;
    }

    private int getFirstEmptyBlock(int size, int maxIndex){
        int blockSize = 0;
        for(int i = 0; i <= maxIndex; i++){
            if(blockSize == size) return i - blockSize;
            if(datas.get(i) < 0) {
                blockSize++;
            } else {
                blockSize = 0;
            }
        }
        return -1;
    }

    private void moveBlock(int blockId){
        int size = 0;
        int index = -1;

        for(int j = 0; j < datas.size(); j++){
            if(index < 0 && datas.get(j) == blockId) index = j;
            if(datas.get(j) == blockId) size++;
        }

        int firstBlock = getFirstEmptyBlock(size, index);
        if(firstBlock < 0) return;

        for(int j = 0; j < size; j++){
            datas.set(firstBlock + j, blockId);
            datas.set(index + j, -1);
        }

        while(datas.getLast() == -1)
            datas.removeLast();
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        for(int i = datas.getLast(); i >= 0; i--){
            moveBlock(i);
        }

        long count = 0;
        for(int i = 0; i < datas.size(); i++){
            if(datas.get(i) >= 0) {
                count += datas.get(i) * i;
            }
        }

        System.out.println("True value : " + count);
        return (int) count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        datas = new ArrayList<>();
        int id = 0;
        boolean isEmptySpace = false;

        for(int i = 0; i < lines.getFirst().length(); i++){
            char c = lines.getFirst().charAt(i);
            if(isEmptySpace){
                for(int j = 0; j < c - '0'; j++){
                    datas.add(-1);
                }
            } else {
                for(int j = 0; j < c - '0'; j++){
                    datas.add(id);
                }
                id++;
            }
            isEmptySpace = !isEmptySpace;
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day9/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day9/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day9/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day9/data1"));
    }
}
