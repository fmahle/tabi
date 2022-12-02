import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;


public class Window
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
        JMenu file_menu = new JMenu("Fil");
        JMenu execution_menu = new JMenu("Run");


        Action action8 = new AbstractAction("File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening file menu");
            }
        };

        action8.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
        file_menu.setAction(action8);


        Action action9 = new AbstractAction("Run!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening execution_menu");
            }
        };

        action9.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        execution_menu.setAction(action9);



        this.new_file = new JMenuItem();
        this.open_file = new JMenuItem();
        this.open_recent = new JMenu();
        this.save_file = new JMenuItem();
        this.save_as = new JMenuItem();
        this.close_file = new JMenuItem();
        this.pyxe = new JMenuItem();


        Action action1 = new AbstractAction("New File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new_tab("");
            }
        };

        action1.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        new_file.setAction(action1);


        Action action2 = new AbstractAction("Open File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(root) == JFileChooser.APPROVE_OPTION) {
                    new_tab(fileChooser.getSelectedFile().getAbsolutePath());
                }
                else {
                    return;
                }
            }
        };

        action2.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        open_file.setAction(action2);


        Action action3 = new AbstractAction("Open Recent File") {
            @Override
            public void actionPerformed(ActionEvent e) {}
        };

        action3.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        open_recent.setAction(action3);


        Action action4 = new AbstractAction("Save File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selab().save_document();
            }
        };

        action4.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        save_file.setAction(action4);


        Action action5 = new AbstractAction("Save File As") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selab().save_as();
            }
        };

        action5.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        save_as.setAction(action5);


        Action action6 = new AbstractAction("Close Current Document") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selab().close_file();
                if (tab_manager.getTabCount() == 0) {
                    new_tab("");
                }
            }
        };

        action6.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        close_file.setAction(action6);


        Action action7 = new AbstractAction("Execute") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selab().execute();
            }
        };

        action7.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        pyxe.setAction(action7);


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
            recent_files[i] = new JMenuItem();


            Action an_action = new AbstractAction(file_name) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (new Filesystem().does_file_exist(file_name)) {
                        new_tab(file_name);
                    }
                    else {
                        //TODO
                    }
                }
            };

            recent_files[i].setAction(an_action);

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
}
