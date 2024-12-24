package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.text.DecimalFormat;
import java.util.*;

public class AOCDay24 implements AbstractDay {

    private static final DecimalFormat format = new DecimalFormat("00");

    private Set<Calculus> calculusSet;
    private Map<String, Boolean> variables;
    private Map<Pair<String, String>, List<Calculus>> calculusPerVariables;

    private boolean applyOperation(Calculus calculus){
        if(variables.containsKey(calculus.var1) && variables.containsKey(calculus.var2)){
            boolean var1 = variables.get(calculus.var1);
            boolean var2 = variables.get(calculus.var2);
            switch (calculus.operation){
                case OR -> variables.put(calculus.output, var1 | var2);
                case XOR -> variables.put(calculus.output, var1 ^ var2);
                case AND -> variables.put(calculus.output, var1 & var2);
            }
            return true;
        }
        return false;
    }

    private void doAllOperations(){
        while (!calculusSet.isEmpty()){
            calculusSet.removeIf(this::applyOperation);
        }
    }

    private List<Calculus> getCalculusPerVariables(String var1, String var2){
        return calculusPerVariables.getOrDefault(new Pair<>(var1, var2), new ArrayList<>());
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        doAllOperations();
        long count = 0;

        for(int i = 63; i >= 0; i--){
            count *= 2;
            count += variables.getOrDefault("z" + (i >= 10 ? i : "0" + i), false) ? 1 : 0;
        }

        System.out.println("True result: " + count);
        return (int) count;
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        long count = 0;

        // This solution is pseudo-automated: the user still need to switch manually the variables in the data file to continue the execution.
        // The program only give an idea of which variables could be misplaced.

        String previousThirdTerm = "";
        for(int i = 0; i < 45; i++){
            String x = "x" + format.format(i), y = "y" + format.format(i), z = "z" + format.format(i);

            if(i == 0){
                for(Calculus calculus : getCalculusPerVariables(x, y)){
                    if(calculus.operation == Operation.AND){
                        previousThirdTerm = calculus.output;
                    } else if(calculus.operation == Operation.XOR){
                        if(!calculus.output.equalsIgnoreCase(z)){
                            System.out.println("Should switch " + calculus.output + " and " + z);
                        }
                    }
                }
            } else {
                String xyXOR = "";
                String xyAND = "";
                String secondAND = "";

                for(Calculus calculus : getCalculusPerVariables(x, y)){
                    if(calculus.operation == Operation.AND){
                        xyAND = calculus.output;
                    } else if(calculus.operation == Operation.XOR){
                        xyXOR = calculus.output;
                    }
                }

                if(xyXOR.isEmpty()){
                    System.out.println("Missing " + x + " XOR " + y);
                }

                if(!xyXOR.isEmpty()) {
                    for (Calculus calculus : getCalculusPerVariables(previousThirdTerm, xyXOR)){
                        if(calculus.operation == Operation.AND){
                            secondAND = calculus.output;
                        } else if(calculus.operation == Operation.XOR){
                            if(!calculus.output.equalsIgnoreCase(z)){
                                System.out.println("Should switch " + calculus.output + " and " + z);
                            }
                        }
                    }
                }

                if(xyAND.isEmpty()){
                    System.out.println("Missing " + x + " AND " + y);
                }

                if(secondAND.isEmpty()){
                    System.out.println(xyXOR + " is " + x + " XOR " + y);
                    System.out.println(previousThirdTerm + " was previous third term");
                    System.out.println("Missing " + xyXOR + " AND " + previousThirdTerm);
                }

                if(!secondAND.isEmpty() && !xyAND.isEmpty()){
                    for(Calculus calculus : getCalculusPerVariables(xyAND, secondAND)){
                        if(calculus.operation != Operation.OR){
                            System.out.println("Error with " + xyAND + " OR " + secondAND);
                        } else {
                            previousThirdTerm = calculus.output;
                        }
                    }
                }
            }
        }

        return (int) count;
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        calculusSet = new HashSet<>();
        calculusPerVariables = new HashMap<>();
        variables = new HashMap<>();

        boolean operations = false;
        for(String line : lines){
            if(line.isEmpty()){
                operations = true;
                continue;
            }

            if(operations){
                String[] split = line.split(" ");
                Calculus calculus = new Calculus(split[0], split[2], Operation.valueOf(split[1]), split[4]);

                calculusSet.add(calculus);

                Pair<String, String> pair = new Pair<>(calculus.var1, calculus.var2);
                Pair<String, String> pair2 = new Pair<>(calculus.var2, calculus.var1);

                List<Calculus> calculusList = calculusPerVariables.getOrDefault(pair, new ArrayList<>());
                calculusList.add(calculus);
                calculusPerVariables.put(pair, calculusList);

                calculusList = calculusPerVariables.getOrDefault(pair2, new ArrayList<>());
                calculusList.add(calculus);
                calculusPerVariables.put(pair2, calculusList);
            } else {
                String[] split = line.split(":");
                variables.put(split[0], Integer.parseInt(split[1].substring(1)) == 1);
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day24/exampleData1"));
        System.out.println("Part 1 example result is " + getPart1Result("/day24/exampleData2"));
        System.out.println("Part 1 result is " + getPart1Result("/day24/data1"));

        //System.out.println("Part 2 example result is " + getPart2Result("/day24/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day24/data2"));
    }

    private enum Operation{
        AND,
        OR,
        XOR
    }

    private record Calculus(String var1, String var2, Operation operation, String output){
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Calculus calculus = (Calculus) object;
            return Objects.equals(var1, calculus.var1) && Objects.equals(var2, calculus.var2) && Objects.equals(output, calculus.output) && operation == calculus.operation;
        }

        @Override
        public int hashCode() {
            return Objects.hash(var1, var2, operation, output);
        }
    }
}
