import java.io.*;

public class Efficient {
    private Basic b;
    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total-Runtime.getRuntime().freeMemory())/10e3;
    }
    private static double getTimeInMilliseconds() {
        return System.nanoTime()/10e6;
    }

    public Efficient(String x, String y){
        b = new Basic(x,y);
    }
    public Alignment getAlignment(){
        int splitX = b.X.length()/2;
        return new Alignment(0,"","");
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
