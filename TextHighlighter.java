import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.StyleConstants.ColorConstants;

import java.awt.Color;
public class TextHighlighter implements DocumentListener{
    private static CharTreeGraph tokenGraph;
    private Text_Tab tab;
    private Token [] tokens;
    private CharTreeGraph removedTokens;
    public TextHighlighter(Text_Tab tab){
        //tokenGraph=new CharTreeGraph();
        removedTokens= new CharTreeGraph();
        //tokens=new Token[2];
        //tokens[0]= new Token("null",0x00FF0000,Token.TokenType.TYPE_DATATYPE);
        //tokens[1]=new Token("int",0x000000FF,Token.TokenType.TYPE_DATATYPE);
        
        
        //tokenGraph.addTokenToGraph(tokens[0],true);
        //tokenGraph.addTokenToGraph(tokens[1],true);
        this.tab=tab;
    }
    CharTreeGraph getGraph(){
        return tokenGraph;
    }
    public static void init(){
        tokenGraph=new CharTreeGraph();
        token_parser.parse(tokenGraph);
    }
    CharTreeGraph getRemovedTokenGraph( ){
        return removedTokens;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
       // System.out.println("e.offset: "+e.getOffset()+" e.length: "+e.getLength());

        // TODO Auto-generated method stub
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                tab.checkForUpdates(false,e.getOffset(),e.getLength());
            }
        });
        
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        
        SwingUtilities.invokeLater(new Runnable(){
          
            @Override
            public void run(){
                tab.checkForUpdates(true,e.getOffset(),e.getLength());
            }
        });
        
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        
        
    }
    
}
