package priv.snowraincloud.opengistcreate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import priv.snowraincloud.opengistcreate.config.MyPluginSettingsState;
import priv.snowraincloud.opengistcreate.dialog.FileEditorDialog;
import priv.snowraincloud.opengistcreate.model.SelectedFile;

import java.util.ArrayList;
import java.util.List;

public class MyPluginAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // selected text
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor != null) {

            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null) {

                VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
                if (file != null) {
                    // get the file name
                    String fileName = file.getName();
                    List<SelectedFile> selectedFileList = new ArrayList<>();
                    selectedFileList.add(new SelectedFile(fileName, selectedText, file.getFileType()));

                    Project project = e.getProject();

                    showContentDialog(project, selectedFileList);
                }
                return;
            }
        }

        // selected file
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (selectedFiles != null) {
            List<SelectedFile> selectedFileList = new ArrayList<>();
            for (VirtualFile file : selectedFiles) {
                Document document = FileDocumentManager.getInstance().getDocument(file);
                if (document != null) {
                    System.out.println(document.getText());
                    FileType fileType = file.getFileType();
                    selectedFileList.add(new SelectedFile(file.getName(), document.getText(), fileType));
                }
            }
            Project project = e.getProject();

            showContentDialog(project, selectedFileList);
            return;
        }

        Messages.showInfoMessage("No file selected", "No File Selected");
    }


    private void showContentDialog(Project project, List<SelectedFile> selectedFileList) {

        DialogWrapper dialogWrapper = new FileEditorDialog(project, selectedFileList);

        dialogWrapper.setSize(MyPluginSettingsState.getInstance().dialogWidth, MyPluginSettingsState.getInstance().dialogHeight);

        dialogWrapper.showAndGet();
    }
}
