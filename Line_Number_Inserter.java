
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Line_Number_Inserter implements DocumentListener {
    private JTextArea text, line_numbers;
    private int lineCount;
 
    Line_Number_Inserter(JTextArea ptext, JTextArea pline_numbers) {
        this.text = ptext;
        this.line_numbers = pline_numbers;
        lineCount=0;
        update_numbers();
       

    }

    private void update_numbers() {
        String result = "1";
        int line_count = 1;
        String input = this.text.getText();
        int length = input.length();
        //System.out.println(this.text.getText());
        for (int i = 0; i < length; i++) {
            if (input.charAt(i) == '\n') {
                line_count++;
                result = result + "\n" + Integer.toString(line_count);
            }
        }
        this.line_numbers.setText(result);
    }

    public void insertUpdate(DocumentEvent e) {
        Document doc= e.getDocument();
        try{
        String t=doc.getText(e.getOffset(), e.getLength());
        if(t.contains("\n")){
            
            this.text.setText(this.text.getText()+"\n"+lineCount);
            lineCount++;
        }
        //System.out.println(t);
        }catch(Exception e_){}
            update_numbers();


    }
    public void removeUpdate(DocumentEvent e) {

        try{
            Document document= e.getDocument();
            String text=document.getText(0, document.getLength());
            

        }catch(Exception ex){}
        update_numbers();
    }
    public void changedUpdate(DocumentEvent e) {

    
        //Plain text components do not fire these events
    }

    
}
