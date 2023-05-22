package br.com.nexus.plugin.storage.database;

import br.com.nexus.plugin.Enum.TellType;
import br.com.nexus.plugin.model.PlayerModel;
import br.com.nexus.plugin.storage.HikariConnect;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DatabaseMethod {

    private final HikariConnect hikariConnect;

    public void createTable() throws SQLException {
        try(Connection connection = hikariConnect.hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `NexusTell`(`Player` VARCHAR(24), `Notification` VARCHAR(15));")) {
            preparedStatement.executeUpdate();
        }

    }

    public void setProxiedPlayer(ProxiedPlayer proxiedPlayer) throws SQLException {
        try (Connection connection = hikariConnect.hikariDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `NexusTell`(`Player`,`Notification`) VALUES (?, ?);")) {

            preparedStatement.setString(1, proxiedPlayer.getName());
            preparedStatement.setString(2, TellType.habilitado.getTellType());
            preparedStatement.executeUpdate();
        }
    }

    public Boolean hasProxiedPlayer(ProxiedPlayer proxiedPlayer) throws SQLException {
        ResultSet rs = null;
        try(Connection connection = hikariConnect.hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `NexusTell` WHERE `Player` = ?;")) {

            preparedStatement.setString(1, proxiedPlayer.getName());
            rs = preparedStatement.executeQuery();
            return rs.next();
        } finally {
            if(rs != null) rs.close();
        }
    }

    public PlayerModel getPlayerModelByProxiedPlayer(ProxiedPlayer proxiedPlayer) throws SQLException {
        ResultSet rs = null;
        try(Connection connection = hikariConnect.hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `NexusTell` WHERE `Player` = ?;")) {
            
            preparedStatement.setString(1, proxiedPlayer.getName());
            rs = preparedStatement.executeQuery();
            rs.next();
            return new PlayerModel(proxiedPlayer, TellType.valueOf(rs.getString("Notification")));
        } finally {
            if(rs != null) rs.close();
        }
    }

    public void updateNotification(ProxiedPlayer proxiedPlayer, TellType typeTell) throws SQLException {
        try(Connection connection = hikariConnect.hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `NexusTell` SET `Notification` = ? WHERE `Player` = ?;")) {

            preparedStatement.setString(1, typeTell.getTellType());
            preparedStatement.setString(2, proxiedPlayer.getName());
            preparedStatement.executeUpdate();
        }
    }

}
