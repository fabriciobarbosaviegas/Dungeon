package Interface;

import Components.EstilizacaoBotoes;
import Personagem.*;
import Utils.ResourceLoader;
import Jogo.Jogo;
import Utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TelaBatalha extends JDialog {
    private final Jogo jogo;
    private final Heroi heroi;
    private final Monstro monstro;
    private final JFrame parent;
    private final JProgressBar pbHeroiHP;
    private final JProgressBar pbMonstroHP;
    private final JLabel lblElixir;
    private final JTextArea txtLog;
    private final SoundPlayer soundPlayer;

    public TelaBatalha(JFrame parent, Jogo jogo, Monstro monstro) {
        super(parent, "Batalha", true); // Torna o diálogo modal
        this.heroi = jogo.getPlayer();
        this.monstro = monstro;
        EstilizacaoBotoes estilizadorBotoes = new EstilizacaoBotoes();  // Instancia a classe de estilização
        this.jogo = jogo;
        this.soundPlayer = new SoundPlayer();
        this.parent = parent;

        if(monstro instanceof Chefao){
            soundPlayer.playSound("/resources/sound/vodka.wav", false);
            jogo.setBgMusic(soundPlayer.playSound("/resources/music/boss.wav", true));
        }
        else{
            jogo.setBgMusic(soundPlayer.playSound("/resources/music/battle.wav", true));
        }

        setTitle("Batalha");
        setSize(815, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Fundo da batalha
        JLabel lblBackground;
        try {
            lblBackground = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/bg-heroesSelection.jpg")));
        } catch (IOException e) {
            lblBackground = new JLabel("Fundo não encontrado.");
        }
        mainPanel.add(lblBackground, BorderLayout.CENTER);
        lblBackground.setLayout(null);

        // Imagem do herói
        JLabel lblHeroi;
        try {
            if(heroi instanceof Paladino) {
                lblHeroi = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/paladino-select.gif")));
            } else if (heroi instanceof Barbaro) {
                lblHeroi = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/barbaro-select.gif")));
            }
            else{
                lblHeroi = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/guerreio-select.gif")));
            }
        } catch (IOException e) {
            lblHeroi = new JLabel("Imagem do herói não encontrada.");
        }
        lblHeroi.setBounds(500, 300, 128, 128);
        lblBackground.add(lblHeroi);

        // Imagem do monstro
        JLabel lblMonstro;
        try {
            if(monstro instanceof Chefao) {
                lblMonstro = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/marilton-battle.png")));
            }
            else{
                lblMonstro = new JLabel(new ImageIcon(ResourceLoader.loadImage("/resources/images/capanga-battle.png")));
            }
        } catch (IOException e) {
            lblMonstro = new JLabel("Imagem do monstro não encontrada.");
        }
        lblMonstro.setBounds(100, 300, 128, 128);
        lblBackground.add(lblMonstro);

        // Barras de HP
        pbHeroiHP = new JProgressBar(0, heroi.getMaxHp()); // Assume um método getHpMax() para o valor máximo de HP
        pbHeroiHP.setValue(heroi.getHp());
        pbHeroiHP.setStringPainted(true);
        pbHeroiHP.setForeground(Color.GREEN);
        pbHeroiHP.setBounds(500, 270, 128, 20);
        lblBackground.add(pbHeroiHP);

        pbMonstroHP = new JProgressBar(0, monstro.getMaxHp()); // Assume um método getHpMax() para o valor máximo de HP
        pbMonstroHP.setValue(monstro.getHp());
        pbMonstroHP.setStringPainted(true);
        pbMonstroHP.setForeground(Color.RED);
        pbMonstroHP.setBounds(100, 270, 128, 20);
        lblBackground.add(pbMonstroHP);

        // Labels para os nomes dos personagens
        JLabel lblHeroiNome = new JLabel(heroi.getNome()); // Assume um método getNome() para o nome do herói
        lblHeroiNome.setFont(new Font("Arial", Font.BOLD, 14));
        lblHeroiNome.setForeground(Color.WHITE);
        lblHeroiNome.setBounds(500, 250, 128, 20); // Posiciona acima da barra de HP
        lblHeroiNome.setHorizontalAlignment(SwingConstants.CENTER);
        lblBackground.add(lblHeroiNome);

        JLabel lblMonstroNome = new JLabel(monstro.getNome()); // Assume um método getNome() para o nome do monstro
        lblMonstroNome.setFont(new Font("Arial", Font.BOLD, 14));
        lblMonstroNome.setForeground(Color.WHITE);
        lblMonstroNome.setBounds(100, 250, 128, 20); // Posiciona acima da barra de HP
        lblMonstroNome.setHorizontalAlignment(SwingConstants.CENTER);
        lblBackground.add(lblMonstroNome);

        // Painel de informações de Elixir
        JPanel pnlInfo = new JPanel(new GridLayout(1, 2));
        pnlInfo.setOpaque(false);
        lblElixir = new JLabel("Elixir: " + heroi.getInventario().size());
        lblElixir.setFont(new Font("Arial", Font.BOLD, 16));
        lblElixir.setForeground(Color.WHITE);
        pnlInfo.add(lblElixir);
        lblBackground.add(pnlInfo);
        pnlInfo.setBounds(10, 10, 780, 50);

        // Área de log de batalha
        txtLog = new JTextArea();
        txtLog.setFont(new Font("Arial", Font.PLAIN, 14));
        txtLog.setForeground(Color.WHITE);
        txtLog.setBackground(new Color(0, 0, 0, 150));  // Fundo preto com transparência
        txtLog.setMargin(new Insets(10, 10, 10, 10));  // Padding interno
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11), 2));  // Borda ao redor do JScrollPane
        lblBackground.add(scrollPane);
        scrollPane.setBounds(10, 400, 780, 150);

        // Painel inferior com botões de ação
        JPanel pnlActions = new JPanel(new FlowLayout());
        pnlActions.setOpaque(false);
        JButton btnAtacar = new JButton("Atacar");
        JButton btnHabilidade = new JButton("Habilidade Especial");
        JButton btnElixir = new JButton("Usar Elixir");

        // Aplica o estilo aos botões
        estilizadorBotoes.estilizarBotao(btnAtacar);
        estilizadorBotoes.estilizarBotao(btnHabilidade);
        estilizadorBotoes.estilizarBotao(btnElixir);

        pnlActions.add(btnAtacar);
        pnlActions.add(btnHabilidade);
        pnlActions.add(btnElixir);
        lblBackground.add(pnlActions);
        pnlActions.setBounds(10, 550, 780, 50);

        txtLog.append("\n\n\n"+heroi.getNome()+" encontrou "+monstro.getNome()+"!\n");

        // Listeners dos botões
        btnAtacar.addActionListener(e -> realizarAtaque());
        btnHabilidade.addActionListener(e -> {
            if(!this.heroi.isUsouHabilidadeEspecial()) {
                usarHabilidade();
            }
            else{
                txtLog.append("Você não pode mais usar sua habilidade especial!\n");
            }
        });
        btnElixir.addActionListener(e -> usarElixir());

        setVisible(true);
    }

    private void realizarAtaque() {
        this.soundPlayer.playSound("/resources/sound/default-atack.wav", false);
        heroi.ataque(monstro, 0);

        if(this.heroi instanceof Guerreiro && ((Guerreiro) this.heroi).getTurnoHabilidadeAtivada() > 2){
            ((Guerreiro) this.heroi).desfazerHabilidadeEspecial();
        }
        else if(this.heroi instanceof Guerreiro){
            ((Guerreiro) this.heroi).setTurnoHabilidadeAtivada();
        }

        txtLog.append(heroi.getNome()+" atacou "+monstro.getNome()+"!\n");

        if (monstro.getHp() > 0) {
            monstro.ataque(heroi, 0);
            txtLog.append(monstro.getNome()+" atacou "+heroi.getNome()+"!\n");
        }

        atualizarStatus();
        verificarFimDeJogo();
    }

    private void usarHabilidade() {
        if(this.heroi instanceof Barbaro) {
            heroi.habilidadeEspecial(this.monstro);
        }
        else{
            heroi.habilidadeEspecial();
        }
        System.out.println(this.heroi.isUsouHabilidadeEspecial());
        txtLog.append(heroi.getNome()+" usou sua habilidade especial!\n");

        if (monstro.getHp() > 0) {
            monstro.ataque(heroi, 0);
            txtLog.append(monstro.getNome()+" atacou "+heroi.getNome()+"!\n");
        }

        atualizarStatus();
        verificarFimDeJogo();
    }

    private void usarElixir() {
        if (!heroi.getInventario().isEmpty()) {
            heroi.usarElixir();
            txtLog.append("Você usou um elixir!\n");
            atualizarStatus();
        } else {
            txtLog.append("Você não possui elixir!\n");
        }
    }

    private void atualizarStatus() {
        pbHeroiHP.setValue(heroi.getHp());
        pbMonstroHP.setValue(monstro.getHp());
        lblElixir.setText("Elixir: " + heroi.getInventario().size());
        revalidate();
        repaint();
    }

    private void verificarFimDeJogo() {
        if (heroi.getHp() <= 0) {
            txtLog.append("Você foi derrotado!\n");
            fimDeJogo(false);
        } else if (monstro.getHp() <= 0) {
            txtLog.append("Você derrotou o monstro!\n");
            fimDeJogo(true);
        }
    }

    private void fimDeJogo(boolean vitoria) {
        String mensagem = vitoria ? "Você venceu a batalha!" : "Você perdeu a batalha.";
        if(vitoria && monstro instanceof Capanga){
            this.soundPlayer.playSound("/resources/sound/battle-win.wav", false);
            this.heroi.vencerBatalha();
            this.jogo.getBgMusic().stop();
            this.jogo.setBgMusic(this.soundPlayer.playSound("/resources/music/game.wav", true));
            JOptionPane.showMessageDialog(this, mensagem);
            dispose();
        } else if(vitoria && monstro instanceof Chefao){
            TelaGameOver tela = new TelaGameOver(this.parent,true, this.jogo);
            jogo.getBgMusic().stop();
            tela.setVisible(true);
            this.parent.dispose();
            dispose();
        }
        else{
            this.heroi.setUsouHabilidadeEspecial();
            dispose();
        }
    }
}
