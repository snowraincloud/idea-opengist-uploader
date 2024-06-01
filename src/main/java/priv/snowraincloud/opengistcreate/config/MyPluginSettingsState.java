package priv.snowraincloud.opengistcreate.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "MyPluginSettingsState",
    storages = @Storage("MyPluginSettings.xml")
)
public class MyPluginSettingsState implements PersistentStateComponent<MyPluginSettingsState> {

    public String url = "";
    public String session = "";
    public String csrf = "";
    public Integer visibility = 2;

    public Integer dialogWidth = 800;
    public Integer dialogHeight = 600;

    public Boolean isOpenBrowser = false;

    public static MyPluginSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(MyPluginSettingsState.class);
    }

    @Nullable
    @Override
    public MyPluginSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MyPluginSettingsState state) {
        this.url = state.url;
        this.session = state.session;
        this.csrf = state.csrf;
        this.dialogWidth = state.dialogWidth;
        this.dialogHeight = state.dialogHeight;
        this.isOpenBrowser = state.isOpenBrowser;
        this.visibility = state.visibility;
    }
}
