import java.io.*;

public class Basic {
    private static double getMemoryInKB() {
        double total = Runtime.getRuntime().totalMemory();
        return (total - Runtime.getRuntime().freeMemory()) / 10e3;
    }

    private static double getTimeInMilliseconds() {
        return System.nanoTime() / 10e6;
    }
    //Usage:
    //  Alignment r = new Basic("AAA","TTTT").getAlignment();
    //  System.out.println(r.cost);
    //  System.out.println(r.alignmentX);
    //  System.out.println(r.alignmentY);


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
        System.out.println(strings[0] + " " + strings[1]);

        double beforeUsedMem = getMemoryInKB();
        double startTime = getTimeInMilliseconds();

        Alignment basic = new Basic(strings[0], strings[1]).getAlignment();


        double afterUsedMem = getMemoryInKB();
        double endTime = getTimeInMilliseconds();
        double totalUsage = afterUsedMem - beforeUsedMem;
        double totalTime = endTime - startTime;

        System.out.println(basic.cost);
        System.out.println(basic.alignmentX);
        System.out.println(basic.alignmentY);
        System.out.println("total time: " + totalTime + " ms");
        System.out.println("total memory: " + totalUsage + " kB");


        FileWriter writeToOutput = new FileWriter(args[1]);

        BufferedWriter writer = new BufferedWriter(writeToOutput);
        writer.write("Cost of alignment: " + basic.cost + "\n");
        writer.write("First String alignment: " + basic.alignmentX + "\n");
        writer.write("Second String alignment: " + basic.alignmentY + "\n");
        writer.write("Total time(ms): " + totalTime + "\n");
        writer.write("Total memory(kB): " + totalUsage + "\n");

        writer.close();
    }

    public Basic(String x, String y) {
        X = x;
        Y = y;
        M = new ManagerEntry[x.length() + 1][y.length() + 1];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                M[i][j] = new ManagerEntry();

            }
        }
        M[0][0].isEmpty = false;

        M[0][0].cost = 0;

    }

    private static class ManagerEntry {
        ManagerEntry() {
            isEmpty = true;
            cost = Integer.MAX_VALUE;
            aX_i = null;
            aY_j = null;
        }

        public boolean isEmpty;
        public int cost;
        public Character aX_i;
        public Character aY_j;
    }

    public final String X;
    public final String Y;
    private final ManagerEntry[][] M;

    public Alignment getAlignment() {
        return getAlignment(X.length(), Y.length());
    }

    public Alignment getAlignment(int i, int j) {
        if (M[i][j].isEmpty) update(i, j);
        return new Alignment(M[i][j].cost, getAlignmentX(i, j), getAlignmentY(i, j));
    }

    public String getAlignmentX(int i, int j) {
        if (M[i][j].isEmpty) update(i, j);
        if ((i == 0) && (j == 0)) return "";
        if (M[i][j].aX_i == null) return getAlignmentX(i, j - 1);
        if (M[i][j].aX_i == '_') return getAlignmentX(i - 1, j) + '_';
        return getAlignmentX(i - 1, j - 1) + M[i][j].aX_i;
    }

    public String getAlignmentY(int i, int j) {
        if (M[i][j].isEmpty) update(i, j);
        if ((i == 0) && (j == 0)) return "";
        if (M[i][j].aY_j == null) return getAlignmentY(i - 1, j);
        if (M[i][j].aY_j == '_') return getAlignmentY(i, j - 1) + '_';
        return getAlignmentY(i - 1, j - 1) + M[i][j].aY_j;
    }

    private void update(int i, int j) {
        int cost = Integer.MAX_VALUE;
        Character aX_i = null, aY_j = null;
        if (i > 0) {
            if (M[i - 1][j].isEmpty) update(i - 1, j);
            if (M[i - 1][j].cost + Value.delta < cost) {
                cost = M[i - 1][j].cost + Value.delta;
                aX_i = '_';
                aY_j = null;
            }
        }
        if (j > 0) {
            if (M[i][j - 1].isEmpty) update(i, j - 1);
            if (M[i][j - 1].cost + Value.delta < cost) {
                cost = M[i][j - 1].cost + Value.delta;
                aX_i = null;
                aY_j = '_';
            }
        }
        if ((i > 0) && (j > 0)) {
            if (M[i - 1][j - 1].isEmpty) update(i - 1, j - 1);
            char X_i = X.charAt(i - 1);
            char Y_j = Y.charAt(j - 1);
            if (M[i - 1][j - 1].cost + Value.alpha(X_i, Y_j) < cost) {
                cost = M[i - 1][j - 1].cost + Value.alpha(X_i, Y_j);
                aX_i = X_i;
                aY_j = Y_j;
            }
        }
        M[i][j].isEmpty = false;
        M[i][j].cost = cost;
        M[i][j].aX_i = aX_i;
        M[i][j].aY_j = aY_j;
    }

    public int[] getLastColumn() {
        if (M[X.length()][Y.length()].isEmpty) update(X.length(), Y.length());
        int[] res = new int[M[0].length];
        for (int i = 0; i < M[0].length; i++) {
            res[i] = M[this.X.length()][i].cost;
        }
        return res;
    }

}