package mx.jml.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Personaje extends Objeto {

    private Animation animation;
    private float timerAnimation;
    private float x,y;

    private EstadoMovimiento estado;

    public Personaje(Texture textura, float x, float y) {
        super(textura, x, y);
        this.x = x;
        this.y = y;
        estado = EstadoMovimiento.QUIETO;
        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturaPersonaje = region.split(32,64);

        animation = new Animation(0.25f, texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        timerAnimation = 0;

        sprite = new Sprite(texturaPersonaje[0][0]);
        if(estado == EstadoMovimiento.IZQUIERDA) {
            sprite.flip(true, false);
        }
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch){
        if (estado == EstadoMovimiento.QUIETO){
            sprite.draw(batch);
        }else{
            timerAnimation += Gdx.graphics.getDeltaTime();
            TextureRegion region = (TextureRegion)animation.getKeyFrame(timerAnimation);
            batch.draw(region, x, y);
        }
    }

    public void setEstado(EstadoMovimiento estado) {
        this.estado = estado;
    }


    public enum EstadoMovimiento {
        QUIETO,
        IZQUIERDA,
        DERECHA
    }
}
