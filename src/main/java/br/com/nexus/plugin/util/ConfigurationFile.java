package br.com.nexus.plugin.util;

import br.com.nexus.plugin.Main;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;

@RequiredArgsConstructor
public class ConfigurationFile {

    private final Main main;

    @SneakyThrows
    public void loadConfig() {
        File file = getFile();
        if(!hasFile(file)) createFile(file);
        Configuration configuration = getConfigurationByFile(file);
        setDefaultConfiguration(configuration);
        saveConfig(file, configuration);
    }

    private void setDefaultConfiguration(Configuration configuration) {
        if(!configuration.contains("MySQL")) setDefaultMsSQLDB(configuration);
    }

    private void setDefaultMsSQLDB(Configuration configuration) {
        configuration.set("MySQL.user", "root");
        configuration.set("MySQL.host", "127.0.0.1:3306");
        configuration.set("MySQL.database", "test");
        configuration.set("MySQL.password", "");
    }

    @SneakyThrows
    public Configuration getConfig() {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());
    }

    @SneakyThrows
    public void saveConfig(Configuration configuration) {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, getFile());
    }

    @SneakyThrows
    private void saveConfig(File file, Configuration configuration) {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
    }

    private File getFile() {
        if(!hasDataFolder()) createDataFolder();
        return new File(main.getDataFolder(), "configuration.yml");
    }

    private boolean hasDataFolder() {
        return main.getDataFolder().exists();
    }

    private void createDataFolder() {
        main.getDataFolder().mkdir();
    }

    @SneakyThrows
    private Configuration getConfigurationByFile(File file) {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }

    private boolean hasFile(File file) {
        return file.exists();
    }

    @SneakyThrows
    private void createFile(File file) {
        file.createNewFile();
    }


}
