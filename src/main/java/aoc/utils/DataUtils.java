package aoc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUtils {

    public static List<String> readResourceFile(String path){
        List<String> lines = new ArrayList<>();
        try {
            InputStream stream = DataUtils.class.getResourceAsStream(path);

            if(stream == null) {
                System.err.println("Resource File " + path + " not found");
                return lines;
            }

            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while((line = bufferedReader.readLine()) != null){
                lines.add(line);
            }

            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

}
