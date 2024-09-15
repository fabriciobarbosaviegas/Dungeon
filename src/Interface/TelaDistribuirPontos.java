package Interface;

import Personagem.Heroi;
import Components.BarraDistribuicao;
import Utils.SoundPlayer;
import Jogo.Jogo;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import Components.EstilizacaoBotoes;

public class TelaDistribuirPontos extends JFrame {
    private final Heroi player;
    private int pontosRestantes;
    private final JLabel lblPontosRestantes;
    private final BarraDistribuicao barraAtaque, barraDefesa, barraSaude;

    public TelaDistribuirPontos(Jogo jogo) {
        this.player = jogo.getPlayer();
        this.pontosRestantes = 10;

        setTitle("Distribuição de Pontos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com fundo customizado
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/images/bg-heroesSelection.jpg")));
                Image background = backgroundIcon.getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.BOTH; // Preencher o espaço disponível

        // Adicionando a imagem no topo e redimensionando
        ImageIcon imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/images/image-title.png"))); // Substitua "top-image.png" pelo nome da sua imagem
        Image img = imgIcon.getImage();
        Image imgScaled = img.getScaledInstance(500,250, Image.SCALE_SMOOTH); // Redimensione para 400x200 pixels, ajuste conforme necessário
        ImageIcon imgIconScaled = new ImageIcon(imgScaled);
        JLabel lblImagemTopo = new JLabel(imgIconScaled);
        lblImagemTopo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; // Ocupa 10% do espaço vertical
        mainPanel.add(lblImagemTopo, gbc);


        // Título principal
        JLabel lblTitulo = new JLabel("Distribua seus Pontos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        mainPanel.add(lblTitulo, gbc);

        // Label de pontos restantes
        lblPontosRestantes = new JLabel("Pontos Restantes: " + pontosRestantes);
        lblPontosRestantes.setFont(new Font("Arial", Font.BOLD, 24));
        lblPontosRestantes.setForeground(Color.WHITE);
        lblPontosRestantes.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        mainPanel.add(lblPontosRestantes, gbc);

        // Configuração das barras e botões
        barraAtaque = new BarraDistribuicao(0, 30);
        barraAtaque.setValorAtual(player.getAtaque());
        JButton btnAumentarAtaque = new JButton("+");
        estilizarBotao(btnAumentarAtaque);
        JButton btnDiminuirAtaque = new JButton("-");
        estilizarBotao(btnDiminuirAtaque);

        barraDefesa = new BarraDistribuicao(0, 30);
        barraDefesa.setValorAtual(player.getDefensa());
        JButton btnAumentarDefesa = new JButton("+");
        estilizarBotao(btnAumentarDefesa);
        JButton btnDiminuirDefesa = new JButton("-");
        estilizarBotao(btnDiminuirDefesa);

        barraSaude = new BarraDistribuicao(0, 30);
        barraSaude.setValorAtual(player.getSaude());
        JButton btnAumentarSaude = new JButton("+");
        estilizarBotao(btnAumentarSaude);
        JButton btnDiminuirSaude = new JButton("-");
        estilizarBotao(btnDiminuirSaude);

        JButton btnConfirmar = new JButton("Confirmar");
        estilizarBotao(btnConfirmar);

        // Configuração para o Ataque
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.2;
        mainPanel.add(btnDiminuirAtaque, gbc);

        gbc.gridx = 1;
        mainPanel.add(barraAtaque, gbc);

        gbc.gridx = 2;
        mainPanel.add(btnAumentarAtaque, gbc);

        // Configuração para a Defesa
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnDiminuirDefesa, gbc);

        gbc.gridx = 1;
        mainPanel.add(barraDefesa, gbc);

        gbc.gridx = 2;
        mainPanel.add(btnAumentarDefesa, gbc);

        // Configuração para a Saúde
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(btnDiminuirSaude, gbc);

        gbc.gridx = 1;
        mainPanel.add(barraSaude, gbc);

        gbc.gridx = 2;
        mainPanel.add(btnAumentarSaude, gbc);

        // Botão Confirmar
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 0.2;
        mainPanel.add(btnConfirmar, gbc);

        // Configuração dos listeners para botões
        btnAumentarAtaque.addActionListener(e -> {
            if (pontosRestantes > 0 && player.getAtaque() < 30) {
                player.setAtaque(player.getAtaque() + 1);
                pontosRestantes--;
                atualizarLabels();
            }
        });

        btnDiminuirAtaque.addActionListener(e -> {
            if (player.getAtaque() > 0) {
                player.setAtaque(player.getAtaque() - 1);
                pontosRestantes++;
                atualizarLabels();
            }
        });

        btnAumentarDefesa.addActionListener(e -> {
            if (pontosRestantes > 0 && player.getDefensa() < 30) {
                player.setDefensa(player.getDefensa() + 1);
                pontosRestantes--;
                atualizarLabels();
            }
        });

        btnDiminuirDefesa.addActionListener(e -> {
            if (player.getDefensa() > 0) {
                player.setDefensa(player.getDefensa() - 1);
                pontosRestantes++;
                atualizarLabels();
            }
        });

        btnAumentarSaude.addActionListener(e -> {
            if (pontosRestantes > 0 && player.getSaude() < 30) {
                player.setSaude(player.getSaude() + 1);
                player.setHp(player.getMaxHp());
                pontosRestantes--;
                atualizarLabels();
            }
        });

        btnDiminuirSaude.addActionListener(e -> {
            if (player.getSaude() > 0) {
                player.setSaude(player.getSaude() - 1);
                player.setHp(player.getMaxHp());
                pontosRestantes++;
                atualizarLabels();
            }
        });

        btnConfirmar.addActionListener(e -> {
            this.player.status();
            jogo.iniciar();
            new TelaJogo(jogo);

            // Implementar lógica para confirmar a distribuição de pontos
            // Exemplo: Salvar herói e iniciar o jogo
            dispose(); // Fecha a janela
        });

        setVisible(true);
    }

    private void atualizarLabels() {
        lblPontosRestantes.setText("Pontos Restantes: " + pontosRestantes);
        barraAtaque.setValorAtual(player.getAtaque());
        barraDefesa.setValorAtual(player.getDefensa());
        barraSaude.setValorAtual(player.getSaude());
    }
    private void estilizarBotao(JButton botao) {
        EstilizacaoBotoes estilizador = new EstilizacaoBotoes();
        estilizador.estilizarBotao(botao);
    }
}
