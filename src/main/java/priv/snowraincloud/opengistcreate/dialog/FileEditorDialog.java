package priv.snowraincloud.opengistcreate.dialog;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;
import priv.snowraincloud.opengistcreate.config.MyPluginSettingsState;
import priv.snowraincloud.opengistcreate.model.OpenGist;
import priv.snowraincloud.opengistcreate.model.SelectedFile;
import priv.snowraincloud.opengistcreate.opengist.CreateService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileEditorDialog extends DialogWrapper {

    private final List<SelectedFile> files;
    private final JTextField titleField = new JBTextField();
    private final JTextField descriptionField = new JBTextField();
    private final JTextField urlField = new JBTextField();
    private JComboBox<String> visibilityComboBox;
    private final JBTabbedPane tabbedPane = new RenameableTabbedPane();
    private final Project project;

    public FileEditorDialog(Project project, List<SelectedFile> files) {
        super(project);
        this.files = files;
        this.project = project;
        setTitle("New Gist");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel metadataPanel = new JPanel(new GridLayout(4, 2));
        metadataPanel.add(new JLabel("Title"));
        metadataPanel.add(titleField);
        metadataPanel.add(new JLabel("Description"));
        metadataPanel.add(descriptionField);
        metadataPanel.add(new JLabel("URL"));
        metadataPanel.add(urlField);

        metadataPanel.add(new JLabel("Visibility"));
        visibilityComboBox = new ComboBox<>(new String[]{"Public", "Unlisted", "Private"});
        visibilityComboBox.setSelectedIndex(MyPluginSettingsState.getInstance().visibility);
        metadataPanel.add(visibilityComboBox);

        panel.add(metadataPanel, BorderLayout.NORTH);

        for (SelectedFile file : files) {
            String fileName = file.fileName();
            EditorTextField editorTextField = new EditorTextField(
                    EditorFactory.getInstance().createDocument(file.fileContent()),
                    this.project, file.fileType());

            JPanel filePanel = new JPanel(new BorderLayout());
            filePanel.add(editorTextField, BorderLayout.CENTER);
            tabbedPane.addTab(fileName, filePanel);
        }

        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        String title = titleField.getText();
        String description = descriptionField.getText();
        String url = urlField.getText();
        Integer visibility = visibilityComboBox.getSelectedIndex();

        List<SelectedFile> files = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String filename = tabbedPane.getTitleAt(i);
            Component component = tabbedPane.getComponentAt(i);
            if (component instanceof JPanel filePanel) {
                for (Component comp : filePanel.getComponents()) {
                    if (comp instanceof EditorTextField editorTextField) {
                        Document document = editorTextField.getDocument();
                        String fileContent = document.getText();
                        files.add(new SelectedFile(filename, fileContent, editorTextField.getFileType()));
                    }
                }
            }
        }

        OpenGist openGist = new OpenGist(title, description, url, files, visibility);
        Optional<String> gist = CreateService.createGist(openGist);
        if (gist.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Create Gist Successfully", "Create Gist", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        gist.ifPresent(s -> JOptionPane.showMessageDialog(null, s, "Create Gist", JOptionPane.ERROR_MESSAGE));
    }
}
