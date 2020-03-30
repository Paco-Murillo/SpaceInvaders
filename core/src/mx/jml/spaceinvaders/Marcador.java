package mx.jml.spaceinvaders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Marcador {
    private int puntos;

    private float xVariable;
    private float yVariable;

    private Texto texto;

    public Marcador(float xVariable, float yVariable){
        this.xVariable = xVariable;
        this.yVariable = yVariable;
        puntos = 0;
        texto = new Texto("Fuente.fnt");
    }

    public void reset(){
        puntos = 0;
    }

    public void marcar(int puntos){
        this.puntos += puntos;
    }

    public void render(SpriteBatch batch){
        String mensaje = "Puntos "+puntos;
        texto.render(batch, mensaje, xVariable, yVariable);
    }
}
