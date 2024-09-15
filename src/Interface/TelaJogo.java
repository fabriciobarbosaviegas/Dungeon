package Interface;

import Armadilha.PerdaAleatoria;
import Components.EstilizacaoBotoes;
import Jogo.Jogo;
import Personagem.*;
import Utils.ResourceLoader;
import Utils.SoundPlayer;
import Armadilha.PerdaFixa;
import Elixir.Elixir;
import Prop.Prop;
import Dungeon.Dungeon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

import Components.TexturedPanel;

public class TelaJogo extends JFrame {
    private static final int ROWS = 5;
    private static final int COLS = 10;
    private final GridCell[][] buttons = new GridCell[ROWS][COLS];
    private final Dungeon dungeon;
    private final Jogo jogo;
    private JLabel heroAttributes;
    private final SoundPlayer musicPlayer;
    private JProgressBar hpBar;

    public TelaJogo(Jogo jogo) {
        this.jogo = jogo;
        this.dungeon = this.jogo.getTabuleiro();
        this.musicPlayer = new SoundPlayer();

        setTitle("Jogo");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = criarPainelMapa();
        JPanel sidePanel = criarPainelLateral();

        add(boardPanel, BorderLayout.CENTER); // Painel do mapa ao centro
        add(sidePanel, BorderLayout.EAST); // Painel lateral à direita

        this.jogo.getBgMusic().stop();
        jogo.setBgMusic(musicPlayer.playSound("/resources/music/game.wav", true));
        updateBoard();

        // Adicionar suporte ao teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        // Para garantir que o JFrame receba os eventos de teclado
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        setVisible(true);
    }

    public static class GridCell extends JPanel {
        private final JLabel imageLabel;

