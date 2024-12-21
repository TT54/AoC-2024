package aoc.days;

import aoc.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class AOCDay21 implements AbstractDay {

    private static final int[][] directions = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static final RobotAction[] directionActions = new RobotAction[] {RobotAction.DOWN, RobotAction.RIGHT, RobotAction.UP, RobotAction.LEFT};

    private List<String> codes;
    private int directionalRobotsAmount;

    private long[][] actionsCost;
    private final List<List<RobotAction>>[] bestActions = new List[25];

    private int getCodeNumericPart(String code){
        return Integer.parseInt(code.replace("A", ""));
    }

    private boolean isValidNumericKeypadPosition(int x, int y){
        return x >= 0 && y >= 0 && x < 4 && y < 3 && !(x == 3 && y == 0);
    }

    private boolean isValidDirectionalKeypadPosition(int x, int y){
        return x >= 0 && y >= 0 && x < 2 && y < 3 && !(x == 0 && y == 0);
    }

    private List<List<RobotAction>> getBestKeypadActions(int initialX, int initialY, int targetX, int targetY, int depthLeft, boolean numericalKeypad){
        if(initialX == targetX && initialY == targetY) return List.of(new ArrayList<>(List.of(RobotAction.ACTIVATE)));

        List<List<RobotAction>> candidates = new ArrayList<>();
        if(depthLeft == 0) return candidates;

        for(int i = 0; i < directions.length; i++){
            int x = initialX + directions[i][0];
            int y = initialY + directions[i][1];
            if((Math.abs(targetX - x) + Math.abs(targetY - y) < Math.abs(targetX - initialX) + Math.abs(targetY - initialY)) && (numericalKeypad ? isValidNumericKeypadPosition(x, y) : isValidDirectionalKeypadPosition(x, y))){
                for(List<RobotAction> actions : getBestKeypadActions(x, y, targetX, targetY, depthLeft - 1, numericalKeypad)){
                    if(!actions.isEmpty()) {
                        actions.add(directionActions[i]);
                        candidates.add(actions);
                    }
                }
            }
        }

        return candidates;
    }

    private long getActionCost(RobotAction previousAction, RobotAction action, int depth){
        if(depth == 0) return 1;

        int index = previousAction.ordinal() * 5 + action.ordinal();
        if(actionsCost[depth - 1][index] != 0){
            return actionsCost[depth - 1][index];
        }

        int x1 = previousAction.keypadX, y1 = previousAction.keypadY;
        int x2 = action.keypadX, y2 = action.keypadY;

        long minCost = Long.MAX_VALUE;

        if(bestActions[index] == null){
            bestActions[index] = getBestKeypadActions(x1, y1, x2, y2, Math.abs(x1 - x2) + Math.abs(y1 - y2), false);
        }

        for(List<RobotAction> robotActions : bestActions[index]){
            long cost = 0;
            robotActions = robotActions.reversed();

            for(int i = 0; i < robotActions.size(); i++){
                long actionCost;
                if(i == 0){
                    actionCost = getActionCost(RobotAction.ACTIVATE, robotActions.get(i), depth - 1);
                } else {
                    actionCost = getActionCost(robotActions.get(i - 1), robotActions.get(i), depth - 1);
                }
                cost += actionCost;
            }

            minCost = Math.min(cost, minCost);
        }

        actionsCost[depth - 1][index] = minCost;
        return minCost;
    }

    private List<List<RobotAction>> getNumericKeyboardActionsCandidates(String code){
        int arm1X = 3, arm1Y = 2;
        List<List<RobotAction>> robot1Actions = new ArrayList<>();

        for (int i = 0; i < code.length(); i++) {
            int[] target = getButtonPositionOnNumericKeypad(code.charAt(i));
            List<List<RobotAction>> candidates = getBestKeypadActions(arm1X, arm1Y, target[0], target[1], Math.abs(arm1X - target[0]) + Math.abs(arm1Y - target[1]), true);
            arm1X = target[0];
            arm1Y = target[1];

            if (i == 0) {
                for (List<RobotAction> actions : candidates) {
                    robot1Actions.add(actions.reversed());
                }
            } else {
                for (List<RobotAction> alreadyPerformedActions : new ArrayList<>(robot1Actions)) {
                    List<List<RobotAction>> toAdd = new ArrayList<>();

                    for (List<RobotAction> actions : candidates) {
                        List<RobotAction> actionsToPerform = new ArrayList<>(alreadyPerformedActions);
                        actionsToPerform.addAll(actions.reversed());
                        toAdd.add(actionsToPerform);
                    }

                    robot1Actions.remove(alreadyPerformedActions);
                    robot1Actions.addAll(toAdd);
                }
            }
        }

        int minSize = robot1Actions.getFirst().size();
        for(List<RobotAction> actions : robot1Actions){
            if(actions.size() < minSize) minSize = actions.size();
        }
        final int finalSize = minSize;
        return robot1Actions.stream().filter(actions -> actions.size() == finalSize).toList();
    }

    private long getSolution(){
        long count = 0;
        for(String code : codes){
            List<List<RobotAction>> candidates = getNumericKeyboardActionsCandidates(code);

            long minCost = Long.MAX_VALUE;
            for(List<RobotAction> actions : candidates){
                long cost = 0;
                for(int i = 0; i < actions.size(); i++){
                    if(i == 0){
                        cost += getActionCost(RobotAction.ACTIVATE, actions.get(i), directionalRobotsAmount);
                    } else {
                        cost += getActionCost(actions.get(i - 1), actions.get(i), directionalRobotsAmount);
                    }
                }

                minCost = Math.min(cost, minCost);
            }

            count += minCost * getCodeNumericPart(code);
        }
        return count;
    }

    public int[] getButtonPositionOnNumericKeypad(char c){
        return switch (c) {
            case 'A' -> new int[]{3, 2};
            case '0' -> new int[]{3, 1};
            default -> {
                int i = c - '0';
                yield new int[]{2 - (i - 1) / 3, (i - 1) % 3};
            }
        };
    }

    @Override
    public int getPart1Result(String dataPath){
        this.directionalRobotsAmount = 2;
        readDatas(dataPath);
        return (int) getSolution();
    }


    @Override
    public int getPart2Result(String dataPath){
        this.directionalRobotsAmount = 25;
        readDatas(dataPath);
        System.out.println("True result: " + getSolution());
        return (int) getSolution();
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.actionsCost = new long[directionalRobotsAmount][25];
        this.codes = new ArrayList<>();
        codes.addAll(lines);
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day21/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day21/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day21/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day21/data1"));
    }

    private enum RobotAction{

        UP(0, 1),
        LEFT(1, 0),
        DOWN(1, 1),
        RIGHT(1, 2),
        ACTIVATE(0, 2);

        private final int keypadX;
        private final int keypadY;

        RobotAction(int keypadX, int keypadY) {
            this.keypadX = keypadX;
            this.keypadY = keypadY;
        }
    }
}
