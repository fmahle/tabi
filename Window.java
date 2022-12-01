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
    JMenu open_recent;
    JMenuItem save_file;
    JMenuItem save_as;
    JMenuItem pyxe; //python execute
    JMenuItem close_file;
    JMenuItem[] recent_files;
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
        JMenu file_menu = new JMenu("File");
        JMenu execution_menu = new JMenu("Run!");

        this.new_file = new JMenuItem("new file");
        this.open_file = new JMenuItem("open file");
        this.open_recent = new JMenu("open recent file");
        this.save_file = new JMenuItem("save file");
        this.save_as = new JMenuItem("save file as");
        this.close_file = new JMenuItem("Close current file");
        this.pyxe = new JMenuItem("execute file");

        this.new_file.addActionListener(this);
        this.open_file.addActionListener(this);
        this.save_file.addActionListener(this);
        this.save_as.addActionListener(this);
        this.close_file.addActionListener(this);
        this.pyxe.addActionListener(this);

        file_menu.add(new_file);
        file_menu.add(open_file);
        file_menu.add(open_recent);
        file_menu.add(save_file);
        file_menu.add(save_as);
        file_menu.add(close_file);

        execution_menu.add(pyxe);

        menubar.add(file_menu);
        menubar.add(execution_menu);
        this.root.setJMenuBar(menubar);

        this.tab_manager = new JTabbedPane();
        //this.tab_manager.addChangeListener(new ChangeListener() {
        //    public void stateChanged(ChangeEvent e) {
        //        System.out.println("Tab: " + tab_manager.getSelectedIndex());
        //    }
        //});

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
        update_recent_file_list();
    }

    private void update_recent_file_list() {
        String[] files = new Filesystem().read(".recent_files").split("\n");
        recent_files = new JMenuItem[files.length];
        int i = 0;
        for (String file_name : files) {
            System.out.println(file_name);
            recent_files[i] = new JMenuItem(file_name);
            recent_files[i].addActionListener(this);
            open_recent.add(recent_files[i]);
            i++;
        }
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
        else if (event.getSource() == this.close_file) {
            selab().close_file();
            if (tab_manager.getTabCount() == 0) {
                new_tab("");
            }
        }
        else {
            for (JMenuItem menu_file : this.recent_files) {
                if (event.getSource() == menu_file) {
                    String file = menu_file.getText();
                    if (new Filesystem().does_file_exist(file)) {
                        new_tab(file);
                    }
                    else {
                        //TODO
                    }
                }
            }
        }
    }
}
