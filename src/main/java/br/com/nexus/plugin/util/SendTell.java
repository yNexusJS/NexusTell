package br.com.nexus.plugin.util;

import br.com.nexus.plugin.API.NexusAmigosAPI;
import br.com.nexus.plugin.Enum.TellType;
import br.com.nexus.plugin.api.NexusCoreAPI;
import br.com.nexus.plugin.cache.TellCache;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class SendTell {

    private final TextComponentUtil textComponentUtil;

    public void sendTell(ProxiedPlayer proxiedPlayer1, ProxiedPlayer proxiedPlayer2, String msg) {
        NexusCoreAPI nexusCoreAPI = new NexusCoreAPI();
        if(nexusCoreAPI.isIgnoreServer(proxiedPlayer2.getServer().getInfo().getName())) {
            proxiedPlayer1.sendMessage(textComponentUtil.createTextComponent("§cO jogador está se autenticando."));
            return;
        }
        if(TellCache.playerPlayerModelHashMap.get(proxiedPlayer2).getToggleNotification().equals(TellType.desabilitado)) {
            proxiedPlayer1.sendMessage(textComponentUtil.createTextComponent("§cO jogador está com o tell desativado."));
            return;
        }
        if(TellCache.playerPlayerModelHashMap.get(proxiedPlayer2).getToggleNotification().equals(TellType.amigos)) {
            if(!new NexusAmigosAPI().isFriend(proxiedPlayer1, proxiedPlayer2)) {
                proxiedPlayer1.sendMessage(textComponentUtil.createTextComponent("§cO jogador está com o tell apenas para amigos."));
                return;
            }
        }

        TellCache.repostPlayerModelHashMap.put(proxiedPlayer1, proxiedPlayer2);
        TellCache.repostPlayerModelHashMap.put(proxiedPlayer2, proxiedPlayer1);

        TextComponent msg1 = new TextComponent(TextComponent.fromLegacyText("§8Mensagem para " + nexusCoreAPI.getTagUtil(proxiedPlayer2).getTag().replaceAll("&", "§")+" " +
                "" +proxiedPlayer2.getName() + "§8: §6" + msg));
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§8Clique para responder")));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + proxiedPlayer2.getName()+" "));


        TextComponent msg2 = new TextComponent(TextComponent.fromLegacyText("§8Mensagem de " + nexusCoreAPI.getTagUtil(proxiedPlayer1).getTag().replaceAll("&", "§")+" " +
                "" +proxiedPlayer1.getName() + "§8: §6" + msg));
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§8Clique para responder")));
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + proxiedPlayer1.getName()+" "));

        proxiedPlayer1.sendMessage(msg1);
        proxiedPlayer2.sendMessage(msg2);
    }

}
