package Personagem;

public class Barbaro extends Heroi{
    public Barbaro(String nome) {
        super(nome, 15, 10, 10);
    }

    @Override
    public void habilidadeEspecial(Monstro alvo){
        this.soundPlayer.playSound("/resources/sound/barbaro-especial.wav", false);
        System.out.println("Golpe furioso");
        this.ataque(alvo, 50);
        this.setUsouHabilidadeEspecial();
    }
}
