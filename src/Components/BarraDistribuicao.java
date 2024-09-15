package Components;

import javax.swing.*;
import java.awt.*;

public class BarraDistribuicao extends JProgressBar {
    private int valorAtual;

    public BarraDistribuicao(int min, int max) {
        super(min, max);
        this.valorAtual = min;
        setValue(valorAtual);
        setStringPainted(true);
        atualizarTexto();
        // Customiza a aparência da barra
        setForeground(new Color(8, 13, 126, 180));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11), 3));
        setFont(new Font("Arial", Font.BOLD, 16));
        setPreferredSize(new Dimension(250, 30));
    }

    public void setValorAtual(int valor) {
        this.valorAtual = valor;
        setValue(valorAtual);
        atualizarTexto();
    }

    private void atualizarTexto() {
        setString(String.valueOf(valorAtual));
    }
}
