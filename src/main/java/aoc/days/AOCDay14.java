package aoc.days;

import aoc.utils.DataUtils;
import fr.ttgraphiclib.GraphicManager;
import fr.ttgraphiclib.graphics.GraphicPanel;
import fr.ttgraphiclib.graphics.nodes.GraphicNode;
import fr.ttgraphiclib.graphics.nodes.RectangleNode;
import fr.ttgraphiclib.thread.Frame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AOCDay14 implements AbstractDay{

    private boolean exampleData;
    private int width;
    private int height;
    private List<Robot> robots;
    private int[][] bathroom;

    private void moveRobot(Robot robot){
        bathroom[robot.y][robot.x]--;
        robot.x = (width + robot.x + robot.velocityX) % width;
        robot.y = (height + robot.y + robot.velocityY) % height;
        bathroom[robot.y][robot.x]++;
    }

    private void moveRobots(){
        for (Robot robot : robots){
            moveRobot(robot);
        }
    }

    private void printBoard(GraphicPanel panel){
        for(GraphicNode node : panel.getNodes()) {
            panel.removeNode(node);
        }

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                panel.addNode(new RectangleNode(panel, j * 5 - 250, i * 5 - 250, 5, 5, bathroom[i][j] == 0 ? Color.WHITE : Color.GREEN));
            }
        }
    }

    private boolean checkTreeCandidate(){
        for(Robot robot : robots){
            boolean tree = true;
            for(int i = 1; i <= 3; i++){
                for(int k = 0; k <= i; k++){
                    if(bathroom[(height + robot.y + i) % height][(width + robot.x + k) % width] == 0) {
                        tree = false;
                    }
                    if(bathroom[(height + robot.y + i) % height][(width + robot.x - k) % width] == 0) {
                        tree = false;
                    }

                    if(!tree) break;
                }

                if(!tree) break;
            }

            if(tree) return tree;
        }
        return false;
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        for(int i = 0; i < 100; i++){
            moveRobots();
        }

        int[] quadrants = new int[4];
        for(Robot robot : robots){
            if(robot.x < width / 2 && robot.y < height / 2) quadrants[0]++;
            else if(robot.x < width / 2 && robot.y > height / 2) quadrants[1]++;
            else if(robot.x > width / 2 && robot.y < height / 2) quadrants[2]++;
            else if(robot.x > width / 2 && robot.y > height / 2) quadrants[3]++;
        }

        int count = quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];

        return count;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        Frame frame = new Frame("Robots", 800, 800);
        GraphicPanel panel = new GraphicPanel();
        GraphicManager.enable(frame, panel);

        int count = 0;
        while(true){
            if(checkTreeCandidate()){
                System.out.println("Candidate found after " + count + " seconds !");
                printBoard(panel);
                return count;
            }
            moveRobots();
            count++;
        }
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        robots = new ArrayList<>();
        if(this.exampleData){
            this.width = 11; this.height = 7;
        } else {
            this.width = 101; this.height = 103;
        }
        bathroom = new int[height][width];

        for(String line : lines){
            int x = Integer.parseInt(line.split(" ")[0].substring(2).split(",")[0]);
            int y = Integer.parseInt(line.split(" ")[0].substring(2).split(",")[1]);
            int velocityX = Integer.parseInt(line.split(" ")[1].substring(2).split(",")[0]);
            int velocityY = Integer.parseInt(line.split(" ")[1].substring(2).split(",")[1]);
            this.robots.add(new Robot(x, y, velocityX, velocityY));
            this.bathroom[y][x]++;
        }
    }

    @Override
    public void showResults() {
        this.exampleData = true;
        System.out.println("Part 1 example result is " + getPart1Result("/day14/exampleData1"));
        this.exampleData = false;
        System.out.println("Part 1 result is " + getPart1Result("/day14/data1"));

        this.exampleData = false;
        System.out.println("Part 2 result is " + getPart2Result("/day14/data1"));
    }

    private static class Robot{

        public int x;
        public int y;
        public int velocityX;
        public int velocityY;

        public Robot(int x, int y, int velocityX, int velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }
}
