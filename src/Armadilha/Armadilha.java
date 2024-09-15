package Armadilha;
import Prop.Prop;
import Personagem.Heroi;
import Dungeon.Dungeon;

abstract public class Armadilha extends Prop{
    private int dano;

    public Armadilha(int dano) {
        this.setDano(dano);
    }

    public int getDano() {
        return dano;
    }
    public void setDano(int dano) {
        this.dano = dano;
    }
    abstract public void calcularDano(Heroi alvo);

    @Override
    public abstract void posiciona(Dungeon tabuleiro);
}
