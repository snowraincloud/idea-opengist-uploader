package priv.snowraincloud.opengistcreate.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.*;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class MyPluginSettingsConfigurable implements Configurable {

    private JTextField urlField;
    private JBTextArea cookieField;
    private JTextField csrfField;
    private JComboBox<String> visibilityComboBox;

    private JTextField dialogWidthField;
    private JTextField dialogHeightField;
    private JCheckBox isOpenBrowserCheckBox;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "My Plugin Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel panel = new JBPanel<>(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = JBUI.insets(5);
        gbc.weightx = 1.0;

        // isOpenBrowser configuration
        isOpenBrowserCheckBox = new JCheckBox("Open browser after creating Gist");
        isOpenBrowserCheckBox.setSelected(MyPluginSettingsState.getInstance().isOpenBrowser);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(isOpenBrowserCheckBox, gbc);

        // Add separator
        gbc.gridy++;
        panel.add(new JSeparator(), gbc);

        // Dialog configuration
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JBLabel("Dialog width:"), gbc);
        gbc.gridx++;
        dialogWidthField = new JBTextField();
        panel.add(dialogWidthField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("Dialog height:"), gbc);
        gbc.gridx++;
        dialogHeightField = new JBTextField();
        panel.add(dialogHeightField, gbc);

        // Add separator
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        // Open Gist request configuration
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JBLabel("URL:"), gbc);
        gbc.gridx++;
        urlField = new JBTextField();
        panel.add(urlField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("Session:"), gbc);
        gbc.gridx++;
        cookieField = new JBTextArea(5, 20);
//        cookieField.setMaximumSize(new Dimension(500, 200));
        JScrollPane scrollPane = new JBScrollPane(cookieField);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("_csrf:"), gbc);
        gbc.gridx++;
        csrfField = new JBTextField();
        panel.add(csrfField, gbc);

        // Visibility configuration
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("Visibility:"), gbc);
        gbc.gridx = 1;
        visibilityComboBox = new ComboBox<>(new String[]{"Public", "Unlisted", "Private"});
        panel.add(visibilityComboBox, gbc);


        return panel;
    }

    @Override
    public boolean isModified() {
        // Logic to check if settings have been modified
        return !urlField.getText().equals(MyPluginSettingsState.getInstance().url) ||
               !cookieField.getText().equals(MyPluginSettingsState.getInstance().session) ||
               !csrfField.getText().equals(MyPluginSettingsState.getInstance().csrf)
                || !dialogWidthField.getText().equals(MyPluginSettingsState.getInstance().dialogWidth.toString())
                || !dialogHeightField.getText().equals(MyPluginSettingsState.getInstance().dialogHeight.toString())
                || isOpenBrowserCheckBox.isSelected() != MyPluginSettingsState.getInstance().isOpenBrowser
                || visibilityComboBox.getSelectedIndex() != MyPluginSettingsState.getInstance().visibility;
    }

    @Override
    public void apply() {
        // Logic to save settings
        MyPluginSettingsState.getInstance().url = urlField.getText();
        MyPluginSettingsState.getInstance().session = cookieField.getText();
        MyPluginSettingsState.getInstance().csrf = csrfField.getText();
        MyPluginSettingsState.getInstance().dialogWidth = Integer.parseInt(dialogWidthField.getText());
        MyPluginSettingsState.getInstance().dialogHeight = Integer.parseInt(dialogHeightField.getText());
        MyPluginSettingsState.getInstance().isOpenBrowser = isOpenBrowserCheckBox.isSelected();
        MyPluginSettingsState.getInstance().visibility = visibilityComboBox.getSelectedIndex();
    }

    @Override
    public void reset() {
        // Logic to reset settings
        urlField.setText(MyPluginSettingsState.getInstance().url);
        cookieField.setText(MyPluginSettingsState.getInstance().session);
        csrfField.setText(MyPluginSettingsState.getInstance().csrf);
        dialogWidthField.setText(MyPluginSettingsState.getInstance().dialogWidth.toString());
        dialogHeightField.setText(MyPluginSettingsState.getInstance().dialogHeight.toString());
        isOpenBrowserCheckBox.setSelected(MyPluginSettingsState.getInstance().isOpenBrowser);
        visibilityComboBox.setSelectedIndex(MyPluginSettingsState.getInstance().visibility);
    }
}
