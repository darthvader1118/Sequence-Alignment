import java.io.*;

public class SequenceAlignment {

    public static void main(String[] args) throws IOException {
        File input = new File(args[0]);
        try(BufferedReader br = new BufferedReader(new FileReader(input))){
            String line;
            while((line = br.readLine())!=null){
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter writetoOuptut = new FileWriter("output.txt");

        BufferedWriter writer = new BufferedWriter(writetoOuptut);
        writer.write("");

        writer.close();



    }
}
