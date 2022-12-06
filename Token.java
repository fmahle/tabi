import java.net.CookieHandler;

import javax.swing.plaf.synth.ColorType;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.StyleConstants.ColorConstants;

public class Token {
    public String tokenName;
    public int color;
    public Token(){

    }
    public Token(String tokenName,int color){
        this.tokenName=tokenName;
        this.color=color;
    }
}
