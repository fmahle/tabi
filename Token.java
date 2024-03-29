import java.net.CookieHandler;

import javax.swing.plaf.synth.ColorType;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.StyleConstants.ColorConstants;

public class Token {
     public enum TokenType{
        TYPE_DATATYPE,
        TYPE_VARIABLE,
        TYPE_GENERIC,
        TYPE_LINKED;
        /* 
        String toString(){
            switch(this){
                case TYPE_DATATYPE:
                return "TYPE_DATATYPE";
                case TYPE_VARIABLE:
                return "TYPE_VARIABLE";
                case TYPE_GENERIC:
                return "TYPE_GENERIC";
                case TYPE_LINKED:
                return "TYPE_LINKED";
                default:
                return "ERROR_TYPE";
            }
        }*/

     }
    public String tokenName;
    public int fontColor;
    public int backgroundColor;
    //flags: 0x01: underlined, 0x02: bold
    public char flags;
    public TokenType type;
    public boolean isValid;
    public Token(){
         
    }
    public Token(String tokenName,int fontColor,int backColor,char flags,TokenType type){
        this.tokenName=tokenName;
        this.fontColor=fontColor;
        this.backgroundColor=backColor;
        this.flags=flags;
        this.type=type;
        isValid=false;
        
    }
    public Token(String tokenName,int fontColor,TokenType type){
        this.tokenName=tokenName;
        this.fontColor=fontColor;
        this.backgroundColor=0xFFFFFFFF;
        this.flags=0x00;
        this.type=type;
        isValid=false;
    }
    public Token(String tokenName,int fontColor,int backColor,int flags,TokenType type){
        this.tokenName=tokenName;
        this.fontColor=fontColor;
        this.backgroundColor=backColor;
        this.flags=(char)(flags&0x000000FF);
        this.type=type;
        isValid=false;
    }
    public Token(String tokenName,Token other){
         this.tokenName=tokenName;
         this.fontColor=other.fontColor;
         this.flags=other.flags;
         this.type=other.type;
         this.backgroundColor=other.backgroundColor;
         isValid=false;
    }
}
