package mx.jml.spaceinvaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Nave extends Objeto {

    public Nave(Texture textura, float x, float y) {
        super(textura, x, y);
        sprite.setColor(Color.GREEN);
    }

    public void mover(float dx){
        sprite.setX(sprite.getX()+dx);
    }
}
