import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordSearchV1 {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter String");
        try {

            String s = br.readLine();
            String[] keys = new String[4];
            keys[0] = "Hallo";
            keys[1] = "hallo";
            keys[2] = "Hi";
            keys[3] = "hi";

            searchInInput(s, keys[0]);

        } catch (Exception e) {

        }

    }

    public static void searchInInput(String str, String key) {
        int foundIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == key.charAt(foundIndex)) {
                foundIndex++;
                if (foundIndex == key.length()) {
                    foundIndex = 0;
                    System.out.println("Found at " + (i - key.length()));
                }

            } else {
                foundIndex = 0;
            }

        }

    }

    public static boolean SearchForStringAt(String str, String key, int offset) {
        for (int i = offset; i < str.length(); i++) {
            if (i - offset < key.length()) {
                if (str.charAt(i) == key.charAt(i - offset)) {
                    if (i - offset == key.length()-1) {
                        System.out.println("Found Key: '"+key+"' at " + (i - key.length()+1));
                        return true;
                    }

                } else {
                    return false;
                }
            } else{
                return false;
            }
             
        }
        return false;
    }
    public static boolean SearchForStringAtDEBUG(String str, String key, int offset) {
        for (int i = offset; i < str.length(); i++) {
            if (i - offset < key.length()) {
                if (str.charAt(i) == key.charAt(i - offset)) {
                    System.out.println("\tString at "+i+" is "+str.charAt(i));
                    if (i - offset == key.length()-1) {
                        System.out.println("Found Key: '"+key+"' at " + (i - key.length()+1));
                        return true;
                    }

                } else {
                    System.out.println("\tString at "+i+" is not "+key.charAt(i - offset));
                    return false;
                }
            } else{
                return false;
            }
          
        }
        return false;
    }
    public static void searchInInputMass(String str, String key[]) {

        for (int i = 0; i < str.length(); i++) {
            for (int k = 0; k < key.length; k++) {
                if(SearchForStringAtDEBUG(str, key[k], i)){   
                    i+=key[k].length()-1;
                    k=key.length;
            
                }

            }

        }

    }

}