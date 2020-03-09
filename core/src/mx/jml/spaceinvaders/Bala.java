package mx.jml.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;

public class Bala extends Objeto {

    //Velocidad
    private float YVelocity = 360; // pix / s

    public Bala (Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void mover(float dt){
        float dy = YVelocity * dt;
        sprite.setY(sprite.getY()+dy);
    }


}
