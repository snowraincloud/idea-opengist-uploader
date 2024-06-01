package priv.snowraincloud.opengistcreate.opengist;

import com.intellij.ide.BrowserUtil;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import priv.snowraincloud.opengistcreate.config.MyPluginSettingsState;
import priv.snowraincloud.opengistcreate.model.OpenGist;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class CreateService {


    public static Optional<String> createGist(OpenGist openGist) {
        MyPluginSettingsState instance = MyPluginSettingsState.getInstance();
        if (instance.url.isEmpty() || instance.session.isEmpty() || instance.csrf.isEmpty()) {
            return Optional.of("Please set the url, cookie and csrf in the settings");
        }


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("title", openGist.title())
                .addFormDataPart("description", openGist.description())
                .addFormDataPart("url", openGist.ulr())
                .addFormDataPart("_csrf", instance.csrf)
                .addFormDataPart("private", String.valueOf(openGist.visibility()));
        for (var file : openGist.files()) {
            builder.addFormDataPart("name", file.fileName())
                    .addFormDataPart("content", file.fileContent());
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder().url(instance.url)
                .method("POST", body)
                .addHeader("Cookie", "session=" + instance.session + ";_csrf=" + instance.csrf)
                .build();
        try (Response response = client.newCall(request)
                .execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // 获取响应的 HTML 内容
                String html = response.body()
                        .string();
                // 使用 Jsoup 解析 HTML
                Document document = Jsoup.parse(html);
                // 获取 <title> 标签的值
                String title = document.title();
                title = title.strip();
                if (title.startsWith(openGist.title())) {
                    URL url = response.request()
                            .url()
                            .url();
                    if (instance.isOpenBrowser) {
                        BrowserUtil.browse(url);
                    }
                    return Optional.empty();
                }
                return Optional.of("Failed to create gist, please check the session in the settings");
            } else if (response.code() == 403) {
                return Optional.of("403 Forbidden, please check the csrf in the settings");
            }
            return Optional.of("Failed to create gist");
        } catch (IOException e) {
            return Optional.of("Failed to create gist: " + e.getMessage());
        }
    }
}
