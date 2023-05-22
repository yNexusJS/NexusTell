package br.com.nexus.plugin.model;

import br.com.nexus.plugin.Enum.TellType;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Getter @Setter
public class PlayerModel {

    private ProxiedPlayer proxiedPlayer;
    private TellType toggleNotification;

    public PlayerModel(ProxiedPlayer proxiedPlayer, TellType toggleNotification) {
        this.proxiedPlayer = proxiedPlayer;
        this.toggleNotification = toggleNotification;
    }
}
