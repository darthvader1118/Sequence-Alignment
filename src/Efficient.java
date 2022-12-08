import java.io.*;

public class Efficient {
    private AlignmentEff a;
    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total-Runtime.getRuntime().freeMemory())/10e3;
    }
    private static double getTimeInMilliseconds() {
        return System.nanoTime()/10e6;
    }
    public Efficient(){
        this.a = new AlignmentEff(0,"","");
    }


    public AlignmentEff getEfficientAlignment(String x, String y){
        AlignmentEff a = new AlignmentEff(0,"","");
        if(x.length()==0){
            for (int i = 0; i < y.length(); i++) {
                a.alignmentX = a.alignmentX + '_';
                a.alignmentY = a.alignmentY + y.charAt(i);
                a.cost = a.cost + Value.delta;

            }
//            return;
        }
        else if(y.length() == 0){
            for (int i = 0; i < x.length(); i++) {
                a.alignmentX = a.alignmentX + x.charAt(i);
                a.alignmentY = a.alignmentY + "_";
                a.cost = a.cost + Value.delta;
            }
//            return;
        }
        else if(x.length() == 1 || y.length() == 1){
            Alignment score = new Basic(x,y).getAlignment();
            a.alignmentX = score.alignmentX;
            a.alignmentY = score.alignmentY;
            a.cost = score.cost;
//            return;
        }
        else{
            int xlen = x.length();
            int xmid = xlen/2;
            int ylen = y.length();
            Basic scoreL = new Basic(x.substring(0,xmid), y);
            scoreL.getAlignment();
            int [] leftScores = scoreL.getLastColumn();
            StringBuilder revX = new StringBuilder(x.substring(xmid));
            StringBuilder revY = new StringBuilder(y);
            revX.reverse();
            revY.reverse();
            Basic scoreR = new Basic(revX.toString(),revY.toString());
            scoreR.getAlignment();
            int [] rightScores = scoreR.getLastColumn();
            int[] combined = new int[leftScores.length];
            int min = Integer.MAX_VALUE;
            int ymid = 0;
            for (int i = 0; i < leftScores.length; i++) {
                combined[i] = leftScores[i] + rightScores[rightScores.length - 1 - i];
                if(combined[i] < min){
                    min = combined[i];
                    ymid = i;
                }
            }
           a = AlignmentEff.sum(getEfficientAlignment(x.substring(0,xmid),y.substring(0,ymid)),getEfficientAlignment(x.substring(xmid),y.substring(ymid)));


        }
        return a;
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
        Efficient eff = new Efficient();
        double beforeUsedMem=getMemoryInKB();
        double startTime = getTimeInMilliseconds();

       AlignmentEff a = eff.getEfficientAlignment(strings[0],strings[1]);


        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        double totalUsage = afterUsedMem-beforeUsedMem;
        double totalTime = endTime - startTime;

        System.out.println(a.cost);
        System.out.println(a.alignmentX);
        System.out.println(a.alignmentY);
        System.out.println("total time: " + totalTime + " ms");
        System.out.println("total memory: " + totalUsage + " kB");
        System.out.println("length of strings: " + (strings[0].length() + strings[1].length()));

        FileWriter writetoOuptut = new FileWriter("output.txt");

        BufferedWriter writer = new BufferedWriter(writetoOuptut);

        writer.write("Cost of alignment: " + a.cost+ "\n");
        writer.write("First String alignment: " + a.alignmentX+ "\n");
        writer.write("Second String alignment: " + a.alignmentY+ "\n");
        writer.write("Total time(ms): " + totalTime+ "\n");
        writer.write("Total memory(kB): " + totalUsage+ "\n");

        writer.close();



    }
}
