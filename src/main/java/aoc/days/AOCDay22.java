package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.text.DecimalFormat;
import java.util.*;

public class AOCDay22 implements AbstractDay {

    private List<Long> initialSecretNumbers;
    private int[][] monkeyChanges;
    private int[][] monkeyPrices;
    private Map<Integer, Integer>[] firstChangeOccurrence;

    private long getNextSecretNumber(long previousNumber){
        long nextNumber = previousNumber * 64;
        nextNumber ^= previousNumber;
        nextNumber %= 16777216;

        previousNumber = nextNumber;
        nextNumber /= 32;
        nextNumber ^= previousNumber;
        nextNumber %= 16777216;

        previousNumber = nextNumber;
        nextNumber *= 2048;
        nextNumber ^= previousNumber;
        nextNumber %= 16777216;

        return nextNumber;
    }

    private int getIndex(int change1, int change2, int change3, int change4){
        return (change1 + 9) * 20 * 20 * 20 + (change2 + 9) * 20 * 20 + (change3 + 9) * 20 + (change4 + 9);
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        long count = 0;
        for(long secretNumber : initialSecretNumbers){
            for(int i = 0; i < 2000; i++){
                secretNumber = getNextSecretNumber(secretNumber);
            }
            count += secretNumber;
        }

        System.out.println("True value: " + count);
        return (int) count;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        for(int i = 0; i < initialSecretNumbers.size(); i++){
            long secretNumber = initialSecretNumbers.get(i);
            monkeyPrices[i][0] = (int) (secretNumber % 10);
            for(int j = 0; j < 2000; j++){
                secretNumber = getNextSecretNumber(secretNumber);
                monkeyPrices[i][j+1] = (int) (secretNumber % 10);
            }
        }

        for(int i = 0; i < monkeyPrices.length; i++){
            for(int j = 0; j < 2000; j++){
                this.monkeyChanges[i][j] = monkeyPrices[i][j+1] - monkeyPrices[i][j];
            }
        }

        for(int i = 0; i < monkeyChanges.length; i++){
            for(int j = 0; j < 1997; j++){
                int change1 = monkeyChanges[i][j];
                int change2 = monkeyChanges[i][j+1];
                int change3 = monkeyChanges[i][j+2];
                int change4 = monkeyChanges[i][j+3];

                int index = getIndex(change1, change2, change3, change4);
                if(!firstChangeOccurrence[i].containsKey(index)){
                    firstChangeOccurrence[i].put(index, j);
                }
            }
        }

        int bestBananasAmount = 0;
        for(int change1 = -9; change1 <= 9; change1++){
            for(int change2 = -9; change2 <= 9; change2++){
                for(int change3 = -9; change3 <= 9; change3++){
                    for(int change4 = -9; change4 <= 9; change4++){
                        int index = getIndex(change1, change2, change3, change4);
                        int bananasAmount = 0;
                        for(int i = 0; i < this.monkeyPrices.length; i++){
                            if(firstChangeOccurrence[i].containsKey(index)){
                                int firstOccurrence = firstChangeOccurrence[i].get(index);
                                bananasAmount += this.monkeyPrices[i][firstOccurrence + 4];
                            }
                        }
                        bestBananasAmount = Math.max(bestBananasAmount, bananasAmount);
                    }
                }
            }
        }

        return bestBananasAmount;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.monkeyChanges = new int[lines.size()][2000];
        this.monkeyPrices = new int[lines.size()][2001];
        this.firstChangeOccurrence = new Map[20 * 20 * 20 * 20];

        this.initialSecretNumbers = new ArrayList<>();
        for(String line : lines){
            this.initialSecretNumbers.add(Long.parseLong(line));
        }
        for(int i = 0; i < this.firstChangeOccurrence.length; i++){
            this.firstChangeOccurrence[i] = new HashMap<>();
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day22/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day22/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day22/exampleData2"));
        System.out.println("Part 2 result is " + getPart2Result("/day22/data1"));
    }
}
