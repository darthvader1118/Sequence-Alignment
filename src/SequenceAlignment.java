import java.io.*;

public class SequenceAlignment {

    public static void main(String[] args) {
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

    }
}
