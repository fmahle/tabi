import javax.swing.*;
import java.awt.Font;
public class FrameBeispiel
{
    public static void main(String[] args)
    {
        JFrame root = new JFrame("Beispiel JFrame");
        root.setSize(800, 600);

        JTextArea text = new JTextArea();
        text.setFont(new Font("Hack", Font.PLAIN, 13));

        JScrollPane scroll_plane = new JScrollPane(text);

        root.getContentPane().add(scroll_plane);

        root.setVisible(true);
    }
}
