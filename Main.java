
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {

    static Scanner entrada = new Scanner(System.in);

    static ArrayList<Usuario> usuarios = new ArrayList<>();
    static ArrayList<Evento> eventos = new ArrayList<>();
    static ArrayList<Inscricao> inscricoes = new ArrayList<>();

    static int idUsuario = 1;
    static int idEvento = 1;
    static int idInscricao = 1;

    static final String ARQUIVO = "events.data";

    static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {

        carregarDados();

        int opcao;

        do {
            System.out.println("\n================================");
            System.out.println("       SISTEMA DE EVENTOS");
            System.out.println("          PORTO ALEGRE");
            System.out.println("================================");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Cadastrar evento");
            System.out.println("3 - Consultar eventos");
            System.out.println("4 - Fazer inscrição");
            System.out.println("5 - Minhas inscrições");
            System.out.println("6 - Cancelar inscrição");
            System.out.println("0 - Sair");
            System.out.println("================================");

            opcao = lerNumero("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;

                case 2:
                    cadastrarEvento();
                    break;

                case 3:
                    consultarEventos();
                    break;

                case 4:
                    fazerInscricao();
                    break;

                case 5:
                    minhasInscricoes();
                    break;

                case 6:
                    cancelarInscricao();
                    break;

                case 0:
                    salvarDados();
                    System.out.println("Dados salvos. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    static void cadastrarUsuario() {

        System.out.println("\n--- CADASTRO DE USUÁRIO ---");

        String nome = lerTexto("Nome: ");
        String email = lerTexto("Email: ");
        String telefone = lerTexto("Telefone: ");

        Usuario usuario = new Usuario(
                idUsuario++, nome, email, telefone
        );

        usuarios.add(usuario);

        salvarDados();

        System.out.println("Usuário cadastrado!");
        System.out.println("Seu código é: " + usuario.getId());
    }

    static void cadastrarEvento() {

        System.out.println("\n--- CADASTRO DE EVENTO ---");

        String nome = lerTexto("Nome do evento: ");
        String endereco = lerTexto("Endereço ou link: ");

        System.out.println("\nCategorias:");
        System.out.println("1 - Tecnologia");
        System.out.println("2 - Educação");
        System.out.println("3 - Música");
        System.out.println("4 - Esporte");
        System.out.println("5 - Cultura");

        int opcaoCategoria = lerNumero("Escolha: ");

        String categoria;

        switch (opcaoCategoria) {
            case 1:
                categoria = "Tecnologia";
                break;
            case 2:
                categoria = "Educação";
                break;
            case 3:
                categoria = "Música";
                break;
            case 4:
                categoria = "Esporte";
                break;
            case 5:
                categoria = "Cultura";
                break;
            default:
                System.out.println("Categoria inválida!");
                return;
        }

        LocalDateTime data;

        try {
            System.out.println("Exemplo: 25/12/2026 19:30");
            String dataTexto = lerTexto("Data e horário: ");

            data = LocalDateTime.parse(dataTexto, FORMATO);

            if (!data.isAfter(LocalDateTime.now())) {
                System.out.println("A data precisa ser futura!");
                return;
            }

        } catch (Exception e) {
            System.out.println("Data inválida!");
            return;
        }

        String descricao = lerTexto("Descrição: ");

        System.out.println("\nTipo do evento:");
        System.out.println("1 - Presencial");
        System.out.println("2 - Online");

        int tipo = lerNumero("Escolha: ");

        Evento evento;

        if (tipo == 1) {

            evento = new EventoPresencial(
                    idEvento++, nome, endereco,
                    categoria, data, descricao
            );

        } else if (tipo == 2) {

            evento = new EventoOnline(
                    idEvento++, nome, endereco,
                    categoria, data, descricao
            );

        } else {
            System.out.println("Tipo inválido!");
            return;
        }

        eventos.add(evento);

        salvarDados();

        System.out.println("Evento cadastrado com sucesso!");
        System.out.println("Código do evento: " + evento.getId());
    }

    static void consultarEventos() {

        System.out.println("\n--- EVENTOS DISPONÍVEIS ---");

        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }

        eventos.sort(
                Comparator.comparing(Evento::getData)
        );

        for (Evento evento : eventos) {
            evento.mostrarEvento();
            System.out.println("------------------------------");
        }
    }

    static void fazerInscricao() {

        System.out.println("\n--- INSCRIÇÃO EM EVENTO ---");

        if (usuarios.isEmpty() || eventos.isEmpty()) {
            System.out.println("Cadastre usuários e eventos primeiro.");
            return;
        }

        int usuarioEscolhido = lerNumero("Código do usuário: ");
        int eventoEscolhido = lerNumero("Código do evento: ");

        Usuario usuario = buscarUsuario(usuarioEscolhido);
        Evento evento = buscarEvento(eventoEscolhido);

        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        if (evento == null) {
            System.out.println("Evento não encontrado!");
            return;
        }

        if (!evento.getData().isAfter(LocalDateTime.now())) {
            System.out.println("Este evento já começou ou aconteceu.");
            return;
        }

        for (Inscricao inscricao : inscricoes) {

            if (inscricao.getUsuario().getId() == usuarioEscolhido
                    && inscricao.getEvento().getId() == eventoEscolhido
                    && inscricao.isConfirmada()) {

                System.out.println("Você já está inscrito!");
                return;
            }
        }

        Inscricao inscricao = new Inscricao(
                idInscricao++, usuario, evento
        );

        inscricoes.add(inscricao);

        salvarDados();

        System.out.println("Inscrição realizada com sucesso!");
    }

    static void minhasInscricoes() {

        System.out.println("\n--- MINHAS INSCRIÇÕES ---");

        int codigo = lerNumero("Código do usuário: ");

        Usuario usuario = buscarUsuario(codigo);

        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        boolean encontrou = false;

        for (Inscricao inscricao : inscricoes) {

            if (inscricao.getUsuario().getId() == codigo
                    && inscricao.isConfirmada()) {

                System.out.println("\nEvento: "
                        + inscricao.getEvento().getNome());

                System.out.println("Data: "
                        + inscricao.getEvento().getData().format(FORMATO));

                System.out.println("Status: "
                        + inscricao.getEvento().getStatus());

                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Você não possui inscrições confirmadas.");
        }
    }

    static void cancelarInscricao() {

        System.out.println("\n--- CANCELAR INSCRIÇÃO ---");

        int codigoUsuario = lerNumero("Código do usuário: ");
        int codigoEvento = lerNumero("Código do evento: ");

        for (Inscricao inscricao : inscricoes) {

            if (inscricao.getUsuario().getId() == codigoUsuario
                    && inscricao.getEvento().getId() == codigoEvento
                    && inscricao.isConfirmada()) {

                inscricao.cancelar();

                salvarDados();

                System.out.println("Inscrição cancelada!");
                return;
            }
        }

        System.out.println("Inscrição não encontrada.");
    }

    static Usuario buscarUsuario(int codigo) {

        for (Usuario usuario : usuarios) {

            if (usuario.getId() == codigo) {
                return usuario;
            }
        }

        return null;
    }

    static Evento buscarEvento(int codigo) {

        for (Evento evento : eventos) {

            if (evento.getId() == codigo) {
                return evento;
            }
        }

        return null;
    }

    static String codificar(String texto) {

        return Base64.getEncoder().encodeToString(
                texto.getBytes(StandardCharsets.UTF_8)
        );
    }

    static String decodificar(String texto) {

        return new String(
                Base64.getDecoder().decode(texto),
                StandardCharsets.UTF_8
        );
    }

    static void salvarDados() {

        try (PrintWriter arquivo = new PrintWriter(
                new FileWriter(ARQUIVO))) {

            for (Usuario usuario : usuarios) {

                arquivo.println(
                        "U|" + usuario.getId() + "|"
                        + codificar(usuario.getNome()) + "|"
                        + codificar(usuario.getEmail()) + "|"
                        + codificar(usuario.getTelefone())
                );
            }

            for (Evento evento : eventos) {

                arquivo.println(
                        "E|" + evento.getId() + "|"
                        + evento.getTipo() + "|"
                        + codificar(evento.getNome()) + "|"
                        + codificar(evento.endereco) + "|"
                        + codificar(evento.categoria) + "|"
                        + evento.getData() + "|"
                        + codificar(evento.descricao)
                );
            }

            for (Inscricao inscricao : inscricoes) {

                arquivo.println(
                        "I|" + inscricao.getId() + "|"
                        + inscricao.getUsuario().getId() + "|"
                        + inscricao.getEvento().getId() + "|"
                        + inscricao.isConfirmada()
                );
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados.");
        }
    }

    static void carregarDados() {

        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor = new BufferedReader(
                new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                String[] dados = linha.split("\\|", -1);

                if (dados[0].equals("U")) {

                    Usuario usuario = new Usuario(
                            Integer.parseInt(dados[1]),
                            decodificar(dados[2]),
                            decodificar(dados[3]),
                            decodificar(dados[4])
                    );

                    usuarios.add(usuario);

                    idUsuario = Math.max(
                            idUsuario, usuario.getId() + 1
                    );
                }

                else if (dados[0].equals("E")) {

                    int codigo = Integer.parseInt(dados[1]);

                    String tipo = dados[2];
                    String nome = decodificar(dados[3]);
                    String endereco = decodificar(dados[4]);
                    String categoria = decodificar(dados[5]);

                    LocalDateTime data =
                            LocalDateTime.parse(dados[6]);

                    String descricao = decodificar(dados[7]);

                    Evento evento;

                    if (tipo.equals("Presencial")) {

                        evento = new EventoPresencial(
                                codigo, nome, endereco,
                                categoria, data, descricao
                        );

                    } else {

                        evento = new EventoOnline(
                                codigo, nome, endereco,
                                categoria, data, descricao
                        );
                    }

                    eventos.add(evento);

                    idEvento = Math.max(idEvento, codigo + 1);
                }

                else if (dados[0].equals("I")) {

                    int codigo = Integer.parseInt(dados[1]);

                    Usuario usuario = buscarUsuario(
                            Integer.parseInt(dados[2])
                    );

                    Evento evento = buscarEvento(
                            Integer.parseInt(dados[3])
                    );

                    boolean confirmada =
                            Boolean.parseBoolean(dados[4]);

                    if (usuario != null && evento != null) {

                        Inscricao inscricao = new Inscricao(
                                codigo, usuario, evento, confirmada
                        );

                        inscricoes.add(inscricao);

                        idInscricao = Math.max(
                                idInscricao, codigo + 1
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar os dados.");
        }
    }

    static String lerTexto(String mensagem) {

        System.out.print(mensagem);
        return entrada.nextLine();
    }

    static int lerNumero(String mensagem) {

        while (true) {

            try {
                System.out.print(mensagem);
                return Integer.parseInt(entrada.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            }
        }
    }
}
