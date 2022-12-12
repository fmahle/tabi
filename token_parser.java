
public class token_parser {
    public static void parse(CharTreeGraph graph) {
        try{
            for (String line : new Filesystem().read(".tokens").split("\n")) {
                String[] arguments = line.split(" ");
                Token.TokenType type= arguments[4].matches("g")?Token.TokenType.TYPE_GENERIC:arguments[4].matches("d")?Token.TokenType.TYPE_DATATYPE:arguments[4].matches("l")?Token.TokenType.TYPE_LINKED:Token.TokenType.TYPE_GENERIC;
    
                graph.addTokenToGraph(new Token(arguments[0], Integer.parseInt(arguments[1], 16), Integer.parseInt(arguments[2], 16),Integer.parseInt(arguments[3]), type), false);
            }
        }catch(Exception e){
            System.out.println("Parser: "+ e.getMessage());
        }
        
    }
}
