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
   // private Token [] tokens;
    private CharTreeGraph removedTokens;
    //private static boolean finished;
    public TextHighlighter(Text_Tab tab){
        removedTokens= new CharTreeGraph();
        
        this.tab=tab;
    }
    public static void init(){
        tokenGraph=new CharTreeGraph();
        token_parser.parse(tokenGraph);
    }
    public CharTreeGraph getGraph(){
        return tokenGraph;
    }
    public CharTreeGraph getRemovedTokenGraph( ){
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
