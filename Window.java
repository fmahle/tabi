import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Dictionary;
import java.util.Hashtable;


public class Window {
    JMenuItem new_file;
    JMenuItem open_file;
    JMenu open_recent;
    JMenuItem save_file;
    JMenuItem save_as;
    JMenuItem pyxe; //python execute
    JMenuItem cho_lan;
    JMenuItem close_file;
    JMenuItem[] recent_files;
    JFrame root;
    JTabbedPane tab_manager;
    Dictionary tabs; //https://www.javatpoint.com/dictionary-class-in-java
    public Window(String[] args) {
        this.tabs = new Hashtable();
        /*int x=0xFFFFFFFF;
        char a =(char)(x&0x0000000E);
        char b =(char)((x>>5)&0x0000000E);*/ 
        this.root = new JFrame("Tabi");

        this.root.setIconImage(Toolkit.getDefaultToolkit().getImage("icon"));

        this.root.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.root.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                while (tab_manager.getTabCount() != 0) {
                    if (!get_selected_tab().close_file(false)) {
                        return;
                    }
                }
                root.setVisible(false);
                System.exit(0);
            }
        });


        this.root.setSize(800, 600);
        this.root.setLocationRelativeTo(null);

        JMenuBar menubar = new JMenuBar();
        JMenu file_menu = new JMenu();
        JMenu execution_menu = new JMenu();


        Action action8 = new AbstractAction("File") {
            @Override
            public void actionPerformed(ActionEvent e) {}
        };

        action8.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
        file_menu.setAction(action8);


        Action action9 = new AbstractAction("Run!") {
            @Override
            public void actionPerformed(ActionEvent e) {}
        };

        action9.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        execution_menu.setAction(action9);



        this.new_file = new JMenuItem();
        this.open_file = new JMenuItem();

        this.open_recent = new JMenu();
        update_recent_file_list();

        this.save_file = new JMenuItem();
        this.save_as = new JMenuItem();
        this.close_file = new JMenuItem();
        this.pyxe = new JMenuItem();
        this.cho_lan = new JMenuItem();


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
                    if (get_selected_tab().file_name == "" && (!get_selected_tab().unsaved_changes[0])) {
                        get_selected_tab().open_file(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                    else {
                        new_tab(fileChooser.getSelectedFile().getAbsolutePath());
                    }
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
                get_selected_tab().save_document();
            }
        };

        action4.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        save_file.setAction(action4);


        Action action5 = new AbstractAction("Save File As") {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_selected_tab().save_as();
            }
        };

        action5.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        save_as.setAction(action5);


        Action action6 = new AbstractAction("Close Current Document") {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_selected_tab().close_file(true);
            }
        };

        action6.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        close_file.setAction(action6);


        Action action7 = new AbstractAction("Save & Execute") {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_selected_tab().execute();
            }
        };

        action7.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
        pyxe.setAction(action7);


        Action actiona = new AbstractAction("Choose language") {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_selected_tab().choose_language();
            }
        };

        actiona.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
        cho_lan.setAction(actiona);

        new_file.setAccelerator(KeyStroke.getKeyStroke("control N"));
        open_file.setAccelerator(KeyStroke.getKeyStroke("control O"));
        save_file.setAccelerator(KeyStroke.getKeyStroke("control S"));
        save_as.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        close_file.setAccelerator(KeyStroke.getKeyStroke("control shift C"));

        pyxe.setAccelerator(KeyStroke.getKeyStroke("control E"));

        file_menu.add(new_file);
        file_menu.add(open_file);
        file_menu.add(open_recent);
        file_menu.add(save_file);
        file_menu.add(save_as);
        file_menu.add(close_file);

        execution_menu.add(pyxe);
        execution_menu.add(cho_lan);

        menubar.add(file_menu);
        menubar.add(execution_menu);
        this.root.setJMenuBar(menubar);

        this.tab_manager = new JTabbedPane();

        this.tab_manager.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Text_Tab temp = get_selected_tab();
                if (temp != null) {
                    if (temp.file_name == "") {
                        root.setTitle("Tabi");
                    }
                    else {
                        root.setTitle(new File(temp.file_name).getName() + " -- Tabi");
                    }
                }
            }
        });

        this.root.add(this.tab_manager);

        this.root.setVisible(true);

        if (args.length == 0) {
            new_tab("");
        }
        else {
            for (String file_name : args) {
                new_tab(file_name);
            }
        }


    }

    public void update_recent_file_list() {
        open_recent.removeAll();
        String[] files = new Filesystem().read(".recent_files").split("\n");
        recent_files = new JMenuItem[files.length];
        int i = 0;
        for (String file_name : files) {
            recent_files[i] = new JMenuItem();


            Action an_action = new AbstractAction(file_name) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (new Filesystem().does_file_exist(file_name)) {
                        if (get_selected_tab().file_name == "" && (!get_selected_tab().unsaved_changes[0])) {
                            get_selected_tab().open_file(file_name);
                        }
                        else {
                            new_tab(file_name);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(root, "This file could not be found: \n" + file_name);
                    }
                }
            };

            recent_files[i].setAction(an_action);

            open_recent.add(recent_files[i]);
            i++;
        }
    }

    public Text_Tab new_tab(String file_name) {
        Text_Tab temp = new Text_Tab(this.tab_manager, this, file_name);
        tabs.put(temp.get_index(), temp);
        return temp;
    }

    private Text_Tab get_selected_tab() {
        return (Text_Tab) tabs.get(tab_manager.getSelectedIndex());
    }
}
