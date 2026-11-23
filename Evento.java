
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Evento {

    protected int id;
    protected String nome;
    protected String endereco;
    protected String categoria;
    protected LocalDateTime data;
    protected String descricao;

    public Evento(int id, String nome, String endereco,
                  String categoria, LocalDateTime data,
                  String descricao) {

        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.data = data;
        this.descricao = descricao;
    }

    public abstract String getTipo();

    public abstract void mostrarTipo();

    public String getStatus() {

        if (data.toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
            return "Já aconteceu";
        }

        if (data.toLocalDate().isEqual(LocalDateTime.now().toLocalDate())) {
            return "Acontece hoje";
        }

        return "Evento futuro";
    }

    public void mostrarEvento() {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\nCódigo: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Endereço: " + endereco);
        System.out.println("Categoria: " + categoria);
        System.out.println("Data: " + data.format(formato));
        System.out.println("Descrição: " + descricao);
        System.out.println("Tipo: " + getTipo());
        System.out.println("Status: " + getStatus());
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getData() {
        return data;
    }
}
