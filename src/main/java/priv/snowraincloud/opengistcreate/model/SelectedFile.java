package priv.snowraincloud.opengistcreate.model;

import com.intellij.openapi.fileTypes.FileType;

public record SelectedFile(String fileName, String fileContent, FileType fileType) {
}
