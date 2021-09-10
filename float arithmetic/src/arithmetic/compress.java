package arithmetic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class compress {
    static HashMap<String, Float> probs = new HashMap<String, Float>();
    static HashMap<String, Float> probl = new HashMap<String, Float>();
    static HashMap<String, Float> probh = new HashMap<String, Float>();
    static String data;

    public static String readData() throws IOException {
        data = new String(Files.readAllBytes(Paths.get("input.txt")));
        return data;
    }

    public static void readProb() throws IOException {
        BufferedReader line;
        String[] l = null;
        line = new BufferedReader(new FileReader("probs.txt"));
        String p = line.readLine();
        while (p != null)
        {
            l = p.split(" ");
            probs.put(l[0], Float.parseFloat(l[1]));
            p = line.readLine();
        }
        line.close();
    }

    public static void generateRange() throws IOException {
        readProb();
        float add = 0;
        for(String s : probs.keySet())
        {
            probl.put(s, add);
            add += probs.get(s);
            probh.put(s, add);
        }
    }

    public static void write(float result) throws IOException {
        FileWriter out = new FileWriter("output.txt");
        out.write(String.valueOf(result));
        out.flush();
        out.close();
    }

    public static float getProb(float lower, float upper, float prob)
    {
        return lower + (upper - lower) * prob;
    }

    public static float rand(float low, float high)
    {
        return Float.parseFloat(String.valueOf((Math.random() * (high - low)) + low));
    }

    public static float generate(String s) throws IOException
    {
        generateRange();
        char[] split = s.toCharArray();
        float l = 0;
        float h = 1;

        for (char c : split)
        {
            if (probl.containsKey(String.valueOf(c)) && probh.containsKey(String.valueOf(c))) {
                    float temp = getProb(l, h, probl.get(String.valueOf(c)));
                    h = getProb(l, h, probh.get(String.valueOf(c)));
                    l = temp;
            }
        }
        return rand(l, h);
    }


    public static void main(String[] args) throws IOException
    {
        write(generate(readData()));
        System.out.println("Done");

    }

}
