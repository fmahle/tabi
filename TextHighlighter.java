import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.StyleConstants.ColorConstants;
import java.awt.Color;
public class TextHighlighter implements CaretListener{
    private CharTreeGraph tokenGraph;
    private JTextPane area;
    private Token [] tokens;
    public TextHighlighter(JTextPane area){
        tokenGraph=new CharTreeGraph();
        tokens=new Token[2];
        tokens[0]= new Token("null",0x00FF0000);
        tokens[1]=new Token("uwu",0x000000FF);

        tokenGraph.addTokenToGraph(tokens[0]);
        tokenGraph.addTokenToGraph(tokens[1]);
        this.area=area;
    }
    CharTreeGraph getGraph(){
        return this.tokenGraph;
    }
    @Override
    public void caretUpdate(CaretEvent e) {
         /* 
        String thisLine;
        //get current line
        String text= area.getText();
        int firstIndex=0;
        int lastIndex=text.length();
        int dotPos=e.getDot();
        for(int i=dotPos; i<text.length();i++){
            if(text.charAt(i)=='\n'){
                lastIndex=i;
                break;
            } 
        }
        
        for(int i=dotPos-1;i>=0;i--){
            
            if(text.charAt(i)=='\n'){
                firstIndex=i;
                break;
            }
        } 
        if(firstIndex>=lastIndex)return;
        StyledDocument sDoc=area.getStyledDocument();
        thisLine= text.substring(firstIndex, lastIndex);
        for(int i=0; i<thisLine.length();i++){
            Token t= tokenGraph.searchForToken(thisLine.substring(i, thisLine.length()-1));
            if(t!=null){
                SimpleAttributeSet colorAttributeSet=new SimpleAttributeSet();
                StyleConstants.setForeground(colorAttributeSet,new Color(t.color));
                StyleConstants.setBackground(colorAttributeSet, Color.WHITE);
                StyleConstants.setUnderline(colorAttributeSet, Boolean.FALSE );
                StyleConstants.setBold(colorAttributeSet, false);
                int startIndex= firstIndex+i;
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        sDoc.setCharacterAttributes(startIndex,t.tokenName.length(),colorAttributeSet,true);
                    }});
                //TODO: change location to outside doc listener
                
                //sDoc.setCharacterAttributes(firstIndex+i,t.tokenName.length(),colorAttributeSet,true);
                i+=t.tokenName.length()-1;
            }

        }

        */
        
    }
    
}
