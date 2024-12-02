package aoc.days;

public interface AbstractDay {

    int getPart1Result(String dataPath);
    int getPart2Result(String dataPath);
    void readDatas(String dataPath);
    void showResults();

}
