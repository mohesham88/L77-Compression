import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class L77Algorithm {

    public static class Tag {
        int position, length;
        String letter;

        public Tag(int pos, int len, String let){
            position = pos;
            length = len;
            letter = let;
        }

        public String toString(){
            String tagsAsString = Integer.toString(position) + "," + Integer.toString(length) + "," + letter + "\n";
            return tagsAsString;
        }
    }

    public void compress(String fileName) throws IOException {
        Path path = Path.of(fileName);
        String[] text = Files.readString(path).split("");
        int index = 0, windowSize = 10;
        ArrayList<String> tags = new ArrayList<String>();



        while(index < text.length){
            int maxMatch = 0, matchIndex = -1;
            for(int i = Math.max(0, (index-windowSize)); i < index; i++){
                int match = 0;
                while((i + match < index) && (index + match < text.length) && text[index + match].equals(text[i + match])){
                    match++;
                }
                if(match > maxMatch){
                    maxMatch = match;
                    matchIndex = i;
                }
            }
            if(maxMatch > 0){
                if(index+maxMatch >= text.length){
                    tags.add(new Tag(index - matchIndex, maxMatch, "\u0000").toString());
                }
                else{
                    tags.add(new Tag(index - matchIndex, maxMatch, text[index + maxMatch]).toString());
                }
                index+= maxMatch+1;
            }
            else{
                tags.add(new Tag(0, 0, text[index]).toString());
                index++;
            }
        }



        fileName = fileName.replace(" --Decompression" , "");
        makeNewFile(fileName, tags , "Compression");
    }

    public void decompress(String fileName) throws IOException {
        Path path = Path.of(fileName);
        String fileData = Files.readString(path);


        // split the file into lines

        String[] lines = fileData.split("\n");

        ArrayList<String> decompressed = new ArrayList<String>();

        for(String line : lines){
            String[] parts= line.split(",");
            int position = Integer.parseInt(parts[0]);
            int length = Integer.parseInt(parts[1]);
            String nextChr = parts[2].replace("\r", ""); // remove the line

            if(position != 0){
                int strtFrom = decompressed.size() - position;
                for(int i =  strtFrom; i < strtFrom + length ;i++  ){
                    decompressed.add(decompressed.get(i));
                }
            }

//            System.out.print(nextChr);

            decompressed.add(nextChr);
        }

        fileName = fileName.replace(" --Compression" , "");
        makeNewFile(fileName, decompressed , "Decompression");
    }



    public void runProgram() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name ex. myfile.txt");
        String filePath = scanner.nextLine();
        System.out.println("Type 1- for compression or 2- for decompression");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                compress(filePath);
                break;
            case 2:
                decompress(filePath);
                break;
            default:
                System.out.println("Invalid input");
        }
    }


    public void makeNewFile(String fileName, ArrayList<String> fileContent, String type){
        try {
            fileName = fileName.replace(".txt", "");
            FileWriter myWriter = new FileWriter(fileName + " --" + type + ".txt");

            String fileData = String.join("",fileContent);

            System.out.println(fileData);
            myWriter.write(fileData);
            myWriter.close();
            System.out.println(type + " DONE");
        } catch (IOException e) {
            System.out.println("An error occurred.");

        }

    }
}
