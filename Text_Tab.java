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

            private int length;
            private int offset;

            public Line() {
                start = null;
                end = null;
                iterator = null;

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
                return iterator == null ? null : iterator.t;
            }
            public void debugPrint(){
                System.out.println("*----------------Line---------------*");
                System.out.println("Length: " + length);
                System.out.println("Offset: "+ offset);
                int i=0;
                for(TokenAddress addr= resetIteration();addr != null;addr=Iterate(),i++){
                    System.out.println("*-------------Token-----------*");
                    System.out.println("Entry: "+i);
                    System.out.println("Name: "+addr.t.tokenName);
                    System.out.println("Address: "+addr.address);
                    System.out.println("Type: "+addr.t.type);
                    System.out.println("IsValid: "+addr.t.isValid);

                }

            }
            public int getOffset() {
                return offset;
            }

            public int getLength() {
                return length;
            }

            

            public void removeEverythingAfterIterator() {
                if (iterator != null) {
                    end = iterator;
                    if(end!=null){
                        end.next.prev=null;
                        end.next = null;
                    }
               

                }
            }
            public void removeEverythingAfterIteratorInc() {
                if (iterator != null) {
                    if(iterator==start){
                        start=null;
                        end=null;
                    }else{
                        
                        end = iterator.prev;
                 
                        if(end!=null){
                            end.next.prev=null;
                            end.next = null;
                        }
                    }
                   
                    
                }
            }
            public void addSorted(TokenAddress t) {
                if (start == null || end == null) {
                    start = new TokenElement(null, null);

                    start.t = t;
                    end = start;
                } else if (start == end) {
                    if (start.t.address < t.address) {
                        start = new TokenElement(null, end);
                        start.t = t;
                        end.prev = start;

                    } else {
                        end = new TokenElement(start, null);
                        end.t = t;
                        start.next = end;
                    }
                } else {
                    TokenElement el = start;
                    boolean inserted = false;
                    do {
                        if (el.t.address > t.address) {

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

                    }
                }
            }

            public void append(TokenAddress t) {
                if (start == null || end == null) {
                    start = new TokenElement(null, null);

                    start.t = t;
                    end = start;
                } else if (start == end) {
                    end = new TokenElement(start, null);
                    start.next = end;
                    end.t = t;

                } else {
                    TokenElement pre = end;
                    end = new TokenElement(pre, null);
                    pre.next = end;
                    end.t = t;

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

                        }
                    }
                    if (iterator == start) {
                        TokenElement pre = start;
                        start = start.next;
                        pre.next = null;
                        if (start != null) {

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
        private Lineholder iterator;

        // private Lineholder iterator;
        public TokenText() {
            start = null;
            end = null;
            iterator = null;
            // iterator=null;
        }

        public Line createNewLine() {
            return new Line();
        }

        public Line resetIteration() {
            iterator = start;
            return start.line;
        }

        public Line iterate() {
            iterator = iterator.next;
            return iterator == null ? null : iterator.line;
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

        public void updateOffsets() {
            int currentOffset = 0;
       
            for (Line l = resetIteration(); l != null; l = iterate()) {
                l.offset = currentOffset;
                currentOffset += l.length;
            }
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
                } else {
                    end = new Lineholder(start, null);
                    end.line = l;
                    start.next = end;
                }
            } else {
            
                boolean inserted = false;
                Lineholder iterator = start;

                for (int i = 0; iterator != null; iterator = iterator.next, i++) {
                    if (i == index) {
                        Lineholder pre = iterator.prev;
                        iterator.prev = new Lineholder(pre, iterator);
                        if (pre != null) {
                            pre.next = iterator.prev;
                        }
                        iterator.prev.line=l;
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    // is at end
                    Lineholder pre = end;
                    end = new Lineholder(pre, null);
                    end.line= l;
                    pre.next = end;
                   

                }
            }
        }
        public void debugPrint(int id){
            System.out.println("*------------------------------TX--------------------------------*");
            System.out.println("DebugPrint of Tokentext: ( "+id+" )");
            int i=0;
            for(Line l= resetIteration();l!=null;l= iterate(),i++){
                System.out.println("Printing Line "+i+": ");
                l.debugPrint();
                System.out.println("*-----------------------End of Line-------------------------*");
            }
            System.out.println("End of Debugprint ( "+id+" )"); 
            System.out.println("*------------------------------ETX--------------------------------*");       

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
    private int editCycle;
    Text_Tab(JTabbedPane ptab_manager, Window root, String pfile_name) {
        this.root = root;
        this.tab_manager = ptab_manager;
        newVarPreset = new Token("preset", 0x00FF0000, Token.TokenType.TYPE_VARIABLE);
        System.out.println(pfile_name);
        this.text_area = new JTextPane();
        this.text_area.setFont(new Font("Hack", Font.PLAIN, 13));
        highlighter = new TextHighlighter(this);
        JTextPane line_numbers = new JTextPane();
        line_numbers.setFont(new Font("Hack", Font.PLAIN, 13));
        line_numbers.setBackground(Color.LIGHT_GRAY);
        line_numbers.setEditable(false);
        lastDot = 0;
        lNI = new Line_Number_Inserter(this.text_area, line_numbers, unsaved_changes);
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
        editCycle=0;
    }

    private class ResultCache {
        public int lineCounter;
        public int charCounter;
        public int currentLineStart;
        public int opIndex;
        public String text;

        public ResultCache() {
            lineCounter = 0;
            charCounter = 0;
            currentLineStart = 0;
        }
    }

    // Simple cache for better performance, could be more complex(more cache
    // entries,sorting...)
    private ResultCache m_GTLCCache;

    private int getLineCountUntil(int index, String str, int[] additionalResults) {
        int lineCounter = 0;
        int charCounter = 0;// excludes lines
        int currentLineStart = 0;
        int lastIndex = str.length();
        if (m_GTLCCache == null) {
            m_GTLCCache = new ResultCache();
        } else if (m_GTLCCache.text == str && m_GTLCCache.opIndex < index) {
            lineCounter = m_GTLCCache.lineCounter;
            charCounter = m_GTLCCache.charCounter;
            currentLineStart = m_GTLCCache.currentLineStart;
        }

        for (int i = charCounter; i < str.length(); i++) {

            if (i + 1 < str.length()) {
                if (str.charAt(i) == (char) 13 && str.charAt(i + 1) == '\n') {
                    lineCounter++;
                    currentLineStart = charCounter+1;
                    charCounter++;
                    i++;
                }
            }
            if (charCounter-lineCounter>= index) {
                if (additionalResults != null) {
                    additionalResults[0] = currentLineStart;
                }

                break;
            }
            charCounter++;

        }
        for (int i = charCounter; i < str.length(); i++) {
            if (i + 1 < str.length()) {
                if (str.charAt(i) == (char) 13 && str.charAt(i + 1) == '\n') {
                     lastIndex=i;
                     break;
                }
            }
        }
        if (additionalResults != null) {
            additionalResults[1] = lastIndex;// abs
            additionalResults[2] = lastIndex - lineCounter;// rel
        }
        m_GTLCCache.charCounter = lastIndex;
        m_GTLCCache.lineCounter = lineCounter;
        m_GTLCCache.currentLineStart = currentLineStart;
        m_GTLCCache.opIndex = index;
        m_GTLCCache.text = str;
        return lineCounter;
    }

    // returns line end
    public int updateLine(int offset, int lineIndex, int endOfLine, String text, TokenText.Line line) {
        String newVar = new String();
        int lastIndex = text.length();
        int realOffset = offset;
        //System.out.println("char: "+(int)text.charAt(offset));
        if (endOfLine == -1) {
            for (int i = realOffset; i < text.length(); i++) {
                if (i + 1 < text.length()) {
                    if (text.charAt(i) == (char) 13 && text.charAt(i + 1) == '\n') {
                         lastIndex=i;
                         break;
                    }
                }
            }
        } else {
            lastIndex = endOfLine;
        }
        line.length = lastIndex - realOffset;
        TokenAddress addr;
        CharTreeGraph graph = highlighter.getGraph();

        boolean isDatatype = false;
        boolean isVar = false;
        CharTreeGraph removedTokens= highlighter.getRemovedTokenGraph();
        // remove all Variables;
        for (addr = line.resetIteration(); addr != null; addr = line.Iterate()) {
            if (addr.t.type == Token.TokenType.TYPE_DATATYPE) {
                isDatatype = true;
            } else if (isDatatype) {
                //resize if needed
                removedTokens.addTokenToGraph(graph.removeToken(addr.t),false);
                isDatatype = false;
            }

        }
        isDatatype = false;
        addr = line.resetIteration();
        
        
        int address=0;
        for (int i = realOffset; i < lastIndex; i++) {
             
            if (!isDatatype) {
                Token t = graph.searchForToken(text.substring(i, lastIndex));
                if (t != null) {
                        // use local address
                    address = i - realOffset;
                

                    if (addr != null) {
                         
                        addr.t = t;
                        addr.address = address;
                        addr = line.Iterate();
                    } else {
                        line.append(new TokenAddress(address, t));
                    }
                    if (t.type == Token.TokenType.TYPE_DATATYPE) {
                        isDatatype = true;
                    }
                    i += t.tokenName.length() - 1;
                }
            } else {
                if (text.charAt(i) == ' ' && !isVar) {
                    isVar = true;
                    address = i-realOffset + 1;
                } else {
                    if (isVar && ((text.charAt(i) != ' ' && text.charAt(i) != '\n') && text.charAt(i) != ';')) {
                        newVar += text.charAt(i);

                    } else {
                        if (newVar.length() != 0) {
                            isVar = false;
                            isDatatype = false;
                            Token newToken = removedTokens.searchForToken(newVar);
                            if(newToken==null){
                                newToken= new Token(newVar,newVarPreset);
                            }
                            graph.addTokenToGraph(newToken,true);
                            if (addr != null) {
                                addr.t = newToken;
                                addr.address=address;
                                addr=line.Iterate();
                            } else {
                                line.append(new TokenAddress(address, newToken));
                            }
                            newVar= new String();
                        }

                    }
                }
            }
        }
        if(newVar.length()!=0){
            Token newToken = removedTokens.searchForToken(newVar);
            if(newToken==null){
                newToken= new Token(newVar,newVarPreset);
            }
            graph.addTokenToGraph(newToken,true);
            if (addr != null) {
                addr.t = newToken;
                addr.address=address;
                addr=line.Iterate();
            } else {
                line.append(new TokenAddress(address, newToken));
            }
        }
        line.removeEverythingAfterIteratorInc();
        return lastIndex;
    }

    public void updateHighlighting(String text, int totalLineCount) {
        StyledDocument sDoc = this.text_area.getStyledDocument();

        // reset Style:
        SimpleAttributeSet colorAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(colorAttributeSet, Color.BLACK);
        StyleConstants.setBackground(colorAttributeSet, Color.WHITE);
        StyleConstants.setUnderline(colorAttributeSet, false);
        StyleConstants.setBold(colorAttributeSet, false);

        sDoc.setCharacterAttributes(0, text.length() - totalLineCount, colorAttributeSet, true);

 

        for (TokenText.Line line = tokenText.resetIteration(); line != null; line = tokenText.iterate()) {
            for (TokenAddress addr = line.resetIteration(); addr != null; addr = line.Iterate()) {
                if (addr.t.isValid) {//new Color(addr.t.backgroundColor)
                    StyleConstants.setForeground(colorAttributeSet, new Color(addr.t.fontColor));
                    StyleConstants.setBackground(colorAttributeSet,new Color(addr.t.backgroundColor) );
                    StyleConstants.setUnderline(colorAttributeSet, (addr.t.flags & 0x01) == 0x01);
                    StyleConstants.setBold(colorAttributeSet, (addr.t.flags & 0x02) == 0x02);

                    sDoc.setCharacterAttributes(line.getOffset() + addr.address, addr.t.tokenName.length(),
                            colorAttributeSet, true);
                }

            }

        }

    }

    private class Bundle {
        int affactedLine;
        int lineStart;
        int lineEnd;
    }

    public void checkForUpdates(boolean isDelete, int affactedAreaOffset, int affactedAreaSize) {
        // get current line
        // String affactedText = null;
        String text = this.text_area.getText();

        Bundle[] affactedLine = new Bundle[16];
        int[] additionalResults = new int[3];
        // [0]=> start of line (absoulute)
        // [1]=> end of line (absolute)
        // [2]=> number of chars(end,relative)[mainly only internly used]
        int currentAffactedLine = 0;
        if (isDelete) {
            // affactedText = prevText.substring(affactedAreaOffset, affactedAreaOffset +
            // affactedAreaSize);
            for (int i = affactedAreaOffset; i < affactedAreaOffset + affactedAreaSize; i++) {

                // resize array
                if (currentAffactedLine >= affactedLine.length) {
                    Bundle[] newAffactedLine = new Bundle[affactedLine.length * 2];
                    for (int k = 0; k < affactedLine.length; k++) {
                        newAffactedLine[k] = affactedLine[k];
                    }
                    affactedLine = newAffactedLine;
                }
                affactedLine[currentAffactedLine] = new Bundle();
                affactedLine[currentAffactedLine].affactedLine = getLineCountUntil(i, prevText, additionalResults);
                affactedLine[currentAffactedLine].lineStart = additionalResults[0];
                affactedLine[currentAffactedLine].lineEnd = -1;
                i = additionalResults[1];// end of line
                currentAffactedLine++;

            }
            // delete
            int lineOffset = affactedLine[0].lineStart;
            for (int i = 0; i < currentAffactedLine; i++) {
                TokenText.Line l = tokenText.getLineAt(affactedLine[i].affactedLine);
                // check if line is fully inside deleted range

                if (l.getOffset() >= affactedAreaOffset
                        && l.getLength() + l.getOffset() <= affactedAreaOffset + affactedAreaSize) {

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
                    lineOffset = updateLine(lineOffset, affactedLine[i].affactedLine, -1, text, l) + 1;
               
                }

            }

        } else {

            // affactedText = text_area.getText(affactedAreaOffset, affactedAreaSize);
            for (int i = affactedAreaOffset; i < affactedAreaOffset + affactedAreaSize; i++) {

                // resize array
                if (currentAffactedLine >= affactedLine.length) {
                    Bundle[] newAffactedLine = new Bundle[affactedLine.length * 2];
                    for (int k = 0; k < affactedLine.length; k++) {
                        newAffactedLine[k] = affactedLine[k];
                    }
                    affactedLine = newAffactedLine;
                }
                affactedLine[currentAffactedLine] = new Bundle();
                affactedLine[currentAffactedLine].affactedLine = getLineCountUntil(i, text, additionalResults);
                
                affactedLine[currentAffactedLine].lineStart = additionalResults[0];
                affactedLine[currentAffactedLine].lineEnd = additionalResults[1];
                i = additionalResults[1];// end of line
                currentAffactedLine++;

            }
            // add
            int lineOffset = affactedLine[0].lineStart;
            for (int i = 0; i < currentAffactedLine; i++) {
                TokenText.Line l = tokenText.getLineAt(affactedLine[i].affactedLine);
                if (l == null) {
                    l = tokenText.createNewLine();
                    tokenText.insertLineAt(affactedLine[i].affactedLine, l);
                }
                //l.debugPrint();
                lineOffset = updateLine(lineOffset, affactedLine[i].affactedLine, affactedLine[i].lineEnd, text, l) + 1;
            
                //l.debugPrint();
            }

        }
        
        tokenText.updateOffsets();
        //tokenText.debugPrint(editCycle);
        updateHighlighting(text, lNI.getLineCount());
        editCycle++;

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
            new ProcessBuilder("python3", "console_starter.py", this.program, this.file_name).inheritIO().start();
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
