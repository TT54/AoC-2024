package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay13 implements AbstractDay{

    private List<ClawMachine> machines;
    private List<ClawMachine> machines2;

    private long getSolution(ClawMachine machine){
        double det = machine.xA * machine.yB - machine.xB * machine.yA;
        if(det == 0) return 0;

        double nA = (double) (machine.xPrize * machine.yB - machine.yPrize * machine.xB) / det;
        double nB = (double) (-machine.xPrize * machine.yA + machine.yPrize * machine.xA) / det;

        long resultA = Math.round(nA);
        long resultB = Math.round(nB);

        if(resultA * machine.xA + resultB * machine.xB == machine.xPrize && resultA * machine.yA + resultB * machine.yB == machine.yPrize){
            return 3 * resultA + resultB;
        }
        return 0;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        long count = 0;
        for(ClawMachine machine : this.machines){
            count += getSolution(machine);
        }

        System.out.println(count);
        return (int) count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        long count = 0;
        for(ClawMachine machine : this.machines2){
            count += getSolution(machine);
        }

        System.out.println(count);
        return (int) count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.machines = new ArrayList<>();
        this.machines2 = new ArrayList<>();
        int i = 0;

        int xA = 0;
        int yA = 0;
        int xB = 0;
        int yB = 0;
        long xPrize = 0;
        long yPrize = 0;

        for(String line : lines){
            switch (i){
                case 0:{
                    xA = Integer.parseInt(line.split("X")[1].split(",")[0]);
                    yA = Integer.parseInt(line.split("Y")[1]);
                    break;
                }
                case 1:{
                    xB = Integer.parseInt(line.split("X")[1].split(",")[0]);
                    yB = Integer.parseInt(line.split("Y")[1]);
                    break;
                }
                case 2:{
                    xPrize = Integer.parseInt(line.split("X=")[1].split(",")[0]);
                    yPrize = Integer.parseInt(line.split("Y=")[1]);
                    break;
                }
                default:{
                    this.machines.add(new ClawMachine(xA, yA, xB, yB, xPrize, yPrize));
                    this.machines2.add(new ClawMachine(xA, yA, xB, yB, xPrize + 10000000000000L, yPrize + 10000000000000L));
                    break;
                }
            }
            i = (i + 1) % 4;
        }

        this.machines.add(new ClawMachine(xA, yA, xB, yB, xPrize, yPrize));
        this.machines2.add(new ClawMachine(xA, yA, xB, yB, xPrize + 10000000000000L, yPrize + 10000000000000L));
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day13/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day13/data1"));

        System.out.println("Part 2 example 1 result is " + getPart2Result("/day13/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day13/data1"));
    }

    private record ClawMachine(int xA, int yA, int xB, int yB, long xPrize, long yPrize){

    }
}
