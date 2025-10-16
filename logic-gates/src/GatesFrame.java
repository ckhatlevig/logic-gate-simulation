import java.awt.*;
import javax.swing.*;

import gates.*;

public class GatesFrame extends JPanel{
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect(50, 50, 100, 100);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        GatesFrame panel = new GatesFrame();
        frame.add(panel);

        frame.setVisible(true);
        System.out.println("Hello, World!");
    }
}
