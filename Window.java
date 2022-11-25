import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window implements ActionListener
{
    JMenuItem new_file;
    JMenuItem open_file;
    JMenuItem open_recent;
    JMenuItem save_file;
    JMenuItem save_as;
    public Window() {
        JFrame root = new JFrame("Beispiel JFrame");
        root.setSize(800, 600);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");

        this.new_file = new JMenuItem("new_file");
        this.open_file = new JMenuItem("open_file");
        this.open_recent = new JMenuItem("open_recent");
        this.save_file = new JMenuItem("save_file");
        this.save_as = new JMenuItem("save_as");

        new_file.addActionListener(this);
        open_file.addActionListener(this);
        open_recent.addActionListener(this);
        save_file.addActionListener(this);
        save_as.addActionListener(this);

        menu.add(new_file);
        menu.add(open_file);
        menu.add(open_recent);
        menu.add(save_file);
        menu.add(save_as);

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

    public void actionPerformed (ActionEvent event){
        System.out.println("the menu got clicked");
        if (event.getSource() == this.new_file) {
            System.out.println("new file");
        }
        /*switch (event.getSource()) {
            case this.new_file:
                System.out.println("here");
                break;
        }*/
    }


}
