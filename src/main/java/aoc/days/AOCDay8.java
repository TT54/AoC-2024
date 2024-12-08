package aoc.days;

import aoc.utils.DataUtils;
import aoc.utils.Vector2DInt;

import java.util.*;

public class AOCDay8 implements AbstractDay{

    private int width;
    private int height;
    private Map<Character, Set<Vector2DInt>> antennas;
    private Set<Vector2DInt> antiNodes;

    private boolean isInMap(Vector2DInt pos){
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }

    private void printMap(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                System.out.print(antiNodes.contains(new Vector2DInt(i, j)) ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public int getPart1Result(String dataPath) {
        this.readDatas(dataPath);

        for(char frequency : antennas.keySet()){
            Set<Vector2DInt> similarAntennas = antennas.get(frequency);

            for(Vector2DInt pos : similarAntennas){
                for(Vector2DInt pos2 : similarAntennas){
                    if(!pos.equals(pos2)){
                        Vector2DInt dist = pos2.clone().subtract(pos);
                        Vector2DInt antiNodeCandidate = pos.clone().add(dist).add(dist);
                        if (isInMap(antiNodeCandidate)) {
                            antiNodes.add(antiNodeCandidate);
                        }
                    }
                }
            }
        }

        return antiNodes.size();
    }

    @Override
    public int getPart2Result(String dataPath) {
        this.readDatas(dataPath);

        for(char frequency : antennas.keySet()){
            Set<Vector2DInt> similarAntennas = antennas.get(frequency);

            for(Vector2DInt pos : similarAntennas){
                for(Vector2DInt pos2 : similarAntennas){
                    if(!pos.equals(pos2)){
                        Vector2DInt dist = pos2.clone().subtract(pos);
                        Vector2DInt antiNodeCandidate = pos.clone().add(dist);

                        while(isInMap(antiNodeCandidate)){
                            antiNodes.add(antiNodeCandidate);
                            antiNodeCandidate = antiNodeCandidate.clone().add(dist);
                        }
                    }
                }
            }
        }

        return antiNodes.size();
    }

    @Override
    public void readDatas(String dataPath) {
        List<String> lines = DataUtils.readResourceFile(dataPath);
        height = lines.size();
        width = lines.get(0).length();
        antennas = new HashMap<>();
        antiNodes = new HashSet<>();

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                char antennaFrequency = lines.get(i).charAt(j);
                if(antennaFrequency == '.') continue;
                Set<Vector2DInt> similarAntennas = antennas.getOrDefault(antennaFrequency, new HashSet<>());
                similarAntennas.add(new Vector2DInt(i, j));
                antennas.put(antennaFrequency, similarAntennas);
            }
        }
    }

    @Override
    public void showResults() {
        System.out.println("Part 1 example result is " + getPart1Result("/day8/exampleData1"));
        System.out.println("Part 1 result is " + getPart1Result("/day8/data1"));

        System.out.println("Part 2 example result is " + getPart2Result("/day8/exampleData1"));
        System.out.println("Part 2 result is " + getPart2Result("/day8/data1"));
    }
}
