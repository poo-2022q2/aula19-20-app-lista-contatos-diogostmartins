package model;

public class Contato implements IContato {
    private final long id;
    private final String nome;
    private final String email;
    private final String endereco;
    private final String telefone;

    public Contato(long id, String nome, String email, 
        String endereco, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n"
        + "nome: " + nome + "\n"
        + "email: " + email + "\n"
        + "endereco: " + endereco + "\n"
        + "telefone: " + telefone + "\n";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contato other = (Contato) obj;
        if (id != other.id)
            return false;
        return true;
    }

    
}
