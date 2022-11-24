import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;




public class FrameBeispiel
{
    public static void main(String[] args)
    {
        JFrame root = new JFrame("Beispiel JFrame");
        root.setSize(800, 600);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem foo = new JMenuItem("New");
        menu.add(foo);
        menubar.add(menu);
        root.setJMenuBar(menubar);

        JTextArea text = new JTextArea("a");
        text.setFont(new Font("Hack", Font.PLAIN, 13));

        JTextArea line_numbers = new JTextArea();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);

        text.getDocument().addDocumentListener(new Line_Number_Inserter(text, line_numbers));

        JScrollPane scroll_plane = new JScrollPane(text);

        scroll_plane.getViewport().add(text);
        scroll_plane.setRowHeaderView(line_numbers);

        root.add(scroll_plane);

        root.setVisible(true);
    }
}



public class Line_Number_Inserter implements DocumentListener {
    private JTextArea text, line_numbers;

    Line_Number_Inserter(JTextArea ptext, JTextArea pline_numbers) {
        this.text = ptext;
        this.line_numbers = pline_numbers;
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
        update_numbers();
    }
    public void removeUpdate(DocumentEvent e) {
        update_numbers();
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }
}
