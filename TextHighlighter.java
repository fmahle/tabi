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
    private CharTreeGraph tokenGraph;
    private Text_Tab tab;
    private Token [] tokens;
    public TextHighlighter(Text_Tab tab){
        tokenGraph=new CharTreeGraph();
        tokens=new Token[2];
        tokens[0]= new Token("null",0x00FF0000);
        tokens[1]=new Token("uwu",0x000000FF);

        tokenGraph.addTokenToGraph(tokens[0]);
        tokenGraph.addTokenToGraph(tokens[1]);
        this.tab=tab;
    }
    CharTreeGraph getGraph(){
        return this.tokenGraph;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
       // System.out.println("e.offset: "+e.getOffset()+" e.length: "+e.getLength());

        // TODO Auto-generated method stub
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                tab.checkForUpdates();
            }
        });
        
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        
        SwingUtilities.invokeLater(new Runnable(){
          
            @Override
            public void run(){
                tab.checkForUpdates();
            }
        });
        
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        
        
    }
    
}
