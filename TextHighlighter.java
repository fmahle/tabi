import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TextHighlighter implements CaretListener{
    private CharTreeGraph tokenGraph;
    private JTextPane area;
    public TextHighlighter(JTextPane area){
        tokenGraph=new CharTreeGraph();
        tokenGraph.addStringToGraph("null");
        tokenGraph.addStringToGraph("uwu");
        this.area=area;
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        String thisLine;
        for(int i=e.getDot(); i<area.getText().length();i++){


            
        }
        //System.out.println("posCursor:"+e.getDot());
        //System.out.println(""+e.getSource().lin);
        // TODO Auto-generated method stub
        
    }
    
}
