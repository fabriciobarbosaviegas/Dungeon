package Components;

import Utils.SoundPlayer;
import javax.swing.*;
import java.awt.*;

public class EstilizacaoBotoes {
    private SoundPlayer musicPlayer;
    public static void estilizarBotao(JButton botao) {
        this.musicPlayer = new SoundPlayer();
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);  // Define o texto como branco
        botao.setBackground(new Color(139, 69, 19));  // Cor de fundo padrão
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11), 3));

        // Adiciona um padding interno
        botao.setMargin(new Insets(10, 20, 10, 20));  // Ajuste conforme necessário

        // Adiciona efeito de hover
        Color corFundoOriginal = botao.getBackground();
        Color corHover = new Color(184, 134, 11);  // Cor de fundo ao passar o mouse
        Color corBordaOriginal = new Color(184, 134, 11);
        Color corBordaHover = new Color(139, 69, 19);  // Cor da borda ao passar o mouse

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corHover);
                botao.setBorder(BorderFactory.createLineBorder(corBordaHover, 3));
                musicPlayer.playSound("/resources/sound/hover-button-sound.wav", false);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundoOriginal);
                botao.setBorder(BorderFactory.createLineBorder(corBordaOriginal, 3));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                musicPlayer.playSound("/resources/sound/click-button-sound.wav", false);
            }
        });
    }
}
