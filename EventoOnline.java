
import java.time.LocalDateTime;

public class EventoOnline extends Evento {

    public EventoOnline(int id, String nome, String endereco,
                        String categoria, LocalDateTime data,
                        String descricao) {

        super(id, nome, endereco, categoria, data, descricao);
    }

    @Override
    public String getTipo() {
        return "Online";
    }

    @Override
    public void mostrarTipo() {
        System.out.println("Este evento será online.");
    }
}
