import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;



public class Tabi_Window implements ActionListener
{
    JMenuItem new_file;
    JMenuItem open_file;
    JMenuItem open_recent;
    JMenuItem save_file;
    JMenuItem save_as;
    JFrame root;
    JTabbedPane tab_manager;
    public Tabi_Window(String[] args) {
        this.root = new JFrame("Tabi");
        this.root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.root.setSize(800, 600);

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
        this.root.setJMenuBar(menubar);

        this.tab_manager = new JTabbedPane();

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

    public static void main(String[] args) {
        new Tabi_Window(args);
    }
}



public class Text_Tab {
    boolean unsaved_changes = false;
    String file_name = "";
    JTextArea text_area;
    Text_Tab(JTabbedPane tab_manager, String pfile_name) {
        System.out.println(pfile_name);
        this.text_area = new JTextArea();
        this.text_area.setFont(new Font("Hack", Font.PLAIN, 13));

        JTextArea line_numbers = new JTextArea();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);

        this.text_area.getDocument().addDocumentListener(new Line_Number_Inserter(this.text_area, line_numbers));

        JScrollPane scroll_plane = new JScrollPane(this.text_area);

        scroll_plane.getViewport().add(this.text_area);
        scroll_plane.setRowHeaderView(line_numbers);

        tab_manager.add("New", scroll_plane);

        if (pfile_name != "") {
            this.file_name = pfile_name;
            // load file
            this.text_area.setText(new Filesystem().read(this.file_name));
        }

        // Make this the selected tab
        tab_manager.setSelectedIndex(tab_manager.getTabCount()-1);
    }

    public void save_document() {
        if (this.file_name == "") {
            //ask user where to save file
            // TODO

            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            }
        }
        //if (this.file_name == "") {
        //    return;
        //}
        new Filesystem().write(this.file_name, this.text_area.getText());
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



public class Filesystem {
    // TODO add Error popup
    public String read(String file_name) {
        // This will reference one line at a time
        String line = null;

        try {
            String result = "";
            // FileReader uses default encoding.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file_name));

            while((line = bufferedReader.readLine()) != null) {
                result += line + "\n";
            }

            bufferedReader.close();
            return result;
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + file_name + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + file_name + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }

    public boolean write(String file_name, String file_content) {
        try {
            //Create file
            new File(file_name).createNewFile();
            //Write to file
            FileWriter myWriter = new FileWriter(file_name);
            myWriter.write(file_content);
            myWriter.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("An error occurred while creating/writing a file");
            e.printStackTrace();
        }
        return false;
    }

    public boolean does_file_exist(String file_name) {
        return new File(file_name).isFile();
    }

    //public static void main(String [] args) {
    //    new Filesystem().write("/home/waldfee/test.txt", "Hello Filesystem", false);
    //    System.out.println(new Filesystem().read("/home/waldfee/test.txt"));
    //}
}
