package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Pair;

import java.util.*;

public class AOCDay12 implements AbstractDay{

    private Map<Character, List<Integer>> cropsAreas;
    private Map<Character, List<Integer>> cropsPerimeters;
    private char[][] cropsMap;
    private boolean[][] visited;

    private static final int[][] directions = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    private boolean isInMap(int x, int y){
        return x >= 0 && y >= 0 && x < cropsMap.length && y < cropsMap[x].length;
    }

    private Pair<Integer, Integer> createArea(int x, int y, char c){
        visited[x][y] = true;
        int perimeter = 0;
        int area = 1;

        for(int[] direction : directions){
            int newX = x + direction[0];
            int newY = y + direction[1];

            if(!isInMap(newX, newY) || cropsMap[newX][newY] != c){
                perimeter++;
            } else if(!visited[newX][newY]){
                Pair<Integer, Integer> result = createArea(newX, newY, c);
                perimeter += result.getFirstElement();
                area += result.getSecondElement();
            }
        }

        return new Pair<>(perimeter, area);
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        long count = 0;
        for(int i = 0; i < cropsMap.length; i++){
            for(int j = 0; j < cropsMap[i].length; j++){
                if(!visited[i][j]){
                    char crop = cropsMap[i][j];
                    Pair<Integer, Integer> result = createArea(i, j, crop);

                    List<Integer> perimeters = cropsPerimeters.getOrDefault(crop, new ArrayList<>());
                    perimeters.add(result.getFirstElement());
                    cropsPerimeters.put(crop, perimeters);

                    List<Integer> areas = cropsAreas.getOrDefault(crop, new ArrayList<>());
                    areas.add(result.getSecondElement());
                    cropsAreas.put(crop, areas);
                }
            }
        }

        for(Map.Entry<Character, List<Integer>> entry : cropsPerimeters.entrySet()){
            for(int i = 0; i < entry.getValue().size(); i++){
                count += (long) entry.getValue().get(i) * cropsAreas.get(entry.getKey()).get(i);
            }
        }

        return (int) count;
    }

    private List<Set<Pair<Integer, Integer>>> delimitedCropsAreas;

    private void constructCropArea(int x, int y, char c, Set<Pair<Integer, Integer>> area){
        visited[x][y] = true;
        area.add(new Pair<>(x, y));

        for(int[] direction : directions){
            int newX = x + direction[0];
            int newY = y + direction[1];

            if(isInMap(newX, newY) && cropsMap[newX][newY] == c && !visited[newX][newY]){
                constructCropArea(newX, newY, c, area);
            }
        }
    }

    private boolean hasBorderOnSide(int x, int y, int borderDirection){
        int nextX = x + directions[borderDirection][0];
        int nextY = y + directions[borderDirection][1];
        return !isInMap(nextX, nextY) || this.cropsMap[x][y] != this.cropsMap[nextX][nextY];
    }

    private int countVertices(int x, int y, Set<Pair<Integer, Integer>> region){
        int vertices = 0;
        for(int i = 0; i < directions.length; i++){
            if(hasBorderOnSide(x, y, i % 4)){
                if(hasBorderOnSide(x, y, (i + 1) % 4)){
                    vertices++;
                }

                int neighbourX = x + directions[(i+1) % 4][0];
                int neighbourY = y + directions[(i+1) % 4][1];

                int nextX = neighbourX + directions[i][0];
                int nextY = neighbourY + directions[i][1];

                if(region.contains(new Pair<>(neighbourX, neighbourY)) && region.contains(new Pair<>(nextX, nextY)) &&
                        isInMap(nextX, nextY) && hasBorderOnSide(nextX, nextY, (i + 3) % 4)){
                    vertices++;
                }
            }
        }
        return vertices;
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        // Constructing the areas
        for(int i = 0; i < cropsMap.length; i++){
            for(int j = 0; j < cropsMap[i].length; j++){
                if(!visited[i][j]){
                    char crop = cropsMap[i][j];

                    Set<Pair<Integer, Integer>> region = new HashSet<>();
                    constructCropArea(i, j, crop, region);
                    this.delimitedCropsAreas.add(region);
                }
            }
        }

        int count = 0;

        // Counting the vertices of the region
        for(Set<Pair<Integer, Integer>> region : this.delimitedCropsAreas){
            int vertices = 0;
            for(Pair<Integer, Integer> plot : region){
                int x = plot.getFirstElement();
                int y = plot.getSecondElement();
                vertices += countVertices(x, y, region);
            }

            count += vertices * region.size();
        }

        return count;
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        this.cropsMap = new char[lines.size()][lines.getFirst().length()];
        this.visited = new boolean[lines.size()][lines.getFirst().length()];
        this.cropsAreas = new HashMap<>();
        this.cropsPerimeters = new HashMap<>();
        this.delimitedCropsAreas = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                this.cropsMap[i][j] = lines.get(i).charAt(j);
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day12/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day12/data1"));

        System.out.println("Part 2 example 1 result is " + getPart2Result("/day12/exampleData1"));
        System.out.println("Part 2 example 2 result is " + getPart2Result("/day12/exampleData2"));
        System.out.println("Part 2 example 3 result is " + getPart2Result("/day12/exampleData3"));
        System.out.println("Part 2 example 4 result is " + getPart2Result("/day12/exampleData4"));
        System.out.println("Part 2 result is " + getPart2Result("/day12/data1"));
    }
}
