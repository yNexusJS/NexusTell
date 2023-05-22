package br.com.nexus.plugin.util;

import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtil {

    public TextComponent createTextComponent(String text) {
        return new TextComponent(TextComponent.fromLegacyText(text.replaceAll("&", "§")));
    }

}
