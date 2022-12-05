public class Basic {
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
}