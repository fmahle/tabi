import javax.swing.*;
import java.awt.Font;
public class FrameBeispiel
{
    public static void main(String[] args)
    {
        JFrame root = new JFrame("Beispiel JFrame");
        root.setSize(200,200);


        JTextArea text = new JTextArea();
        text.setFont(new Font("Hack", Font.PLAIN, 20));
        root.add(text);

        root.setVisible(true);
    }
}
