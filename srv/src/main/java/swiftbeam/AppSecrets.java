package swiftbeam;

import restx.config.Settings;
import restx.config.SettingsKey;

@Settings
public interface AppSecrets {

    @SettingsKey(key = "oauth.secrets.token")
    String oauthSecretsToken();

    @SettingsKey(key = "tvdb.apikey")
    String tvDbApiKey();
}
