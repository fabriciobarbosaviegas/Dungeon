package Interface;

import Components.EstilizacaoBotoes;
import Jogo.Jogo;
import Utils.ResourceLoader;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TelaGameOver extends JDialog {

    public TelaGameOver(JFrame parent, boolean venceu, Jogo jogo) {
        super(parent, "Game Over!", true); // Torna o diálogo modal

        // Configurações da janela
        setTitle("Game Over!");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizar a janela na tela

        // Carregar a imagem de fundo
        Image imagemFundo = null;
        try {
            imagemFundo = ResourceLoader.loadImage("/resources/images/bg-image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Painel com imagem de fundo
        Image finalImagemFundo = imagemFundo;
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalImagemFundo != null) {
                    g.drawImage(finalImagemFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        painelPrincipal.setLayout(new BorderLayout());
        add(painelPrincipal);

        // Mensagem de vitória ou derrota
        String mensagem = venceu ? "Você venceu!" : "Você perdeu!";
        JLabel labelMensagem = new JLabel(mensagem, SwingConstants.CENTER);
        labelMensagem.setFont(new Font("Arial", Font.BOLD, 24));
        labelMensagem.setForeground(Color.WHITE); // Deixe o texto branco para contrastar com o fundo
        painelPrincipal.add(labelMensagem, BorderLayout.CENTER);

        // Painel para os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false); // Deixe o painel de botões transparente
        painelBotoes.setLayout(new FlowLayout());

        // Botão Reiniciar Jogo
        JButton btnReiniciar = new JButton("Reiniciar Jogo");
        btnReiniciar.addActionListener(e -> {
            jogo.reiniciar();
            dispose(); // Fecha a tela de Game Over
        });
        estilizarBotao(btnReiniciar);
        painelBotoes.add(btnReiniciar);

        // Botão Novo Jogo
        JButton btnNovoJogo = new JButton("Novo Jogo");
        btnNovoJogo.addActionListener(e -> {
            jogo.getBgMusic().stop();
            new Jogo();
            dispose(); // Fecha a tela de Game Over
        });
        estilizarBotao(btnNovoJogo);
        painelBotoes.add(btnNovoJogo);

        // Adiciona o painel de botões ao painel principal
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void estilizarBotao(JButton botao) {
        EstilizacaoBotoes estilizador = new EstilizacaoBotoes();
        estilizador.estilizarBotao(botao);
    }
}
