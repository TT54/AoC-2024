package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;
import fr.ttgraphiclib.GraphicManager;
import fr.ttgraphiclib.graphics.GraphicPanel;
import fr.ttgraphiclib.graphics.nodes.GraphicNode;
import fr.ttgraphiclib.graphics.nodes.RectangleNode;
import fr.ttgraphiclib.thread.Frame;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;

public class AOCDay16 implements AbstractDay {

    private static final DecimalFormat format = new DecimalFormat("00000");
    private static final int[][] directions = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    private int[][] maze;
    int beginX;
    int beginY;
    int endX;
    int endY;

    private Set<Position> nextPoints;
    private Map<Position, Integer> exploredPoints;

    private void printMaze(GraphicPanel panel){
        for (GraphicNode node : panel.getNodes()) {
            panel.removeNode(node);
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                panel.addNode(new RectangleNode(panel, j * 5 - 5 * maze.length / 2, i * 5 - 5 * maze.length / 2, 5, 5, maze[i][j] >= 0 ? new Color(50 + 2 * maze[i][j] / 1000, 0, 0) : Color.BLACK));
            }
        }

        if (maze[endX][endY] > 0) {
            for(Pair<Integer, Integer> pos : getBestPositions()){
                panel.addNode(new RectangleNode(panel, pos.getSecondElement() * 5 - 5 * maze.length / 2, pos.getFirstElement() * 5 - 5 * maze.length / 2, 5, 5, Color.CYAN));
            }
        }
    }

    private Set<Pair<Integer, Integer>> getBestPositions(){
        List<Position> toVisit = new ArrayList<>(exploredPoints.keySet().stream().filter(position -> position.x == endX && position.y == endY).toList());
        Set<Position> visited = new HashSet<>();
        Set<Pair<Integer, Integer>> bestPos = new HashSet<>();
        bestPos.add(new Pair<>(beginX, beginY));

        while(!toVisit.isEmpty()){
            Position position = toVisit.removeLast();
            visited.add(position);
            bestPos.add(new Pair<>(position.x, position.y));

            int previousX = position.x - directions[position.direction][0];
            int previousY = position.y - directions[position.direction][1];

            toVisit.addAll(exploredPoints.keySet().stream().filter(p -> p.x == previousX && p.y == previousY && exploredPoints.get(p)  == position.previousValue && !visited.contains(p)).toList());
        }

        return bestPos;
    }

    private int getCaseCost(Position position) {
        return position.previousValue + (position.previousDirection != position.direction ? 1001 : 1);
    }

    private void doDijkstra(){
        this.maze[beginX][beginY] = 0;

        for(int i = 0; i < 4; i++){
            Position nextCandidate = new Position(beginX + directions[i][0], beginY + directions[i][1], i, 1, 0);
            if(!exploredPoints.containsKey(nextCandidate) && (this.maze[nextCandidate.x][nextCandidate.y] == -1 || this.maze[nextCandidate.x][nextCandidate.y] > 0)){
                this.nextPoints.add(nextCandidate);
            }
        }

        while(this.maze[endX][endY] == -1){
            Position nextPoint = null;
            int nextDistance = -1;
            for(Position pos : nextPoints){
                int cost = getCaseCost(pos);

                if(nextDistance == -1 || nextDistance > cost){
                    nextDistance = cost;
                    nextPoint = pos;
                }
            }

            if(nextPoint != null) {
                nextPoints.remove(nextPoint);
                exploredPoints.put(nextPoint, nextDistance);
                this.maze[nextPoint.x][nextPoint.y] = nextDistance;

                for(int i = 0; i < 4; i++){
                    Position nextCandidate = new Position(nextPoint.x + directions[i][0], nextPoint.y + directions[i][1], i, nextPoint.direction, nextDistance);
                    if(!exploredPoints.containsKey(nextCandidate) && (this.maze[nextCandidate.x][nextCandidate.y] == -1 || this.maze[nextCandidate.x][nextCandidate.y] > 0)){
                        this.nextPoints.add(nextCandidate);
                    }
                }
            } else {
                return;
            }
        }
    }

    @Override
    public int getPart1Result(String dataPath){
        readDatas(dataPath);

        Frame frame = new Frame("Robots", 800, 800);
        GraphicPanel panel = new GraphicPanel();
        GraphicManager.enable(frame, panel);

        doDijkstra();
        printMaze(panel);

        return this.maze[endX][endY];
    }

    @Override
    public int getPart2Result(String dataPath){
        readDatas(dataPath);

        doDijkstra();

        return getBestPositions().size();
    }

    @Override
    public void readDatas(String dataPath){
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.maze = new int[lines.size()][lines.getFirst().length()];
        nextPoints = new HashSet<>();
        exploredPoints = new HashMap<>();

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            for(int j = 0; j < line.length(); j++){
                switch (line.charAt(j)){
                    case '#': {
                        this.maze[i][j] = -10; break;
                    }
                    case 'S': {
                        this.maze[i][j] = -1; this.beginX = i; this.beginY = j; break;
                    }
                    case 'E': {
                        this.maze[i][j] = -1; this.endX = i; this.endY = j; break;
                    }
                    default: {
                        this.maze[i][j] = -1;
                    }
                }
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day16/exampleData1"));
        System.out.println("Part 1 example result is " + getPart1Result("/day16/exampleData2"));
        System.out.println("Part 1 example result is " + getPart1Result("/day16/exampleData3"));
        System.out.println("Part 1 example result is " + getPart1Result("/day16/exampleData4"));
        System.out.println("Part 1 result is " + getPart1Result("/day16/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day16/exampleData1"));
        System.out.println("Part 2 example result is " + getPart2Result("/day16/exampleData2"));
        System.out.println("Part 2 result is " + getPart2Result("/day16/data1"));
    }

    private record Position(int x, int y, int direction, int previousDirection, int previousValue){
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && direction == position.direction && previousDirection == position.previousDirection;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, direction, previousDirection);
        }
    }
}
