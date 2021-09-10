package arithmetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class decompress {
    private static File in;
    static Vector <Float> probability = new Vector<Float>();
    static Vector <Float> cummProb = new Vector<Float>();
    static Vector <Character> letters = new Vector<Character>();
    static double code ;
    static int k=0;
    static String data;
    static int scaleMove=0;
    public static void read()
    {
        in = new File("probs.txt");
        String data = "";
        try {
            Scanner scanf = new Scanner(in);
            while(scanf.hasNextLine())
            {
                data = scanf.nextLine();
                letters.add(data.charAt(0));
                String prob = "";
                for(int j=1; j<data.length(); j++)
                    prob = prob + data.charAt(j);
                Float num = Float.parseFloat(prob);
                probability.add(num);
                
            }
            cummProb.add((float) 0);
            cummProb.add(probability.get(0));
            for(int i=1; i<probability.size(); i++)
            {
                cummProb.add(cummProb.get(i) + probability.get(i));
            }
            scanf.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void getK()
    {
    	float smallOne=2;
        Boolean b=true;
        int getk=2;
    	for(int i=0;i<probability.size();i++)
    	{
    		if(b)
        	{
        		smallOne=probability.elementAt(i);
        		b=false;
        	}
        	if(probability.elementAt(i)<smallOne)
        		smallOne=probability.elementAt(i);
    	}
    	smallOne=1/smallOne;
        

        while(true)
        {
        	getk=(int) Math.pow(2,k);
        	
        	if(getk>=smallOne)
        		break;
        	k++;
        }
        //System.out.println(k);
    }
    public static void readCode()
    {
        in = new File("output.txt");
        data = "";
        try {
            Scanner scanf = new Scanner(in);
            while(scanf.hasNextLine())
            {
                data = scanf.nextLine();
                //code = Float.parseFloat(data);
            }
            scanf.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void write(String text)
    {
        FileWriter out;
        try {
            out = new FileWriter("decompress.txt");
            out.write(text);
            out.flush();
            out.close();
            System.out.println("DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void BinaryToCode()
    {
    	double f=.5;
    	code=0;
    	for(int i=0;i<k;i++)
    	{
    		if(data.charAt(i+scaleMove)=='1')
    		{
    			code+=f;
    			f/=2;
    		}
    		else
    			f/=2;
    	}
    }

    public static void main(String[] args) {
        read();
        readCode();
        getK();
        BinaryToCode();
        Float lower, upper, prevLower = null, prevUpper = null;
        boolean flag = true;
        double newCode =code;
        String text="";
        for (int i=0; i<cummProb.size(); i++)
        {
            if( cummProb.get(i) < newCode && cummProb.get(i+1) > newCode)
            {
                text = text + letters.get(i);
                if(flag)
                {
                    lower = prevLower = cummProb.get(i);
                    upper = prevUpper = cummProb.get(i+1);
                    flag = false;
                }
                else
                {
                    lower = cummProb.get(i);
                    upper = cummProb.get(i+1);
                    lower = prevLower+(prevUpper-prevLower)*lower;
                    upper = prevLower+(prevUpper-prevLower)*upper;
                    ////Scaling
                    while(true)
                    {
                    	if(upper<0.5 && lower<0.5)
                        {
                    		upper*=2;
                    		lower*=2;
                        	scaleMove++;
                        }
                        else if(upper>0.5 && lower>0.5)
                        {
                        	upper=(float) ((upper-0.5)*2);
                        	lower=(float) ((lower-0.5)*2);
                        	scaleMove++;
                        }
                        else
                        	break;
                    }
                    prevLower = lower;
                    prevUpper = upper;
                }
                
                
                
                BinaryToCode();               
                newCode = (code-prevLower) / (prevUpper-prevLower);
                //System.out.println(code+" "+scaleMove+" "+newCode);
                i=0;
            }
        }
        for (int i=0; i<cummProb.size(); i++)
        {
            if( cummProb.get(i) < newCode && cummProb.get(i+1) > newCode)
            {
                text = text + letters.get(i);
            }
        }
        write(text);

    }

}
