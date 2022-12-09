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
        public void actionPerformed(ActionEvent evt) {
            close_file(true);
        }
    }

    public class TokenAddress {
        public Token t;
        public int address;

        public TokenAddress(int address, Token t) {
            this.t = t;
            this.address = address;
        }
    }

    public class TokenText {
        public class Line {
            public class TokenElement {
                public TokenAddress t;
                private TokenElement next;
                private TokenElement prev;

                public TokenElement(TokenElement prev, TokenElement next) {
                    this.prev = prev;
                    this.next = next;

                }
            }

            // protected TokenElement[] tokens;
            // protected TokenElement currentToken;
            private TokenElement start;
            private TokenElement end;
            private TokenElement iterator;
            public int rangeMin;
            public int rangeMax;

            public Line() {
                start = null;
                end = null;
                iterator = null;
                rangeMin = 0;
                rangeMax = 0;
            }

            public TokenAddress Iterate() {
                if (iterator != null) {
                    iterator = iterator.next;

                    return iterator != null ? iterator.t : null;

                } else
                    return null;
            }

            public TokenAddress resetIteration() {
                iterator = start;
                return start.t;
            }

            public void removeEverythingAfterIterator() {
                if (iterator != null) {
                    end = iterator;
                    end.next = null;
                    rangeMax = end.t.address + end.t.t.tokenName.length();
                } else {
                    start = null;
                    end = null;
                    rangeMin = 0;
                    rangeMax = 0;
                }
            }

            public void addSorted(TokenAddress t) {
                if (start == null || end == null) {
                    start = new TokenElement(null, null);
                    rangeMax = t.address + t.t.tokenName.length();
                    rangeMin = t.address;
                    start.t = t;
                    end = start;
                } else if (start == end) {
                    if (start.t.address < t.address) {
                        start = new TokenElement(null, end);
                        start.t = t;
                        end.prev = start;
                        rangeMin = t.address;
                    } else {
                        end = new TokenElement(start, null);
                        end.t = t;
                        start.next = end;
                        rangeMax = t.address + t.t.tokenName.length();
                    }
                } else {
                    TokenElement el = start;
                    boolean inserted = false;
                    do {
                        if (el.t.address > t.address) {
                            if (el == start) {
                                rangeMin = t.address;
                            }
                            TokenElement pre = el.prev;
                            el.prev = new TokenElement(pre, el);
                            if (pre != null) {
                                pre.next = el.prev;
                            }
                            inserted = true;
                            break;
                        }
                        el = el.next;
                    } while (el != null);
                    if (!inserted) {
                        // is at end
                        TokenElement pre = end;
                        end = new TokenElement(pre, null);
                        pre.next = end;
                        rangeMax = t.address + t.t.tokenName.length();

                    }
                }
            }

            public void append(TokenAddress t) {
                if (start == null || end == null) {
                    start = new TokenElement(null, null);
                    rangeMax = t.address + t.t.tokenName.length();
                    rangeMin = t.address;
                    start.t = t;
                    end = start;
                } else if (start == end) {
                    end = new TokenElement(start, null);
                    start.next = end;
                    end.t = t;
                    rangeMax = t.address + t.t.tokenName.length();
                } else {
                    TokenElement pre = end;
                    end = new TokenElement(pre, null);
                    pre.next = end;
                    end.t = t;
                    rangeMax = t.address + t.t.tokenName.length();

                }
            }

            public void removeCurrentToken() {
                if (iterator != null) {
                    if (iterator.next != null && iterator.prev != null) {
                        TokenElement pre = iterator;
                        iterator.next.prev = iterator.prev;
                        iterator.prev.next = iterator.next;
                        iterator = iterator.prev;
                        pre.next = null;
                        pre.prev = null;
                    } else if (iterator == end) {
                        // is at end
                        TokenElement pre = end;
                        end = end.prev;
                        pre.prev = null;
                        if (end != null) {
                            rangeMax = end.t.address + end.t.t.tokenName.length();
                        }
                    }
                    if (iterator == start) {
                        TokenElement pre = start;
                        start = start.next;
                        pre.next = null;
                        if (start != null) {
                            rangeMin = start.t.address;
                        }
                    }
                }
            }

        }

        public class Lineholder {
            public Line line;
            public Lineholder prev;
            public Lineholder next;

            public Lineholder(Lineholder prev, Lineholder next) {
                this.prev = prev;
                this.next = next;
            }

        }

        private Lineholder start;
        private Lineholder end;

        // private Lineholder iterator;
        public TokenText() {
            start = null;
            end = null;
            // iterator=null;
        }

        public Line getLineAt(int index) {
            Lineholder iterator = start;

            for (int i = 0; iterator != null; iterator = iterator.next, i++) {
                if (i == index) {
                    return iterator.line;
                }
            }
            return null;
        }

        public void insertLineAt(int index, Line l) {
            if (start == null) {
                start = new Lineholder(null, null);
                start.line = l;
                end = start;
            } else if (start == end) {
                if (index == 0) {
                    start = new Lineholder(null, end);
                    start.line = l;
                    end.prev = start;
                }
            } else {
                Lineholder el = start;
                boolean inserted = false;
                Lineholder iterator = start;

                for (int i = 0; iterator != null; iterator = iterator.next, i++) {
                    if (i == index) {
                        Lineholder pre = el.prev;
                        el.prev = new Lineholder(pre, el);
                        if (pre != null) {
                            pre.next = el.prev;
                        }
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    // is at end
                    Lineholder pre = end;
                    end = new Lineholder(pre, null);
                    pre.next = end;

                }
            }
        }

        public void deleteLineAt(int index) {
            Lineholder iterator = start;
            for (int i = 0; iterator != null; iterator = iterator.next, i++) {
                if (i == index) {
                    break;
                }
            }
            if (iterator != null) {
                if (iterator.next != null && iterator.prev != null) {
                    Lineholder pre = iterator;
                    iterator.next.prev = iterator.prev;
                    iterator.prev.next = iterator.next;
                    iterator = iterator.prev;
                    pre.next = null;
                    pre.prev = null;
                } else if (iterator == end) {
                    // is at end
                    Lineholder pre = end;
                    end = end.prev;
                    pre.prev = null;

                }
                if (iterator == start) {
                    Lineholder pre = start;
                    start = start.next;
                    pre.next = null;
                }
            }
        }
    }

    public boolean[] unsaved_changes = { false }; // make bool pointer
    public String file_name = "";
    public String program = "";
    public final JTextPane text_area;
    public int tab_index;
    public JTabbedPane tab_manager;
    public TextHighlighter highlighter;
    public Window root;
    public int lastDot;
    public JLabel lblTitle;
    public TokenText tokenText;
    public String prevText;
    public final Token newVarPreset;
    public Line_Number_Inserter lNI;
    Text_Tab(JTabbedPane ptab_manager, Window root, String pfile_name) {
        this.root = root;
        this.tab_manager = ptab_manager;
        newVarPreset = new Token("preset", 0x0000FFFF, Token.TokenType.TYPE_VARIABLE);
        System.out.println(pfile_name);
        this.text_area = new JTextPane();
        this.text_area.setFont(new Font("Hack", Font.PLAIN, 13));
        highlighter = new TextHighlighter(this);
        JTextPane line_numbers = new JTextPane();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);
        lastDot = 0;
        lNI=new Line_Number_Inserter(this.text_area, line_numbers, unsaved_changes);
        this.text_area.getDocument()
                .addDocumentListener(lNI);
        this.text_area.getDocument().addDocumentListener(highlighter);
        JScrollPane scroll_plane = new JScrollPane(this.text_area);
        text_area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                prevText = text_area.getText();
            }
        });
        scroll_plane.getViewport().add(this.text_area);
        scroll_plane.setRowHeaderView(line_numbers);

        this.tab_manager.add("New", scroll_plane);

        this.tab_index = this.tab_manager.getTabCount() - 1;

        // *
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);

        this.lblTitle = new JLabel("New ");

        ImageIcon xicon = new ImageIcon("xicon");
        JButton btnClose = new JButton("", xicon);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        // btnClose.setRolloverIcon(xicon);
        tokenText = new TokenText();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(this.lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        this.tab_manager.setTabComponentAt(this.tab_index, pnlTab);

        btnClose.addActionListener(new MyCloseActionHandler());
        // */

        if (pfile_name != "") {
            open_file(pfile_name);
        }

        this.unsaved_changes[0] = false;

        // Make this the selected tab
        this.tab_manager.setSelectedIndex(this.tab_index);
    }
    private class ResultCache{
        public int lineCounter;
        public int charCounter;
        public int currentLineStart;
        public int opIndex;
        public String text;
        public ResultCache(){
            lineCounter = 0; 
            charCounter = 0;
            currentLineStart = 0;
        }
    }
    //Simple cache for better performance, could be more complex(more cache entries,sorting...)
    private ResultCache m_GTLCCache;
    private int getLineCountUntil(int index, String str, int[] additionalResults) {
        int lineCounter = 0;
        int charCounter = 0;//excludes lines
        int currentLineStart = 0;
        if(m_GTLCCache==null){
            m_GTLCCache=new ResultCache();
        }else if(m_GTLCCache.text==str&&m_GTLCCache.opIndex<index){
            lineCounter=m_GTLCCache.lineCounter;
            charCounter=m_GTLCCache.charCounter;
            currentLineStart=m_GTLCCache.currentLineStart;
        }
       
        for (int i = charCounter+lineCounter; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                lineCounter++;
                currentLineStart = charCounter;
            } else {
                charCounter++;
                if (charCounter >= index) {
                    if (additionalResults != null) {
                        additionalResults[0] = currentLineStart;
                    }

                    break;
                }
            }
        }
        for (int i = charCounter + lineCounter; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                if (additionalResults != null) {
                    additionalResults[1] = i;
                }
                break;
            }
        }
        if (additionalResults != null) {
            additionalResults[2] = charCounter;
        }
        m_GTLCCache.charCounter=charCounter;
        m_GTLCCache.lineCounter=lineCounter;
        m_GTLCCache.currentLineStart=currentLineStart;
        m_GTLCCache.opIndex=index;
        m_GTLCCache.text=str;
        return lineCounter;
    }

    private class bundle {
        int affactedLine;
        int lineStart;
    }

    // returns line end
    public int updateLine(int offset, int lineIndex, String text, TokenText.Line line) {

        int lastIndex = text.length();
        int realOffset = offset + lineIndex;
        for (int i = realOffset; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lastIndex = i;
                break;
            }
        }
        StyledDocument sDoc = this.text_area.getStyledDocument();

        // reset Style:
        SimpleAttributeSet colorAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(colorAttributeSet, Color.BLACK);
        StyleConstants.setBackground(colorAttributeSet, Color.WHITE);
        StyleConstants.setUnderline(colorAttributeSet, false);
        StyleConstants.setBold(colorAttributeSet, false);
        TokenAddress addr;
        sDoc.setCharacterAttributes(offset, lastIndex - (realOffset), colorAttributeSet, true);
        CharTreeGraph graph = highlighter.getGraph();

        boolean isDatatype = false;
        boolean isVar = false;
        // remove all Variables;
        for (addr = line.resetIteration(); addr != null; addr = line.Iterate()) {
            if (addr.t.type == Token.TokenType.TYPE_DATATYPE) {
                isDatatype = true;
            } else if (isDatatype) {
                graph.removeToken(addr.t);
                isDatatype = false;
            }

        }
        addr = line.resetIteration();
        int variableOffset = 0;
        String newVar = new String();

        for (int i = realOffset; i < lastIndex; i++) {
            if (!isDatatype) {
                Token t = graph.searchForToken(text.substring(i, lastIndex));
                if (t != null) {

                    // use local address
                    int address = i - realOffset;
                    if (addr != null) {
                        if (addr.t != t && addr.address != address) {
                            addr.t = t;
                            addr.address = address;
                        }
                        addr = line.Iterate();
                    } else {
                        line.append(new TokenAddress(address, t));
                    }

                    i += t.tokenName.length() - 1;
                }
            } else {
                if (text.charAt(i) == ' ' && !isVar) {
                    isVar = true;
                    variableOffset = i + 1;
                } else {
                    if (isVar && ((text.charAt(i) != ' ' && text.charAt(i) != '\n') && text.charAt(i) != ';')) {
                        newVar += text.charAt(i);

                    } else {
                        isVar = false;
                        isDatatype = false;
                        Token newToken = new Token(newVar, newVarPreset);
                        line.append(new TokenAddress(variableOffset - realOffset, newToken));
                        graph.addTokenToGraph(newToken);

                    }
                }
            }
        }
        line.removeEverythingAfterIterator();
        return lastIndex;
    }
    public void updateHighlighting(String text,int totalLineCount){
        StyledDocument sDoc = this.text_area.getStyledDocument();
        int currentLine=0;
        // reset Style:
        SimpleAttributeSet colorAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(colorAttributeSet, Color.BLACK);
        StyleConstants.setBackground(colorAttributeSet, Color.WHITE);
        StyleConstants.setUnderline(colorAttributeSet, false);
        StyleConstants.setBold(colorAttributeSet, false);
       
        sDoc.setCharacterAttributes(0, text.length()-totalLineCount, colorAttributeSet, true);
        CharTreeGraph graph = highlighter.getGraph();
        for (int i = 0; i < text.length()-totalLineCount; i++) {
                if(text.charAt(i)== '\n'){currentLine++;continue;}
                Token t = graph.searchForToken(text.substring(i, text.length()-totalLineCount));
                if (t != null) {
                    StyleConstants.setForeground(colorAttributeSet, new Color(t.fontColor));
                    StyleConstants.setBackground(colorAttributeSet, new Color(t.backgroundColor));
                    StyleConstants.setUnderline(colorAttributeSet, (t.flags & 0x01) == 0x01);
                    StyleConstants.setBold(colorAttributeSet, (t.flags & 0x02) == 0x02);

                    sDoc.setCharacterAttributes(i - currentLine, t.tokenName.length(), colorAttributeSet, true);
                    // check if token is datatype
                    i += t.tokenName.length() - 1;
                }
           
        }

    }
    public void checkForUpdates(boolean isDelete, int affactedAreaOffset, int affactedAreaSize) {
        // get current line
        // String affactedText = null;
        String text = this.text_area.getText();

        bundle[] affactedLine = new bundle[16];
        int[] additionalResults = new int[3];
        int currentAffactedLine = 0;
        if (isDelete) {
            // affactedText = prevText.substring(affactedAreaOffset, affactedAreaOffset +
            // affactedAreaSize);
            for (int i = affactedAreaOffset; i < affactedAreaOffset + affactedAreaSize; i++) {
                if (prevText.charAt(i) == '\n') {
                    // resize array
                    if (currentAffactedLine >= affactedLine.length) {
                        bundle[] newAffactedLine = new bundle[affactedLine.length * 2];
                        for (int k = 0; k < affactedLine.length; k++) {
                            newAffactedLine[k] = affactedLine[k];
                        }
                        affactedLine = newAffactedLine;
                    }
                    affactedLine[currentAffactedLine].affactedLine = getLineCountUntil(i, prevText, additionalResults);
                    affactedLine[currentAffactedLine].lineStart = additionalResults[0]
                            + affactedLine[currentAffactedLine].affactedLine;

                    currentAffactedLine++;
                }
            }
            int lineOffset=affactedLine[0].lineStart+affactedLine[0].affactedLine;
            for(int i=0; i<affactedLine.length; i++) {
                TokenText.Line l = tokenText.getLineAt(affactedLine[i].affactedLine);
                lineOffset=updateLine(lineOffset,affactedLine[i].affactedLine,text,l)+1;
            }

        } else {

            // affactedText = text_area.getText(affactedAreaOffset, affactedAreaSize);
            for (int i = affactedAreaOffset; i < affactedAreaOffset + affactedAreaSize; i++) {
                if (prevText.charAt(i) == '\n') {
                    // resize array
                    if (currentAffactedLine >= affactedLine.length) {
                        bundle[] newAffactedLine = new bundle[affactedLine.length * 2];
                        for (int k = 0; k < affactedLine.length; k++) {
                            newAffactedLine[k] = affactedLine[k];
                        }
                        affactedLine = newAffactedLine;
                    }
                    affactedLine[currentAffactedLine].affactedLine = getLineCountUntil(i, text, additionalResults);
                    affactedLine[currentAffactedLine].lineStart = additionalResults[0]
                            + affactedLine[currentAffactedLine].affactedLine;
                    currentAffactedLine++;
                }
            }
            int lineOffset=affactedLine[0].lineStart+affactedLine[0].affactedLine;
            for (int i = 0; i < currentAffactedLine; i++) {
                TokenText.Line l = tokenText.getLineAt(affactedLine[i].affactedLine);
                // check if line is fully inside deleted range
              
                if (l.rangeMin + affactedLine[i].lineStart >= affactedAreaOffset
                        && l.rangeMax + affactedLine[i].lineStart <= affactedAreaOffset + affactedAreaSize) {

                    // remove all Variables;
                    boolean isDatatype = false;
                    CharTreeGraph graph = highlighter.getGraph();
                    for (TokenAddress addr = l.resetIteration(); addr != null; addr = l.Iterate()) {
                        if (addr.t.type == Token.TokenType.TYPE_DATATYPE) {
                            isDatatype = true;
                        } else if (isDatatype) {
                            graph.removeToken(addr.t);
                            isDatatype = false;
                        }

                    }
                    tokenText.deleteLineAt(affactedLine[i].affactedLine);
                } else {
                    lineOffset=updateLine(lineOffset,affactedLine[i].affactedLine,text,l)+1;
                }

            }
        }
        updateHighlighting(text,lNI.getLineCount());    
        
    }

    private void update_language() {
        if (Pattern.compile("[\\/]*.java", Pattern.CASE_INSENSITIVE).matcher(this.file_name).find()) {
            this.program = "java";
        } else if (Pattern.compile("[\\/]*.py", Pattern.CASE_INSENSITIVE).matcher(this.file_name).find()) {
            this.program = "python3";
        }
    }

    public void open_file(String pfile_name) {
        if (new Filesystem().does_file_exist(pfile_name)) {
            this.file_name = pfile_name;
            // this.tab_manager.setTitleAt(this.tab_index, new
            // File(this.file_name).getName());
            lblTitle.setText(new File(this.file_name).getName() + " ");
            this.text_area.setText(new Filesystem().read(this.file_name));
            this.unsaved_changes[0] = false;
            update_language();
        } else {
            JOptionPane.showMessageDialog(this.tab_manager, "This file could not be found: \n" + pfile_name);
        }
    }

    public int get_index() {
        return this.tab_index;
    }

    public void choose_language() {
        String temp = JOptionPane.showInputDialog(this.tab_manager,
                "What program should run your code?\nOnly use valid options!", this.program);
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
            } else {
                return;
            }
            update_language();
            // this.tab_manager.setTitleAt(this.tab_index, new
            // File(this.file_name).getName());
            lblTitle.setText(new File(this.file_name).getName() + " ");
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
            new ProcessBuilder(this.program, this.file_name).inheritIO().start();
        } catch (IOException e) {
        }
    }

    public boolean close_file(boolean respawn) {
        if (this.unsaved_changes[0]) {
            int reply = JOptionPane.showConfirmDialog(null, "Do you want to save this document? ",
                    "Save" + this.file_name + " ?", JOptionPane.YES_NO_CANCEL_OPTION);

            if (reply != JOptionPane.NO_OPTION && reply != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        this.tab_manager.remove(this.tab_index);

        if (respawn && tab_manager.getTabCount() == 0) {
            this.root.new_tab("");
        }

        if (this.file_name != "") {
            boolean file_already_in_list = false;
            String result = this.file_name + "\n";
            for (String file : new Filesystem().read(".recent_files").split("\n")) {
                if (new Filesystem().does_file_exist(file) && (!file.equals(this.file_name))) {
                    result += file + "\n";
                }
            }
            new Filesystem().write(".recent_files", result);
            this.root.update_recent_file_list();
        }
        return true;
    }
}
