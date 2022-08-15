package controller;

import model.Contato;
import model.IContato;

public class ContatoListItem implements IContato {
    private final Contato contato;

    public ContatoListItem(Contato contato) {
        this.contato = contato;
    }

    public Contato getContato() {
        return contato;
    }

    @Override
    public long getId() {
        return contato.getId();
    }

    @Override
    public String getNome() {
        return contato.getNome();
    }

    @Override
    public String getEmail() {
        return contato.getEmail();
    }

    @Override
    public String getEndereco() {
        return contato.getEndereco();
    }

    @Override
    public String getTelefone() {
        return contato.getTelefone();
    }

    @Override
    public String toString() {
        return getNome();
    }
}
