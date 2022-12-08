import java.io.*;

public class Basic {
    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total-Runtime.getRuntime().freeMemory())/10e3;
    }
    private static double getTimeInMilliseconds() {
        return System.nanoTime()/10e6;
    }
    //Usage:
    //  Alignment r = new Basic("AAA","TTTT").getAlignment();
    //  System.out.println(r.cost);
    //  System.out.println(r.alignmentX);
    //  System.out.println(r.alignmentY);
    public Basic(String x, String y) {
        X = x;
        Y = y;
        M = new ManagerEntry[x.length() + 1][y.length() + 1];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                M[i][j] = new ManagerEntry();
                if(i==0){
                    M[i][0].isEmpty = false;
                    M[i][0].alignment = new Alignment(j*Value.delta, x.substring(0,j),"");
                }
                if(j==0){
                    M[0][j].isEmpty = false;
                    M[0][j].alignment = new Alignment(i*Value.delta, "",y.substring(0,i));
                }
            }
        }
        M[0][0].isEmpty = false;
        M[0][0].alignment = new Alignment(0, "", "");

    }

    private class ManagerEntry {
        ManagerEntry() {
            isEmpty = true;
            alignment = null;
        }
        public boolean isEmpty;
        public Alignment alignment;
    }

    public final String X;
    public final String Y;
    private ManagerEntry[][] M;

    public Alignment getAlignment() {
        return getAlignment(X.length(),Y.length());
    }

    public Alignment getAlignment(int i, int j) {
        if (!M[i][j].isEmpty) return M[i][j].alignment;

        int cost = Integer.MAX_VALUE;
        String alignmentX = null, alignmentY = null;
        if (i > 0) {
            Alignment a = getAlignment(i-1,j);
            if (a.cost + Value.delta < cost) {
                cost = a.cost + Value.delta;
                alignmentX = a.alignmentX + '_';
                alignmentY = a.alignmentY;
            }
        }
        if (j > 0) {
            Alignment a = getAlignment(i,j-1);
            if (a.cost + Value.delta < cost) {
                cost = a.cost + Value.delta;
                alignmentX = a.alignmentX;
                alignmentY = a.alignmentY + '_';
            }
        }
        if ((i > 0) && (j > 0)) {
            Alignment a = getAlignment(i-1,j-1);
            char X_i = X.charAt(i - 1);
            char Y_j = Y.charAt(j - 1);
            if (a.cost + Value.alpha(X_i,Y_j) < cost) {
                cost = a.cost + Value.alpha(X_i,Y_j);
                alignmentX = a.alignmentX + X_i;
                alignmentY = a.alignmentY + Y_j;
            }
        }
        M[i][j].isEmpty = false;
        M[i][j].alignment = new Alignment(cost, alignmentX, alignmentY);
        return M[i][j].alignment;
    }
    public static void main(String[] args) throws IOException {
        File input = new File(args[0]);
        String[] strings = new String[2];
        int i = -1;
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(input))){
            String line;

            while((line = br.readLine())!=null){


                // System.out.println(line);
                if(!line.matches("[0-9]+")){
                    if(i != -1) {
                        strings[i] = sb.toString();
                        i++;
                        sb = new StringBuilder();
                        sb.append(line);
                    }
                    else{
                        sb.append(line);
                        i++;
                    }

                }
                else{
                    String add = sb.toString();
                    sb.insert(Integer.parseInt(line) +1,add);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        strings[i] = sb.toString();
        System.out.println(strings[0] + " " + strings[1]);

        double beforeUsedMem=getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        Alignment basic = new Basic(strings[0],strings[1]).getAlignment();


        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        System.out.println(basic.cost);
        System.out.println(basic.alignmentX);
        System.out.println(basic.alignmentY);
        System.out.println("total time: " + totalTime + " ms");
        System.out.println("total memory: " + totalUsage + " kB");
        System.out.println("length of strings: " + (strings[0].length() + strings[1].length()));

        FileWriter writetoOuptut = new FileWriter("output.txt");

        BufferedWriter writer = new BufferedWriter(writetoOuptut);

        writer.write("Cost of alignment: " + basic.cost + "\n");
        writer.write("First String alignment: " + basic.alignmentX+ "\n");
        writer.write("Second String alignment: " + basic.alignmentY+ "\n");
        writer.write("Total time(ms): " + totalTime+ "\n");
        writer.write("Total memory(kB): " + totalUsage+ "\n");

        writer.close();



    }
}