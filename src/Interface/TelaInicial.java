package Jogo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame{

    public TelaInicial(Jogo jogo) {

        // Configurações da janela principal
        setTitle("Bem-vindo ao Jogo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizar a janela

        // Layout da janela
        setLayout(new BorderLayout());

        // Criar um painel para os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(3, 1, 10, 10));

        // Criar botões
        JButton jogarButton = new JButton("Jogar");
        JButton debugButton = new JButton("DEBUG");
        JButton sairButton = new JButton("Sair");

        // Adicionar botões ao painel
        painelBotoes.add(jogarButton);
        painelBotoes.add(debugButton);
        painelBotoes.add(sairButton);

        // Adicionar painel à janela
        add(painelBotoes, BorderLayout.CENTER);

        // Criar um rótulo de boas-vindas
        JLabel mensagemBoasVindas = new JLabel("Bem-vindo ao Jogo!", SwingConstants.CENTER);
        mensagemBoasVindas.setFont(new Font("Arial", Font.BOLD, 18));

        // Adicionar rótulo à janela
        add(mensagemBoasVindas, BorderLayout.NORTH);

        // Ações dos botões
        jogarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogo.setDebug(false); // Define debug como falso
                jogo.iniciar(); // Inicia o jogo
                dispose(); // Fecha a tela inicial
            }
        });

        debugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogo.setDebug(true); // Define debug como verdadeiro
                System.out.println("Debug ativado!");
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha o jogo
            }
        });
    }}
