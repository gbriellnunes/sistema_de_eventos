
import java.time.LocalDateTime;

public class EventoPresencial extends Evento {

    public EventoPresencial(int id, String nome, String endereco,
                            String categoria, LocalDateTime data,
                            String descricao) {

        super(id, nome, endereco, categoria, data, descricao);
    }

    @Override
    public String getTipo() {
        return "Presencial";
    }

    @Override
    public void mostrarTipo() {
        System.out.println("Este evento será presencial.");
    }
}
