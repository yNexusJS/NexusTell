package br.com.nexus.plugin.Enum;

public enum TellType {

    habilitado("habilitado"), desabilitado("desabilitado"), amigos("amigos");

    private String descricao;

    TellType(String descricao) {
        this.descricao = descricao;
    }

    public String getTellType() {
        return descricao;
    }

}
