package priv.snowraincloud.opengistcreate.dialog;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RenameableTabbedPane extends JBTabbedPane {

    public RenameableTabbedPane() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int tabIndex = getUI().tabForCoordinate(RenameableTabbedPane.this, e.getX(), e.getY());
                    if (tabIndex >= 0) {
                        renameTab(tabIndex);
                    }
                }
            }
        });
    }

    private void renameTab(int tabIndex) {
        String currentTitle = getTitleAt(tabIndex);
        String newTitle = JOptionPane.showInputDialog(this, "Enter new name for the tab:", currentTitle);
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            setTitleAt(tabIndex, newTitle);
        }
    }
}
