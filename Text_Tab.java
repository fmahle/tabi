import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.awt.*;
import java.awt.event.*;

public class Text_Tab {
    
    
    public class MyCloseActionHandler implements ActionListener {
        //Text_Tab tab;
        //
        //public MyCloseActionHandler(Text_Tab ptab) {
        //    this.tab = ptab;
        //}

        public void actionPerformed(ActionEvent evt) {
            close_file();
        }
    }

    public boolean[] unsaved_changes = {false}; // make bool pointer
    public String file_name = "";
    String program = "";
    public JTextPane text_area;
    public int tab_index;
    public JTabbedPane tab_manager;
    public TextHighlighter highlighter;
    public Window root;
    public int lastDot;

                    
    Text_Tab(JTabbedPane ptab_manager, Window root, String pfile_name) {
        this.root = root;
        this.tab_manager = ptab_manager;
        System.out.println(pfile_name);
        this.text_area = new JTextPane();
        this.text_area.setFont(new Font("Hack", Font.PLAIN, 13));
        highlighter= new TextHighlighter(this);
        JTextPane line_numbers = new JTextPane();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);
        lastDot=0;
        this.text_area.getDocument().addDocumentListener(new Line_Number_Inserter(this.text_area, line_numbers,unsaved_changes)); 
        this.text_area.getDocument().addDocumentListener(highlighter);
        JScrollPane scroll_plane = new JScrollPane(this.text_area);

        scroll_plane.getViewport().add(this.text_area);
        scroll_plane.setRowHeaderView(line_numbers);
        
        this.tab_manager.add("New", scroll_plane);

        this.tab_index = this.tab_manager.getTabCount()-1;

        /*
        JPanel pnlTab = new JPanel(new GridBagLayout());

        JLabel lblTitle = new JLabel("New");
        JButton btnClose = new JButton("x");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        this.tab_manager.setTabComponentAt(this.tab_index, pnlTab);

        btnClose.addActionListener(new MyCloseActionHandler());
        */

       
        if (pfile_name != "") {
            open_file(pfile_name);
        }

        this.unsaved_changes[0] = false;

