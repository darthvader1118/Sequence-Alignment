import java.io.*;

public class Efficient {
    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total - Runtime.getRuntime().freeMemory()) / 10e3;
    }

    private static double getTimeInMilliseconds() {
        return System.nanoTime() / 10e6;
    }

    public static Alignment getEfficientAlignment(String x, String y) {
        int cost = 0;
        String alignmentX = "", alignmentY = "";
        if (x.length() == 0) {
            for (int i = 0; i < y.length(); i++) {
                alignmentX = alignmentX + '_';
                alignmentY = alignmentY + y.charAt(i);
                cost = cost + Value.delta;

            }
//            return;
        } else if (y.length() == 0) {
            for (int i = 0; i < x.length(); i++) {
                alignmentX = alignmentX + x.charAt(i);
                alignmentY = alignmentY + "_";
                cost = cost + Value.delta;
            }
//            return;
        } else if (x.length() == 1 || y.length() == 1) {
            Alignment score = new Basic(x, y).getAlignment();
            alignmentX = score.alignmentX;
            alignmentY = score.alignmentY;
            cost = score.cost;
//            return;
        } else {
            int xlen = x.length();
            int xmid = xlen / 2;
            int ylen = y.length();
            int yMid = 0;
            {
                StringBuilder revX = new StringBuilder(x.substring(xmid));
                StringBuilder revY = new StringBuilder(y);
                revX.reverse();
                revY.reverse();
                int[] leftScores = getLastColumn(x.substring(0, xmid), y);
                int[] rightScores = getLastColumn(revX.toString(), revY.toString());
                int min = Integer.MAX_VALUE;
                for (int i = 0; i <= ylen; i++) {
                    int combinedScore = leftScores[i] + rightScores[ylen - i];
                    if (combinedScore < min) {
                        min = combinedScore;
                        yMid = i;
                    }
                }
            }
            Alignment aL = getEfficientAlignment(x.substring(0, xmid), y.substring(0, yMid));
            Alignment aR = getEfficientAlignment(x.substring(xmid), y.substring(yMid));
            alignmentX = aL.alignmentX + aR.alignmentX;
            alignmentY = aL.alignmentY + aR.alignmentY;
            cost = aL.cost + aR.cost;

        }
        return new Alignment(cost, alignmentX, alignmentY);
    }

    private static int[] getLastColumn(String x, String y) {
        int[] oldCol = new int[y.length() + 1];
        int[] newCol = new int[y.length() + 1];
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                int cost = Integer.MAX_VALUE;
                if ((i == 0) && (j == 0)) cost = 0;
                if (i > 0) {
                    if (oldCol[j] + Value.delta < cost) {
                        cost = oldCol[j] + Value.delta;
                    }
                }
                if (j > 0) {
                    if (newCol[j - 1] + Value.delta < cost) {
                        cost = newCol[j - 1] + Value.delta;
                    }
                }
                if ((i > 0) && (j > 0)) {
                    char X_i = x.charAt(i - 1);
                    char Y_j = y.charAt(j - 1);
                    if (oldCol[j - 1] + Value.alpha(X_i, Y_j) < cost) {
                        cost = oldCol[j - 1] + Value.alpha(X_i, Y_j);
                    }
                }
                newCol[j] = cost;
            }
            oldCol = newCol;
            newCol = new int[y.length() + 1];
        }
        return oldCol;
    }

    public static void main(String[] args) throws IOException {
        File input = new File(args[0]);
        String[] strings = new String[2];
        int i = -1;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;

            while ((line = br.readLine()) != null) {


                // System.out.println(line);
                if (!line.matches("[0-9]+")) {
                    if (i != -1) {
                        strings[i] = sb.toString();
                        i++;
                        sb = new StringBuilder();
                        sb.append(line);
                    } else {
                        sb.append(line);
                        i++;
                    }

                } else {
                    String add = sb.toString();
                    sb.insert(Integer.parseInt(line) + 1, add);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        strings[i] = sb.toString();
        boolean isSwitched = false;
        //System.out.println(strings[0] + " " + strings[1]);
        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();
        Alignment a;
        if(strings[0].length()> strings[1].length()){
             a = Efficient.getEfficientAlignment(strings[0], strings[1]);
        }
        else{
            isSwitched = true;
             a = Efficient.getEfficientAlignment(strings[1], strings[0]);
        }




        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        double totalUsage = afterUsedMem - beforeUsedMem;
        double totalTime = endTime - startTime;
//
        System.out.println(a.cost);
        System.out.println(a.alignmentX);
        System.out.println(a.alignmentY);
        System.out.println("total time: " + totalTime + " ms");
        System.out.println("total memory: " + totalUsage + " kB");
        System.out.println("length of strings: " + (strings[0].length() + strings[1].length()));

        String first = "";
        String second = "";
        if(isSwitched){
            first = a.alignmentY;
            second = a.alignmentX;
        }
        else{
            first = a.alignmentX;
            second = a.alignmentY;
        }

        FileWriter writetoOuptut = new FileWriter("output.txt");

        BufferedWriter writer = new BufferedWriter(writetoOuptut);

        writer.write("Cost of alignment: " + a.cost + "\n");

        writer.write("First String alignment: " + first+ "\n");
        writer.write("Second String alignment: " + second + "\n");
        writer.write("Total time(ms): " + totalTime + "\n");
        writer.write("Total memory(kB): " + totalUsage + "\n");

        writer.close();


    }
}
