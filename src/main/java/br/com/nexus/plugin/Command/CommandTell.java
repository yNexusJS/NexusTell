package br.com.nexus.plugin.Command;

import br.com.nexus.plugin.Enum.TellType;
import br.com.nexus.plugin.api.NexusCoreAPI;
import br.com.nexus.plugin.cache.TellCache;
import br.com.nexus.plugin.model.PlayerModel;
import br.com.nexus.plugin.storage.database.DatabaseMethod;
import br.com.nexus.plugin.util.SendTell;
import br.com.nexus.plugin.util.TextComponentUtil;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandTell extends Command {

    public CommandTell(TextComponentUtil textComponentUtil, SendTell sendTell, DatabaseMethod databaseMethod) {
        super("tell");
        this.textComponentUtil = textComponentUtil;
        this.sendTell = sendTell;
        this.databaseMethod = databaseMethod;
    }

    private final TextComponentUtil textComponentUtil;
    private final SendTell sendTell;
    private final DatabaseMethod databaseMethod;

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(textComponentUtil.createTextComponent("§aApenas jogadores podem mandar mensagem."));
        }
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
        if(args.length == 0) {
            sendDefaultMessage(proxiedPlayer);
            return;
        }
        if(new NexusCoreAPI().isIgnoreServer(proxiedPlayer.getServer().getInfo().getName())) {
            proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cVocê precisa se autenticar para executar esse comando."));
            return;
        }
        if(args.length == 1) {
            switch (args[0].toUpperCase()) {
                case("HABILITAR"):
                    sendToggleTell(proxiedPlayer, TellType.habilitado);
                    return;
                case("DESABILITAR"):
                    sendToggleTell(proxiedPlayer, TellType.desabilitado);
                    return;
                case("AMIGOS"):
                    sendToggleTell(proxiedPlayer, TellType.amigos);
                    return;
                default:
                    proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cUse o comando da seguinte forma: /tell <nick> <mensagem>."));
                    return;
            }
        }
        if(proxiedPlayer.getName().equalsIgnoreCase(args[0])) {
            proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cVocê não pode mandar mensagem para si mesmo."));
            return;
        }

        StringBuilder str = new StringBuilder();
        for (int i = 1; i < args.length; i++) str.append(args[i]).append(" ");
        String msg = str.toString();

        ProxiedPlayer proxiedPlayer2 = BungeeCord.getInstance().getPlayer(args[0]);

        if(proxiedPlayer2 != null) sendTell.sendTell(proxiedPlayer, proxiedPlayer2, msg);
        else proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cO jogador §7"+args[0]+"§c não foi encontrado."));
    }

    @SneakyThrows
    public void sendToggleTell(ProxiedPlayer proxiedPlayer, TellType changed) {
        PlayerModel playerModel = TellCache.playerPlayerModelHashMap.get(proxiedPlayer);
        if(playerModel.getToggleNotification().equals(changed)) {
            proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§cEssa configuração já foi definida."));
            return;
        }
        playerModel.setToggleNotification(changed);
        databaseMethod.updateNotification(proxiedPlayer, changed);
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§aO status do seu tell agora é: §7"+changed.getTellType()+"§a."));
        return;
    }

    public void sendDefaultMessage(ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent(""));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§aComandos tell:"));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent(""));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§a/tell <nick> <mensagem> §f§l- §7Enviar mensagem particular ao jogador."));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§a/tell desabilitar §f§l- §7Desabilitar todas as mensagem no seu tell."));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§a/tell habilitar §f§l- §7Habilitar todas as mensagem no seu tell."));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent("§a/tell amigos §f§l- §7Apenas seus amigos poderam mandar mensagem."));
        proxiedPlayer.sendMessage(textComponentUtil.createTextComponent(""));
    }
}
