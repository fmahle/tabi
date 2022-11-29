import java.io.*;

public class Filesystem {
    // TODO add Error popup
    public String read(String file_name) {
        // This will reference one line at a time
        String line = null;

        try {
            String result = "";
            // FileReader uses default encoding.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file_name));

            while((line = bufferedReader.readLine()) != null) {
                result += line + "\n";
            }

            bufferedReader.close();
            return result;
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + file_name + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + file_name + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }

    public boolean write(String file_name, String file_content, boolean allow_overwrite) {
        try {
            //Create file
            if (new File(file_name).createNewFile() || allow_overwrite) {
                //Write to file
                FileWriter myWriter = new FileWriter(file_name);
                myWriter.write(file_content);
                myWriter.close();
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred while creating/writing a file");
            e.printStackTrace();
        }
        return false;
    }

    //public static void main(String [] args) {
    //    new Filesystem().write("/home/waldfee/test.txt", "Hello Filesystem", false);
    //    System.out.println(new Filesystem().read("/home/waldfee/test.txt"));
    //}
}

