package aoc.days;

import aoc.utils.DataUtils;

import java.util.*;

public class AOCDay17 implements AbstractDay {

    private long registerA;
    private long registerB;
    private long registerC;
    private List<Integer> instructions;
    private List<Integer> output;
    private int instructionPointer;

    private long getComboOperandValue(long operand){
        if(operand <= 3) return operand;
        return operand == 4 ? this.registerA : (operand == 5 ? this.registerB : (operand == 6 ? this.registerC : -1));
    }

    private void printOutput(){
        String str = "";
        for(int i : output) str += "," + i;
        System.out.println(str.substring(1));
    }

    private boolean executeInstruction(){
        if(instructionPointer + 1 >= instructions.size()) return false;

        int opcode = instructions.get(instructionPointer);
        int operand = instructions.get(instructionPointer + 1);

        switch (opcode){
            case 0:{
                this.registerA = (long) (this.registerA / (Math.pow(2, getComboOperandValue(operand))));
                break;
            }
            case 1:{
                this.registerB ^= operand;
                break;
            }
            case 2:{
                this.registerB = getComboOperandValue(operand) % 8;
                break;
            }
            case 3:{
                if(this.registerA == 0) break;
                this.instructionPointer = operand;
                return true;
            }
            case 4:{
                this.registerB ^= this.registerC;
                break;
            }
            case 5:{
                this.output.add((int) (getComboOperandValue(operand) % 8));
                break;
            }
            case 6:{
                this.registerB = (long) (this.registerA / (Math.pow(2, getComboOperandValue(operand))));
                break;
            }
            case 7:{
                this.registerC = (long) (this.registerA / (Math.pow(2, getComboOperandValue(operand))));
                break;
            }
        }

        this.instructionPointer += 2;
        return true;
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        while(executeInstruction()){}
        printOutput();

        return 0;
    }

    private void readInstructionsWithRegisterA(long registerA){
        this.registerA = registerA;
        this.instructionPointer = 0;
        this.output.clear();

        while(executeInstruction()){}

        printOutput();
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        List<Long> registerACandidates = new ArrayList<>();
        registerACandidates.add(0L);

        for(int i = 0; i < 16; i++) {
            List<Long> newCandidates = new ArrayList<>();
            for(long candidate : registerACandidates){
                for (int a = 0; a < 8; a++) {
                    long A = candidate + a;
                    long B = a;
                    B ^= 5;
                    long C = A / (int) (Math.pow(2, B));
                    B ^= 6;
                    B ^= C;
                    B %= 8;

                    if(B == instructions.get(15 - i)){
                        newCandidates.add(A * 8);
                    }
                }
            }
            registerACandidates = newCandidates;
        }

        long minA = Long.MAX_VALUE;
        for(long a : registerACandidates){
            if(a / 8 <= minA) minA = a / 8;
        }
        System.out.println(minA);
        readInstructionsWithRegisterA(minA);

        // Not the right value
        return 0;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        instructionPointer = 0;
        instructions = new ArrayList<>();
        output = new ArrayList<>();

        this.registerA = Integer.parseInt(lines.get(0).split(" ")[2]);
        this.registerB = Integer.parseInt(lines.get(1).split(" ")[2]);
        this.registerC = Integer.parseInt(lines.get(2).split(" ")[2]);

        this.instructions.addAll(Arrays.stream(lines.get(4).split(" ")[1].split(",")).map(Integer::parseInt).toList());
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day17/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day17/data1"));

        System.out.println("Part 2 result is " + getPart2Result("/day17/data1"));
    }
}
