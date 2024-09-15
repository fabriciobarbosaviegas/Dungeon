package Personagem;

public class Paladino extends Heroi{
    public Paladino(String nome) {
        super(nome, 10, 5, 20);
    }

    @Override
    public void habilidadeEspecial() {
        System.out.println("Recuperação!");
        this.recuperarHP(this.getMaxHp()/2);
        this.setUsouHabilidadeEspecial();
    }
}
