
public class token_parser {
    public static void parse(CharTreeGraph graph) {
        
            String[] lines= new Filesystem().read(".token").split("\n");
            String[] arguments=null;
            for (int i=0; i<lines.length; i++) {
                try{
                    arguments= lines[i].split(" ");
                    Token.TokenType type= arguments[4].matches("g")?Token.TokenType.TYPE_GENERIC:arguments[4].matches("d")?Token.TokenType.TYPE_DATATYPE:arguments[4].matches("l")?Token.TokenType.TYPE_LINKED:Token.TokenType.TYPE_GENERIC;
        
                    graph.addTokenToGraph(new Token(arguments[0], Integer.parseInt(arguments[1], 16), Integer.parseInt(arguments[2], 16),Integer.parseInt(arguments[3]), type), true);
                }catch(Exception e){
                    String name;
                    if(arguments!=null){
                        if(arguments.length<0){
                            name=arguments[0];
                        }else{
                            name="ErrorName";
                        }
                    }else{
                        name="ARG-ERROR";
                    }
                    
                    System.out.println("*-----------------------------------------------------------------*");
                    System.out.println("[FileParser]: Error with token at line ["+i+"] name: "+name+" :: ");;
                    System.out.println(e.getMessage());
                }
                
            }
       
        
    }
}
