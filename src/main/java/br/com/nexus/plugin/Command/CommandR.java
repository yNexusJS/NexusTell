package br.com.nexus.plugin.Command;

import br.com.nexus.plugin.api.NexusCoreAPI;
import br.com.nexus.plugin.cache.TellCache;
import br.com.nexus.plugin.util.SendTell;
import br.com.nexus.plugin.util.TextComponentUtil;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandR extends Command {

    public CommandR(TextComponentUtil textComponentUtil, SendTell sendTell) {
        super("r");
        this.textComponentUtil = textComponentUtil;
        this.sendTell = sendTell;
    }

    private final TextComponentUtil textComponentUtil;
    private final SendTell sendTell;

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(textComponentUtil.createTextComponent("§aApenas jogadores podem mandar mensagem."));
        }
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
        if(args.length == 0) {
            proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cUse o comando da seguinte forma: /r <mensagem>."));
            return;
        }
        if(new NexusCoreAPI().isIgnoreServer(proxiedPlayer.getServer().getInfo().getName())) {
            proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cVocê precisa se autenticar para executar esse comando."));
            return;
        }
        StringBuilder str = new StringBuilder();
        for (String arg : args) str.append(arg).append(" ");
        String msg = str.toString();

        ProxiedPlayer proxiedPlayer2 = TellCache.repostPlayerModelHashMap.get(proxiedPlayer);

        if(proxiedPlayer2 != null) {
            if(proxiedPlayer2.isConnected()) sendTell.sendTell(proxiedPlayer, proxiedPlayer2, msg);
            else proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cInfelizmente o jogador não se encontra mais online."));
        } else proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cVocê não tem ninguém para responder."));
    }
}
