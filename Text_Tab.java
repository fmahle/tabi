import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.AttributeSet.ColorAttribute;

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
        this.text_area.addCaretListener(new TextHighlighter(text_area));
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
