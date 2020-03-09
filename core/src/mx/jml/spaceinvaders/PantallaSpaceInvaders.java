package mx.jml.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class PantallaSpaceInvaders extends Pantalla {
    private final Juego juego;

    //Aliens
    private Array<Alien> alienArray;
    private Texture texturaAlien;
    private final int COLUMNAS = 11;
    private final int RENGLONES = 5;
    private final float DELTA_X = ANCHO*0.8f/COLUMNAS;
    private final float DELTA_Y = ALTO*0.4f/RENGLONES;

    //Nave
    private Nave nave;
    private Texture texturaNave;
    private Movimiento movimiento = Movimiento.QUIETO;

    //Bala
    private Bala bala;
    private Texture texturaBala;

    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearTexturas();
        crearAliens();
        crearNave();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearNave() {
        nave = new Nave(texturaNave,ANCHO/2-texturaNave.getWidth()/2f,ALTO*0.05f);
    }

    private void crearTexturas() {
        texturaAlien = new Texture("space/enemigoArriba.png");
        texturaNave = new Texture("space/nave.png");
        texturaBala = new Texture("space/bala.png");
    }

    private void crearAliens() {

        alienArray = new Array<>(55);

        for(int x=0; x<COLUMNAS; x++){
            for (int y=0; y<RENGLONES; y++){

                Alien alien = new Alien(texturaAlien, x*DELTA_X + ANCHO*0.1f,
                        y*DELTA_Y+ALTO*0.45f);

                alienArray.add(alien);
            }
        }
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0,0,0);

        // Gdx.app.log("DELTA", delta+" s");

        //Actualizaciones
        moverNave();
        moverBala(delta);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        for (Alien alien : alienArray) { alien.render(batch); }
        nave.render(batch);
        if(bala!=null) { bala.render(batch); }

        batch.end();
    }

    private void moverBala(float delta) {
        if(bala!=null){
            bala.mover(delta);
            Rectangle balaBound = bala.sprite.getBoundingRectangle();
            for (Alien alien : alienArray) {
                Rectangle alienBound = alien.sprite.getBoundingRectangle();
                if(alienBound.overlaps(balaBound)){
                    alienArray.removeValue(alien,true);
                    bala = null;
                    return;
                }
            }
            if(bala.sprite.getY()>=ALTO){
                bala = null;
            }
        }
    }

    private void moverNave() {
        switch (movimiento){
            case DERECHA:
                nave.mover(10);
                break;
            case IZQUIERDA:
                nave.mover(-10);
                break;
            default:
                break;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 vector3 = new Vector3(screenX,screenY,0);
            camara.unproject(vector3);
            if(vector3.y >= ALTO/2) {
                if (vector3.x >= ANCHO / 2) {
                    //Derecha
                    movimiento = Movimiento.DERECHA;
                } else {
                    //Izquierda
                    movimiento = Movimiento.IZQUIERDA;
                }
            } else {
                if(bala==null){
                    float xBala = nave.sprite.getX() + nave.sprite.getWidth()/2
                            - texturaBala.getWidth()/2;
                    float yBala = nave.sprite.getY() + nave.sprite.getHeight();
                    bala = new Bala(texturaBala, xBala, yBala);
                }
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            movimiento = Movimiento.QUIETO;
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    private enum Movimiento {
        DERECHA,
        IZQUIERDA,
        QUIETO
    }
}
