package Interface;

import Jogo.Jogo;
import Utils.SoundPlayer;
import Utils.ResourceLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

public class TelaInicial extends JFrame {
    final private Jogo jogo;
    private final SoundPlayer musicPlayer = new SoundPlayer();

    public TelaInicial(Jogo jogo) {
        this.jogo = jogo;
        setTitle("Tela Inicial");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        initializeComponents();

        // Atualiza o layout ao redimensionar a janela
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    // Método para tocar o efeito sonoro
    private void playButtonSound() {
        try {
            musicPlayer.playSound("/resources/sound/click-button-sound.wav", false);
        } catch (Exception e) {
            System.err.println("Erro ao tocar o efeito sonoro: " + e.getMessage());
        }
    }

    private void playHoverButtonSound() {
        try {
            musicPlayer.playSound("/resources/sound/hover-button-sound.wav", false);
        } catch (Exception e) {
            System.err.println("Erro ao tocar o efeito sonoro: " + e.getMessage());
        }
    }

    private void initializeComponents() {
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        playBackgroundMusic();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel();
        try {
            URL titleImageUrl = getClass().getResource("/resources/images/image-title.png");
            if (titleImageUrl != null) {
                ImageIcon titleImageIcon = new ImageIcon(titleImageUrl);
                Image image = titleImageIcon.getImage();
                Image scaledImage = image.getScaledInstance(-1, 300, Image.SCALE_SMOOTH); // Ajuste a altura desejada
                titleLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                System.err.println("URL da imagem do título é nula. Verifique o caminho.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza horizontalmente
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.24;

        JButton jogarButton = createStyledButton("Jogar");
        JButton debugButton = createStyledButton("DEBUG");
        JButton fecharButton = createStyledButton("Fechar");

        gbc.gridy = 0;
        panel.add(jogarButton, gbc);

        gbc.gridy = 1;
        panel.add(debugButton, gbc);

        gbc.gridy = 2;
        panel.add(fecharButton, gbc);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 180)); // Cor de fundo com transparência
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playHoverButtonSound();
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0, 180));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playButtonSound(); // Toca o efeito sonoro
                button.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }
        });

        button.addActionListener(e -> handleButtonClick(text));

        return button;
    }

    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "Jogar":
                mostrarTelaEntradaNome();
                break;
            case "DEBUG":
                jogo.setDebug();
                System.out.println("Debug: " + jogo.isDebug());
                if (jogo.isDebug()) {
                    JOptionPane.showMessageDialog(this, "Modo Debug Ativado", "Estado do Debug", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Modo Debug Desativado", "Estado do Debug", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "Fechar":
                jogo.getBgMusic().stop();
                System.exit(0);
                break;
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);

        // Configurações de fonte
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(139, 69, 19)); // Cor de fundo semelhante a madeira

        // Personalizando a borda do JTextField
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(184, 134, 11), 3), // Borda dourada
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Espaçamento interno
        ));

        // Deixa o fundo opaco para que a cor de fundo seja aplicada
        textField.setOpaque(true);

        return textField;
    }

    private void mostrarTelaEntradaNome() {
        // Remove os componentes centrais existentes, mas mantém o painel do título
        BackgroundPanel backgroundPanel = (BackgroundPanel) getContentPane();
        backgroundPanel.removeAll();

        JPanel titlePanel = createTitlePanel();
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Cria um novo painel para a tela de entrada do nome
        JPanel nameEntryPanel = new JPanel(new GridBagLayout());
        nameEntryPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Como você vai se chamar?");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 32));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        nameEntryPanel.add(nameLabel, gbc);

        // Usa o JTextField estilizado
        JTextField nameField = createStyledTextField();
        gbc.gridy = 1;
        nameEntryPanel.add(nameField, gbc);

        JButton continueButton = createStyledButton("Continuar");
        gbc.gridy = 2;
        nameEntryPanel.add(continueButton, gbc);

        // Adiciona o botão "Voltar"
        JButton voltarButton = createStyledButton("Voltar");
        gbc.gridy = 3;
        nameEntryPanel.add(voltarButton, gbc);

        backgroundPanel.add(nameEntryPanel, BorderLayout.CENTER);

        // Atualiza o layout do painel para refletir as mudanças
        backgroundPanel.revalidate();
        backgroundPanel.repaint();

        // Adiciona ação ao botão "Continuar"
        continueButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (!playerName.isEmpty()) {
                System.out.println("Nome do jogador: " + playerName);

                // Inicializa e exibe a tela de seleção de herói
                TelaSelecaoHeroi telaSelecaoHeroi = new TelaSelecaoHeroi(this.jogo, playerName);
                telaSelecaoHeroi.setVisible(true);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, insira um nome.");
            }
        });

        // Adiciona ação ao botão "Voltar"
        voltarButton.addActionListener(e -> {
            jogo.getBgMusic().stop();
            // Restaura a tela inicial
            initializeComponents();
            // Atualiza a tela
            revalidate();
            repaint();
        });
    }

    private void playBackgroundMusic() {
        try {
            jogo.setBgMusic(musicPlayer.playSound("/resources/music/title.wav", true));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a música de fundo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ResourceLoader.loadImage("/resources/images/bg-image.jpg");
            } catch (IOException e) {
                throw new RuntimeException("Imagem de fundo não encontrada.", e);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
