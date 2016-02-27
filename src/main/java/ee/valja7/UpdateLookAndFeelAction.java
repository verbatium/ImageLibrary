package ee.valja7;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by valeri on 27.02.16.
 */
public class UpdateLookAndFeelAction implements ActionListener {
    private JList<String> list;
    private JFrame rootFrame;

    public UpdateLookAndFeelAction(JFrame frame, JList<String> list) {
        this.rootFrame = frame;
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String lookAndFeelName = list.getSelectedValue();
        UIManager.LookAndFeelInfo[] infoArray =
                UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo info : infoArray) {
            if (info.getName().equals(lookAndFeelName)) {
                String message = "Look&feel was changed to " + lookAndFeelName;
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(rootFrame);
                } catch (ClassNotFoundException e1) {
                    message = "Error: " + info.getClassName() + " not found";
                } catch (InstantiationException e1) {
                    message = "Error: instantiation exception";
                } catch (IllegalAccessException e1) {
                    message = "Error: illegal access";
                } catch (UnsupportedLookAndFeelException e1) {
                    message = "Error: unsupported look and feel";
                }
                JOptionPane.showMessageDialog(null, message);
                break;
            }
        }
    }
}
