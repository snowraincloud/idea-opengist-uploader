package priv.snowraincloud.opengistcreate.model;

import java.util.List;

public record OpenGist(String title, String description, String ulr, List<SelectedFile> files, Integer visibility) {
}
