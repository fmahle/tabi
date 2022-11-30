import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;


public class Window implements ActionListener
{
    JMenuItem new_file;
    JMenuItem open_file;
    JMenuItem open_recent;
    JMenuItem save_file;
    JMenuItem save_as;
    JMenuItem pyxe; //python execute
    JFrame root;
    JTabbedPane tab_manager;
    Dictionary tabs; //https://www.javatpoint.com/dictionary-class-in-java
    public Window(String[] args) {
        this.tabs = new Hashtable();

        this.root = new JFrame("Tabi");

        this.root.setIconImage(Toolkit.getDefaultToolkit().getImage("icon"));

        this.root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.root.setSize(800, 600);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");

        this.new_file = new JMenuItem("new file");
        this.open_file = new JMenuItem("open file");
        this.open_recent = new JMenuItem("open recent file");
        this.save_file = new JMenuItem("save file");
        this.save_as = new JMenuItem("save file as");
        this.pyxe = new JMenuItem("execute file");

        new_file.addActionListener(this);
        open_file.addActionListener(this);
        open_recent.addActionListener(this);
        save_file.addActionListener(this);
        save_as.addActionListener(this);
        pyxe.addActionListener(this);

        menu.add(new_file);
        menu.add(open_file);
        menu.add(open_recent);
        menu.add(save_file);
        menu.add(save_as);
        menu.add(pyxe);

        menubar.add(menu);
        this.root.setJMenuBar(menubar);

        this.tab_manager = new JTabbedPane();

        if (args.length == 0) {
            new_tab("");
        }
        else {
            for (String file_name : args) {
                new_tab(file_name);
            }
        }

        this.root.add(this.tab_manager);

        this.root.setVisible(true);
    }

    private Text_Tab new_tab(String file_name) {
        Text_Tab temp = new Text_Tab(this.tab_manager, file_name);
        tabs.put(temp.get_index(), temp);
        return temp;
    }

    private Text_Tab selab() { //get_selected_tab()
        return (Text_Tab) tabs.get(tab_manager.getSelectedIndex());
    }

    public void actionPerformed (ActionEvent event){
        if (event.getSource() == this.new_file) {
            new_tab("");
        }
        else if (event.getSource() == this.open_file) {

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this.root) == JFileChooser.APPROVE_OPTION) {
                new_tab(fileChooser.getSelectedFile().getAbsolutePath());
            }
            else {
               return;
            }
        }
        else if (event.getSource() == this.save_file) {
            selab().save_document();
        }
        else if (event.getSource() == this.save_as) {
            selab().save_as();
        }
        else if (event.getSource() == this.pyxe) {
            selab().execute();
        }
    }
}