        public GridCell() {
            setLayout(new BorderLayout());
            setOpaque(false); // Torna o painel transparente
            setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Adiciona uma borda preta
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            add(imageLabel, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredSize() {
            int availableWidth = getParent().getWidth();
            int availableHeight = getParent().getHeight();

            int cellWidth = availableWidth / COLS;
            int cellHeight = availableHeight / ROWS;

            int size = Math.min(cellWidth, cellHeight);
            return new Dimension(size, size);
        }

        public void setImageIcon(ImageIcon icon) {
            imageLabel.setIcon(icon);
        }

        public void clear() {
            imageLabel.setIcon(null);
        }
    }

    private JPanel criarPainelMapa() {
        Image backgroundImage = null;
        try {
            backgroundImage = ResourceLoader.loadImage("/resources/images/dungeon-bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BackgroundPanel boardPanel = new BackgroundPanel(backgroundImage);
        boardPanel.setLayout(new GridLayout(ROWS, COLS, 0, 0)); // Define o espaçamento entre as células para 0
        boardPanel.setPreferredSize(new Dimension(500, 500)); // Ajuste conforme necessário

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GridCell cell = new GridCell();
                buttons[i][j] = cell;
                boardPanel.add(cell);
            }
        }

        // Adiciona o listener para redimensionamento
        boardPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        buttons[i][j].revalidate();
                    }
                }
            }
        });

        return boardPanel;
    }

    private JPanel criarPainelLateral() {
        TexturedPanel sidePanel = new TexturedPanel("/resources/images/texture.jpg");
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel playerNameLabel = new JLabel(this.jogo.getPlayer().getNome());
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Defina a fonte e o tamanho do texto
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centralizar o texto

        // Painel de informações do herói
        JLabel heroImage;
        try {
            ImageIcon originalIcon;
            if (this.jogo.getPlayer() instanceof Paladino) {
                originalIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/paladino-down.png"));
            } else if (this.jogo.getPlayer() instanceof Barbaro) {
                originalIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/barbaro-down.png"));
            } else {
                originalIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/guerreiro-down.png"));
            }

            // Redimensiona a imagem
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            heroImage = new JLabel(new ImageIcon(scaledImage));

        } catch (IOException e) {
            heroImage = new JLabel("Imagem não encontrada");
            e.printStackTrace();
        }

        // Barra de HP
        hpBar = new JProgressBar(0, dungeon.getPlayer().getMaxHp());  // Inicialize hpBar aqui
        hpBar.setValue(dungeon.getPlayer().getHp());
        hpBar.setStringPainted(true);
        hpBar.setForeground(new Color(0, 255, 21, 181));

        heroAttributes = new JLabel(updateHeroAttributes());
        heroAttributes.setFont(new Font("Arial", Font.PLAIN, 14));

        // Centralizando a imagem e atributos do herói
        JPanel heroInfoPanel = new JPanel();
        heroInfoPanel.setLayout(new BoxLayout(heroInfoPanel, BoxLayout.Y_AXIS));
        heroInfoPanel.setBorder(BorderFactory.createTitledBorder("Informações do Herói"));
        heroInfoPanel.add(Box.createVerticalGlue()); // Espaço flexível acima
        heroInfoPanel.add(centerComponent(playerNameLabel)); // Adiciona o nome do jogador
        heroInfoPanel.add(centerComponent(heroImage));
        heroInfoPanel.add(centerComponent(hpBar)); // Adiciona a barra de HP ao painel
        heroInfoPanel.add(centerComponent(heroAttributes));
        heroInfoPanel.add(Box.createVerticalGlue()); // Espaço flexível abaixo

        // Painel de ações
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Ações"));

        JButton moveButton = new JButton("Movimentar Herói");
        estilizarBotao(moveButton);
        moveButton.addActionListener(new MoveHeroListener());

        JButton hintButton = new JButton("Dica");
        estilizarBotao(hintButton);
        hintButton.addActionListener(new HintListener());

        JButton exitButton = new JButton("Sair");
        estilizarBotao(exitButton);
        exitButton.addActionListener(new ExitListener());

        // Centralizando os botões de ação
        actionPanel.add(centerComponent(moveButton));
        actionPanel.add(centerComponent(hintButton));
        actionPanel.add(centerComponent(exitButton));

        sidePanel.add(heroInfoPanel);
        sidePanel.add(actionPanel);

        return sidePanel;
    }

    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Desenha a imagem de fundo redimensionada para o tamanho do painel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void estilizarBotao(JButton botao) {
        EstilizacaoBotoes estilizador = new EstilizacaoBotoes();
        estilizador.estilizarBotao(botao);
    }

    private void updateHpBar(JProgressBar hpBar) {
        Heroi hero = dungeon.getPlayer();
        hpBar.setValue(hero.getHp());
        hpBar.setString(String.format("%d/%d HP", hero.getHp(), hero.getMaxHp()));
        this.jogo.gameOver(this);
    }

    private JPanel centerComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(component);
        return panel;
    }


    private void updateBoard() {
        ImageIcon heroIcon = null;
        ImageIcon bossIcon = null;
        ImageIcon randomIcon = null;
        ImageIcon fixedIcon = null;
        ImageIcon elixirIcon = null;
        ImageIcon minionIcon = null;

        try {
            if(this.jogo.getPlayer() instanceof Paladino) {
                heroIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/paladino-down.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }
            else if(this.jogo.getPlayer() instanceof Barbaro) {
                heroIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/barbaro-down.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }
            else{
                heroIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/guerreiro-down.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }
            bossIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/marilton.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            randomIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/trap.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            fixedIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/armadilha-fixa.gif"));
            elixirIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/elixir.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            minionIcon = new ImageIcon(ResourceLoader.loadImage("/resources/images/capanga.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isDebug = this.jogo.isDebug();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GridCell cell = buttons[i][j];
                Prop prop = dungeon.getConteudoPosicao(i, j);
                System.out.println(dungeon.getConteudoPosicao(i, j));
                System.out.println(Arrays.toString(dungeon.getPlayer().getPosicao()));

                if (prop instanceof Heroi) {
                    System.out.println(i+", "+j);
                    cell.setImageIcon(heroIcon); // Define a imagem do herói
                } else if (prop instanceof Chefao) {
                    cell.setImageIcon(bossIcon); // Define a imagem do chefão
                } else if (prop instanceof PerdaFixa && isDebug) {
                    cell.setImageIcon(fixedIcon); // Define a imagem da armadilha
                } else if (prop instanceof PerdaAleatoria && isDebug) {
                    cell.setImageIcon(randomIcon); // Define a imagem do elixir
                } else if (prop instanceof Elixir && isDebug) {
                    cell.setImageIcon(elixirIcon); // Define a imagem do elixir
                } else if (prop instanceof Capanga && isDebug) {
                    cell.setImageIcon(minionIcon); // Define a imagem do capanga
                } else{
                    cell.clear();
                }
            }
        }
    }

    private String updateHeroAttributes() {
        Heroi hero = dungeon.getPlayer();
        return String.format(
                "<html>ATK: %d<br>DEF: %d<br>Elixires: %d<br>Dicas: %d</html>",
                hero.getAtaque(),
                hero.getDefensa(),
                hero.getInventario().size(),
                jogo.getDicasRestantes()
        );
    }

    private void handleKeyPress(KeyEvent e) {
        int x = this.jogo.getPlayer().getPosicao()[0];
        int y = this.jogo.getPlayer().getPosicao()[1];
        System.out.println(x+", "+y);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (x > 0) {
                    this.dungeon.mostrarTabuleiro(true);
                    musicPlayer.playSound("/resources/sound/steps.wav", false);
                    moveHero(x - 1, y);
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (x < ROWS - 1) {
                    this.dungeon.mostrarTabuleiro(true);
                    musicPlayer.playSound("/resources/sound/steps.wav", false);
                    moveHero(x + 1, y);
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (y > 0) {
                    this.dungeon.mostrarTabuleiro(true);
                    musicPlayer.playSound("/resources/sound/steps.wav", false);
                    moveHero(x, y - 1);
                }
            }
            case KeyEvent.VK_RIGHT -> {
                if (y < COLS - 1) {
                    this.dungeon.mostrarTabuleiro(true);
                    musicPlayer.playSound("/resources/sound/steps.wav", false);
                    moveHero(x, y + 1);
                }
            }
        }
    }

    private void moveHero(int newX, int newY) {
        Heroi hero = dungeon.getPlayer();
        dungeon.verificaArmadilha(newX, newY); // Verifica armadilhas na nova posição
        dungeon.verificaElixir(newX, newY); // Verifica elixires na nova posição
        if(dungeon.verificaBatalha(newX, newY)){
            jogo.getBgMusic().stop();
            new TelaBatalha(this, this.jogo, (Monstro) this.dungeon.getConteudoPosicao(newX, newY));
            dungeon.removerProp(dungeon.getConteudoPosicao(newX, newY));
        }
        dungeon.removerHeroi(); // Remove o herói da posição atual
        hero.setPosicao(newX, newY); // Move o herói para a nova posição
        dungeon.posicionaHeroi(); // Posiciona o herói na nova posição no tabuleiro
        updateBoard(); // Atualiza a interface gráfica
        heroAttributes.setText(updateHeroAttributes()); // Atualiza os atributos do herói na interface
        updateHpBar(hpBar); // Atualiza a barra de HP do herói
    }

    private class MoveHeroListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Use as setas do teclado para mover o herói.");
            requestFocusInWindow();
        }
    }

    private class HintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String column = JOptionPane.showInputDialog("Informe a coluna para obter uma dica:");
            try {
                int colIndex = Integer.parseInt(column) - 1;
                int trapsInColumn = jogo.usarDica(colIndex);
                JOptionPane.showMessageDialog(null, "Existem " + trapsInColumn + " armadilhas na coluna " + column);
                heroAttributes.setText(updateHeroAttributes()); // Atualiza a interface com o número de dicas restantes
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(null, "Coluna inválida.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            requestFocusInWindow();
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(null, "Deseja realmente sair do jogo?", "Sair", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose();
                showRestartOptions();
            }
            else{
                requestFocusInWindow();
            }
        }

        private void showRestartOptions() {
            EstilizacaoBotoes estilizador = new EstilizacaoBotoes();

            JFrame restartFrame = new JFrame("Jogo Encerrado");
            restartFrame.setSize(300, 150);
            restartFrame.setLayout(new GridLayout(2, 1));

            JButton restartButton = new JButton("Reiniciar Jogo");
            restartButton.addActionListener(e -> {
                restartFrame.dispose();
                jogo.reiniciar();
                dispose();
            });
            estilizador.estilizarBotao(restartButton);
            restartFrame.add(restartButton);

            JButton newGameButton = new JButton("Novo Jogo");
            newGameButton.addActionListener(e -> {
                new Jogo();
                jogo.getBgMusic().stop();
                restartFrame.dispose();
            });
            estilizador.estilizarBotao(newGameButton);
            restartFrame.add(newGameButton);

            restartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            restartFrame.setVisible(true);
        }
    }
}
