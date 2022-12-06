import java.util.HashMap;

public class Value {
    public static final int delta = 30;
    public static HashMap<String,Integer> alphatables = new HashMap<>();
    static{
        //put mathcing pairs
        alphatables.put("AA",0);
        alphatables.put("CC",0);
        alphatables.put("GG",0);
        alphatables.put("TT",0);
        //
        alphatables.put("AC",110);
        alphatables.put("CA",110);
        alphatables.put("AG",48);
        alphatables.put("GA",48);
        alphatables.put("AT",94);
        alphatables.put("TA",94);
        alphatables.put("CG",118);
        alphatables.put("GC",118);
        alphatables.put("CT",48);
        alphatables.put("TC",48);
        alphatables.put("TG",110);
        alphatables.put("GT",110);
    }


    public static int alpha(char x, char y) {
        String key = "" + x+y;




        return alphatables.get(key); // hard code alpha value here
    }
}
