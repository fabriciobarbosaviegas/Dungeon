package Interface;

import Jogo.Jogo;
import Utils.SoundPlayer;
import Personagem.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class TelaSelecaoHeroi extends JFrame {
    private final Jogo jogo;
    private final String nome;
    private final SoundPlayer musicPlayer = new SoundPlayer();

    public TelaSelecaoHeroi(Jogo jogo, String nome) {
        this.jogo = jogo;
        this.nome = nome;
        setTitle("Seleção de Herói");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);  // Permite redimensionamento

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK); // Cor de fundo preta

        JLabel titleLabel = new JLabel("Quem é " + this.nome + "?", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); // 20px de margem em cima e embaixo
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel principal com fundo e GIF do personagem
        JPanel characterPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        characterPanel.setOpaque(false);

        // Adiciona botões com o fundo e o GIF sobreposto
        characterPanel.add(createHeroButton("Barbaro", "/resources/images/barbaro-select.gif", "/resources/sound/barbaro-selected.wav"));
        characterPanel.add(createHeroButton("Guerreiro", "/resources/images/guerreio-select.gif", "/resources/sound/guerreiro-selected.wav"));
        characterPanel.add(createHeroButton("Paladino", "/resources/images/paladino-select.gif", "/resources/sound/paladino-selected.wav"));

        mainPanel.add(characterPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Adiciona um ComponentListener para ajustar os componentes ao redimensionamento
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = getSize();
                for (Component comp : characterPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        JLayeredPane layeredPane = (JLayeredPane) ((JButton) comp).getComponent(0);
                        Dimension buttonSize = new Dimension(newSize.width / 3, newSize.height - 100); // Exemplo de ajuste
                        layeredPane.setPreferredSize(buttonSize);
                        layeredPane.revalidate();
                        layeredPane.repaint();
                    }
                }
            }
        });
    }

    private JButton createHeroButton(String heroType, String gifPath, String soundPath) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());

        // Criação do JLayeredPane para sobrepor as camadas
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(200, 300)); // Tamanho inicial

        // Imagem de fundo
        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/images/bg-heroesSelection.jpg")));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 600, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // GIF do sprite
        ImageIcon gifIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(gifPath)));
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(50, 50, 150, 300); // Ajuste essas dimensões para posicionar o GIF corretamente
        layeredPane.add(gifLabel, JLayeredPane.PALETTE_LAYER);

        // Adiciona o JLayeredPane ao botão
        button.add(layeredPane, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(heroType, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.YELLOW);
        button.add(nameLabel, BorderLayout.SOUTH);

        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));

        button.addActionListener(e -> {
            this.musicPlayer.playSound(soundPath, false); // Toca o som específico ao clicar no botão
            handleHeroSelection(heroType);
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                musicPlayer.playSound("/resources/sound/hover-button-sound.wav", false);
                button.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
            }
        });

        return button;
    }

    private void handleHeroSelection(String heroType) {
        switch (heroType) {
            case "Barbaro":
                jogo.setPlayer(new Barbaro(this.nome));
                break;
            case "Guerreiro":
                jogo.setPlayer(new Guerreiro(this.nome));
                break;
            case "Paladino":
                jogo.setPlayer(new Paladino(this.nome));
                break;
        }
        dispose();
        new TelaDistribuirPontos(this.jogo);
    }
}
