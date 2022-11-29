import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TextHighlighter implements CaretListener{
    private CharTreeGraph tokenGraph;
    public TextHighlighter(){
        tokenGraph=new CharTreeGraph();
        tokenGraph.addStringToGraph("null");
        tokenGraph.addStringToGraph("uwu");
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        System.out.println(e.getDot());
        
        // TODO Auto-generated method stub
        
    }
    
}
