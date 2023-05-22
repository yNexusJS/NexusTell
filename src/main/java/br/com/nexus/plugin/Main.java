package br.com.nexus.plugin;

import br.com.nexus.plugin.Command.CommandR;
import br.com.nexus.plugin.Command.CommandTell;
import br.com.nexus.plugin.listener.PlayerListener;
import br.com.nexus.plugin.storage.HikariConnect;
import br.com.nexus.plugin.storage.database.DatabaseMethod;
import br.com.nexus.plugin.util.ConfigurationFile;
import br.com.nexus.plugin.util.SendTell;
import br.com.nexus.plugin.util.TextComponentUtil;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    private String prefix = "§5[NexusTell] ";
    private final HikariConnect hikariConnect = new HikariConnect();
    private final TextComponentUtil textComponentUtil = new TextComponentUtil();
    private final SendTell sendTell = new SendTell(textComponentUtil);
    private final ConfigurationFile configurationFile = new ConfigurationFile(this);
    private final DatabaseMethod databaseMethod = new DatabaseMethod(hikariConnect);

    @Override @SneakyThrows
    public void onEnable() {
        BungeeCord.getInstance().getConsole().sendMessage(textComponentUtil.createTextComponent(prefix + "§aPlugin iniciado"));
        BungeeCord.getInstance().getConsole().sendMessage(textComponentUtil.createTextComponent(prefix + "§aConfiguraçãos carregadas."));
        configurationFile.loadConfig();
        hikariConnect.MySQLConnectLoad(configurationFile.getConfig().getString("MySQL.host"), configurationFile.getConfig().getString("MySQL.database"),
                configurationFile.getConfig().getString("MySQL.user"), configurationFile.getConfig().getString("MySQL.password"));
        databaseMethod.createTable();
        BungeeCord.getInstance().getConsole().sendMessage(textComponentUtil.createTextComponent(prefix + "§aDatabase carregada."));
        getProxy().getPluginManager().registerListener(this, new PlayerListener(databaseMethod));
        getProxy().getPluginManager().registerCommand(this, new CommandTell(textComponentUtil, sendTell, databaseMethod));
        getProxy().getPluginManager().registerCommand(this, new CommandR(textComponentUtil, sendTell));
    }

    @Override
    public void onDisable() {
        BungeeCord.getInstance().getConsole().sendMessage(textComponentUtil.createTextComponent(prefix + "§cPlugin desligando..."));
        hikariConnect.hikariDataSource.close();
        BungeeCord.getInstance().getConsole().sendMessage(textComponentUtil.createTextComponent(prefix + "§cDatabase descarregada."));
    }
}
