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

    public static void FinishAndWrite(String code) throws IOException {
        FileWriter out = new FileWriter("output.txt");
        code+="1";
        int getk=0;
        float smallOne=2;
        Boolean b=true;
        for(float f:probs.values())
        {
        	if(b)
        	{
        		smallOne=f;
        		b=false;
        	}
        	if(f<smallOne)
        		smallOne=f;
        	
        }
        smallOne=1/smallOne;
        
        int k=1;
        while(true)
        {
        	getk=(int) Math.pow(2,k);
        	
        	if(getk>=smallOne)
        		break;
        	k++;
        }
       // System.out.println("smallOne: "+k);
        for(int i=0;i<k-1;i++)
        {
        	code+="0";
        }
        out.write(code);
        out.flush();
        out.close();
    }

    public static double getProb(double l, double h, float prob)
    {
        return l + (h - l) * prob;
    }

    public static float rand(double l, double h)
    {
        return Float.parseFloat(String.valueOf((Math.random() * (h - l)) + l));
    }

    public static String generate(String s) throws IOException
    {
        generateRange();
        char[] split = s.toCharArray();
        double l = 0;
        double h = 1;
        String code="";

        for (char c : split)
        {
            if (probl.containsKey(String.valueOf(c)) && probh.containsKey(String.valueOf(c))) {
            		double temp = getProb(l, h, probl.get(String.valueOf(c)));
                    h = getProb(l, h, probh.get(String.valueOf(c)));
                    l = temp;
                   ///scalling
                    while(true)
                    {
                    	if(h<0.5 && l<0.5)
                        {
                        	h*=2;
                        	l*=2;
                        	code+="0";
                        }
                        else if(h>0.5 && l>0.5)
                        {
                        	h=(h-0.5)*2;
                        	l=(l-0.5)*2;
                        	code+="1";
                        }
                        else
                        	break;
                    }
                    //System.out.println("h: "+h+" - "+"l: "+l);
                    //System.out.println(scale);

                    
            }
            
        }
        return code;
    }


    public static void main(String[] args) throws IOException
    {
    	FinishAndWrite(generate(readData()));
        System.out.println("Done");

    }

}
