public class token_parser {
    public static void main(String[] args) {
        for (String line : new Filesystem().read(".tokens").split("\n")) {
            String[] arguments = line.split(" ");
            Token(arguments[0], Integer.parseInt(arguments[1], 16), Integer.parseInt(arguments[2], 16), arguments[3].equals("t"), arguments[4].equals("t"), arguments[6]);
        }
    }
}
