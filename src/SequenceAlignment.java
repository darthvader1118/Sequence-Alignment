import java.io.*;

public class SequenceAlignment {

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

        FileWriter writetoOuptut = new FileWriter("output.txt");

        BufferedWriter writer = new BufferedWriter(writetoOuptut);
        writer.write("");

        writer.close();



    }
}
