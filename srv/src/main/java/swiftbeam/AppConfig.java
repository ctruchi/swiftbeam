package swiftbeam;

import restx.config.Settings;
import restx.config.SettingsKey;

@Settings
public interface AppConfig {

    @SettingsKey(key = "base.path")
    String basePath();
}
