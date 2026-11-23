
public class Inscricao {

    private int id;
    private Usuario usuario;
    private Evento evento;
    private boolean confirmada;

    public Inscricao(int id, Usuario usuario, Evento evento) {
        this(id, usuario, evento, true);
    }

    public Inscricao(int id, Usuario usuario, Evento evento,
                     boolean confirmada) {

        this.id = id;
        this.usuario = usuario;
        this.evento = evento;
        this.confirmada = confirmada;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void cancelar() {
        confirmada = false;
    }
}
