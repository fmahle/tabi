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
    JFrame root;
    JTabbedPane tab_manager;
   // JTextArea text;
    public Window(String[] args) {
        this.root = new JFrame("Tabi");
        this.root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.root.setSize(800, 600);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        //text= new JTextArea();
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
        this.root.setJMenuBar(menubar);

        this.tab_manager = new JTabbedPane();

        JTextArea line_numbers = new JTextArea();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);

        //text.getDocument().addDocumentListener(new Line_Number_Inserter(text, line_numbers));
        //TextHighlighter highlighter= new TextHighlighter();
        //text.addCaretListener(highlighter);
        //JScrollPane scroll_plane = new JScrollPane(text);

        //scroll_plane.getViewport().add(text);
        //scroll_plane.setRowHeaderView(line_numbers);
        if (args.length == 0) {
            new Text_Tab(this.tab_manager, "");
        }
        else {
            for (String file_name : args) {
                new Text_Tab(this.tab_manager, file_name);
            }
        }

        this.root.add(this.tab_manager);

        this.root.setVisible(true);
    }

    public void actionPerformed (ActionEvent event){
        if (event.getSource() == this.new_file) {
            new Text_Tab(this.tab_manager, "");
        }
        else if (event.getSource() == this.open_file) {

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this.root) == JFileChooser.APPROVE_OPTION) {
                new Text_Tab(this.tab_manager, fileChooser.getSelectedFile().getAbsolutePath());
            }
            else {
               return;
            }
        }
    }

   
}
