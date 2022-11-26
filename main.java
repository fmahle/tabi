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
    public Tabi_Window() {
        JFrame root = new JFrame("Beispiel JFrame");
        root.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

    public static void main(String[] args) {
        new Tabi_Window();
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

    public boolean write(String file_name, String file_content, boolean allow_overwrite) {
        try {
            //Create file
            if (new File(file_name).createNewFile() || allow_overwrite) {
                //Write to file
                FileWriter myWriter = new FileWriter(file_name);
                myWriter.write(file_content);
                myWriter.close();
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred while creating/writing a file");
            e.printStackTrace();
        }
        return false;
    }

    //public static void main(String [] args) {
    //    new Filesystem().write("/home/waldfee/test.txt", "Hello Filesystem", false);
    //    System.out.println(new Filesystem().read("/home/waldfee/test.txt"));
    //}
}
