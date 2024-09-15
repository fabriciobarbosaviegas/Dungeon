package Personagem;

public class Guerreiro extends Heroi{
    private int turnoHabilidadeAtivada;
    private int defesaNormal;

    public Guerreiro(String nome) {
        super(nome, 10, 15, 15);
        this.defesaNormal = 15;
        this.turnoHabilidadeAtivada = 0;
    }
    public int getTurnoHabilidadeAtivada() {
        return turnoHabilidadeAtivada;
    }
    public void setTurnoHabilidadeAtivada() {
        this.turnoHabilidadeAtivada++;
    }

    @Override
    public void habilidadeEspecial() {
        System.out.println("Postura defensiva");
        this.defesaNormal = this.getDefensa();
        this.setUsouHabilidadeEspecial();
        this.setDefensa(this.getDefensa() + this.getDefensa()/2);
    }

    public void desfazerHabilidadeEspecial() {
        System.out.println("Desfaze habilidade");
        this.setDefensa(this.defesaNormal);
    }
}
