package br.com.nexus.plugin.listener;

import br.com.nexus.plugin.cache.TellCache;
import br.com.nexus.plugin.storage.database.DatabaseMethod;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final DatabaseMethod databaseMethod;

    @EventHandler @SneakyThrows
    public void toJoin(PostLoginEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        if(!databaseMethod.hasProxiedPlayer(proxiedPlayer)) databaseMethod.setProxiedPlayer(proxiedPlayer);
        TellCache.playerPlayerModelHashMap.put(proxiedPlayer, databaseMethod.getPlayerModelByProxiedPlayer(proxiedPlayer));
    }

    @EventHandler
    public void toCloseConnect(PlayerDisconnectEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        TellCache.playerPlayerModelHashMap.remove(proxiedPlayer);
    }

}
