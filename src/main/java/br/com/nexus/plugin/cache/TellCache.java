package br.com.nexus.plugin.cache;

import br.com.nexus.plugin.model.PlayerModel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class TellCache {

    public static HashMap<ProxiedPlayer, PlayerModel> playerPlayerModelHashMap = new HashMap<>();
    public static HashMap<ProxiedPlayer, ProxiedPlayer> repostPlayerModelHashMap = new HashMap<>();

}
