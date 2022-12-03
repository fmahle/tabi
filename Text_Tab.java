import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

public class Text_Tab {
    boolean[] unsaved_changes = {false}; // bools are immutable yet arrays are
    String file_name = "";
    JTextArea text_area;
    int tab_index;
    JTabbedPane tab_manager;
    Window root;
    Text_Tab(JTabbedPane ptab_manager, Window root, String pfile_name) {
        this.root = root;
        this.tab_manager = ptab_manager;
        System.out.println(pfile_name);
        this.text_area = new JTextArea();
        this.text_area.setFont(new Font("Hack", Font.PLAIN, 13));

        JTextArea line_numbers = new JTextArea();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);

        this.text_area.getDocument().addDocumentListener(new Line_Number_Inserter(this.text_area, line_numbers, unsaved_changes));
        this.text_area.addCaretListener(new TextHighlighter());
        JScrollPane scroll_plane = new JScrollPane(this.text_area);

        scroll_plane.getViewport().add(this.text_area);
        scroll_plane.setRowHeaderView(line_numbers);

        this.tab_manager.add("New", scroll_plane);

        this.tab_index = this.tab_manager.getTabCount()-1;

        if (pfile_name != "") {
            this.file_name = pfile_name;
            this.tab_manager.setTitleAt(this.tab_index, new File(this.file_name).getName());
            // load file
            this.text_area.setText(new Filesystem().read(this.file_name));
        }

        // Make this the selected tab
        this.tab_manager.setSelectedIndex(this.tab_index);
    }

    public int get_index() {
        return this.tab_index;
    }

    public void save_as() {
        String temp = this.file_name;
        this.file_name = "";
        save_document();
        if (this.file_name == "") {
            this.file_name = temp;
        }
    }

    public void save_document() {
        if (this.file_name == "") {
            JFrame parentFrame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                this.file_name = fileChooser.getSelectedFile().getAbsolutePath();
            }
            else {
                return;
            }
        }
        new Filesystem().write(this.file_name, this.text_area.getText());
        this.unsaved_changes[0] = false;
    }

    public void execute() {
        if (this.file_name == "") {
            save_document();
            if (this.file_name == "") {
                JOptionPane.showMessageDialog(this.tab_manager, "You need to save your files in order to run them!");
                return;
            }
        }

        try {
            Process Demo_Process = new ProcessBuilder("python", this.file_name).inheritIO().start();
            Demo_Process.waitFor();

            BufferedReader Buffered_Reader = new BufferedReader(
                                            new InputStreamReader(
                                            Demo_Process.getInputStream()
                                            ));
            String Output_line = "";

            while ((Output_line = Buffered_Reader.readLine()) != null) {
                System.out.println(Output_line);
            }
        }
        catch (IOException e) {}
        catch (InterruptedException e) {}
    }
    public boolean close_file() {
        if (this.unsaved_changes[0]) {
            int reply = JOptionPane.showConfirmDialog(null, "Do you want to save this document? ", "Save" + this.file_name + " ?", JOptionPane.YES_NO_CANCEL_OPTION);

            if (reply != JOptionPane.NO_OPTION && reply != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        this.tab_manager.remove(this.tab_index);

        if (this.file_name != "") {
            boolean file_already_in_list = false;
            for (String file : new Filesystem().read(".recent_files").split("\n")) {
                if (file == this.file_name) {
                    file_already_in_list = true;
                }
            }
            if (!file_already_in_list) {
                new Filesystem().write(".recent_files", this.file_name + "\n" + new Filesystem().read(".recent_files"));
            }
            this.root.update_recent_file_list();
        }
        return true;
    }
}