        // Make this the selected tab
        this.tab_manager.setSelectedIndex(this.tab_index);
    }
    public void checkForUpdates() {
        //get current line
        String text = this.text_area.getText();
        // System.out.println(text);
        int firstIndex = 0;
        int lastIndex = text.length();
        int dotPos = this.text_area.getCaret().getDot();

        //System.out.println("dotPos: "+dotPos);
        //System.out.println("totalLength: "+this.text_area.getText().length());
  
        if (lastDot != dotPos) {
            lastDot = dotPos;
          
                int lineCounter = 0;
                int charCounter = 0;
                int currentLineStart = 0;
                for (int i = 0; i < lastIndex; i++) {
                    if (text.charAt(i)=='\n'){
                        lineCounter++;
                        currentLineStart=charCounter;
                    }
                    else {
                        charCounter++;
                        if(charCounter == dotPos) {
                            firstIndex = currentLineStart;
                            break;
                        }
                    }
                }
                for (int i = charCounter + lineCounter; i < lastIndex; i++) {
                    if (text.charAt(i) == '\n'){
                        lastIndex=i;
                        break;
                    }
                }


                if (firstIndex >= lastIndex) {
                    return;
                }
                StyledDocument sDoc = this.text_area.getStyledDocument();

                //reset Style:
                SimpleAttributeSet colorAttributeSet=new SimpleAttributeSet();
                StyleConstants.setForeground(colorAttributeSet, Color.BLACK);
                StyleConstants.setBackground(colorAttributeSet, Color.WHITE);
                StyleConstants.setUnderline(colorAttributeSet, false);
                StyleConstants.setBold(colorAttributeSet, false);
                boolean isDatatype = false;
                boolean nextVariable = false;
                String newVar = null;
                sDoc.setCharacterAttributes(firstIndex,lastIndex - (firstIndex+lineCounter),colorAttributeSet,true);
                CharTreeGraph graph = highlighter.getGraph();
                for(int i = firstIndex + lineCounter; i < lastIndex; i++) {
                    if (!isDatatype) {
                    Token t = graph.searchForToken(text.substring(i, lastIndex));
                    if (t!=null) {
                        StyleConstants.setForeground(colorAttributeSet,new Color(t.fontColor));
                        StyleConstants.setBackground(colorAttributeSet, new Color(t.backgroundColor));
                        StyleConstants.setUnderline(colorAttributeSet, (t.flags & 0x01) == 0x01);
                        StyleConstants.setBold(colorAttributeSet, (t.flags & 0x02) == 0x02);

                        sDoc.setCharacterAttributes(i - lineCounter,t.tokenName.length(), colorAttributeSet, true);
                        //check if token is datatype
                        if (t.type == Token.TokenType.TYPE_DATATYPE) {
                            isDatatype = true;
                        }

                        i += t.tokenName.length() - 1;
                    }
                }
                else if ((isDatatype && text.charAt(i) == ' ') && !nextVariable){
                        nextVariable = true;
                        newVar = new String();
                }
                else if (nextVariable&&text.charAt(i) != ' '){
                        newVar+=text.charAt(i);
                }
                else if (nextVariable&&(text.charAt(i) == ' ' || text.charAt(i) == '\n')) {

                    if (newVar.length() > 1) {
                            this.highlighter.getGraph().addTokenToGraph(new Token(newVar,0x0000FFFF, Token.TokenType.TYPE_VARIABLE));
                    }
                    else {
                        newVar = null;
                    }
                    nextVariable = false;
                    isDatatype = false;
                }
                else {
                    nextVariable = false;
                    isDatatype = false;
                }
            }
        }
    }
    public void open_file(String pfile_name) {
        if (new Filesystem().does_file_exist(pfile_name)) {
            this.file_name = pfile_name;
            this.tab_manager.setTitleAt(this.tab_index, new File(this.file_name).getName());
            this.text_area.setText(new Filesystem().read(this.file_name));
            this.unsaved_changes[0] = false;
            if (Pattern.compile("[\\/]*.java", Pattern.CASE_INSENSITIVE).matcher(this.file_name).find()) {
                this.program = "java";
            }
            else if (Pattern.compile("[\\/]*.py", Pattern.CASE_INSENSITIVE).matcher(this.file_name).find()) {
                this.program = "python3";
            }
        }
        else {
            JOptionPane.showMessageDialog(this.tab_manager, "This file could not be found: \n" + pfile_name);
        }
    }

    public int get_index() {
        return this.tab_index;
    }

    public void choose_language() {
        String temp = JOptionPane.showInputDialog(this.tab_manager, "What program should run your code?\nOnly use valid options!", this.program);
        if (!("".equals(temp) || temp == null)) {
            this.program = temp;
        }
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
            fileChooser.setDialogTitle("Specify where to save this file in order to run it");

            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                this.file_name = fileChooser.getSelectedFile().getAbsolutePath();
            }
            else {
                return;
            }
            this.tab_manager.setTitleAt(this.tab_index, new File(this.file_name).getName());
        }
        new Filesystem().write(this.file_name, this.text_area.getText());
        this.unsaved_changes[0] = false;
    }

    public void execute() {
        save_document();
        if (this.file_name == "") {
            if (this.file_name == "") {
                JOptionPane.showMessageDialog(this.tab_manager, "You need to save your files in order to run them!");
                return;
            }
        }

        try {
            Process Demo_Process = new ProcessBuilder(this.program, this.file_name).inheritIO().start();
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
                if (file.equals(this.file_name)) {
                    file_already_in_list = true;
                    return true;
                }
            }
            if (!file_already_in_list) {
                new Filesystem().write(".recent_files", this.file_name + "\n" + new Filesystem().read(".recent_files"));
                this.root.update_recent_file_list();
            }
        }
        return true;
    }
}
