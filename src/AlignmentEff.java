public class AlignmentEff {
    public int cost;
    public String alignmentX;
    public String alignmentY;
    AlignmentEff(int cost, String alignmentX, String alignmentY){
        this.cost = cost;
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
    }
    public static AlignmentEff sum(AlignmentEff a, AlignmentEff b){
        AlignmentEff sum = new AlignmentEff(0,"","");
        sum.cost = a.cost + b.cost;
        sum.alignmentX = a.alignmentX + b.alignmentX;
        sum.alignmentY = a.alignmentY + b.alignmentY;
        return sum;
    }
}
