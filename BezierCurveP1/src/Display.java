import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Display {
    static String option;
    
    public static void gui() {
        JFrame f = new JFrame("Bezier Curves");
        BezierCurve t2 = new BezierCurve();

        f.setResizable(false);
        f.setSize(1200, 800);
        f.setLocation(350, 150);
        f.setBackground(Color.PINK);
        JButton b1 = new JButton("confirm");
        JLabel l1 = new JLabel("Select Sample Number:");
        option = "";

        String[] items = new String[] { "1", "3", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50" };
        JComboBox comBox = new JComboBox(items);
        comBox.setSelectedIndex(9);
        comBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                option = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
            }
        });

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(null, "tongyao");
                t2.setT(1.0/(Double.parseDouble(option)+1));
            }
        });

        JPanel p = new JPanel();
        p.setBackground(Color.LIGHT_GRAY);
        p.add(l1);
        p.add(comBox);
        p.add(b1);

        f.add(t2);
        f.add(p, BorderLayout.NORTH);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
