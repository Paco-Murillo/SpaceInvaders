package mx.jml.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.lang.model.type.PrimitiveType;

class PantallaSpaceInvaders extends Pantalla {
    private final Juego juego;

    //Sistema de particulas
    private ParticleEffect sistemaParticulas;
    private ParticleEmitter emisorParticulas;

    // Pausa
    private EscenaPausa escenaPausa;
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;

    //Aliens
    private Array<Alien> alienArray;
    private Texture texturaAlien;
    private final int COLUMNAS = 11;
    private final int RENGLONES = 5;
    private final float DELTA_X = ANCHO*0.8f/COLUMNAS;
    private final float DELTA_Y = ALTO*0.4f/RENGLONES;
    private final float TIEMPO_PASO = 0.5f;
    private final int MAX_PASOS = 32;
    private int numeroPasos = MAX_PASOS/2;
    private Movimiento direccion = Movimiento.DERECHA;
    private final float paso = ANCHO*0.3f/MAX_PASOS;
    private final float pasoY = ALTO*0.4f/RENGLONES;
    private float timerAlienMover = 0;

    //Nave
    private Nave nave;
    private Texture texturaNave;
    private Movimiento movimiento = Movimiento.QUIETO;

    //Bala
    private Bala bala;
    private Texture texturaBala;

    //Marcador
    private Marcador marcador;

    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearTexturas();
        crearAliens();
        crearNave();
        crearMarcador();
        crearParticulas();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearParticulas() {
        sistemaParticulas = new ParticleEffect();
        sistemaParticulas.load(Gdx.files.internal("fuego.p"), Gdx.files.internal(""));
        Array<ParticleEmitter> emisores = sistemaParticulas.getEmitters();
        emisorParticulas = emisores.get(0);
        emisores.get(0).setPosition(ANCHO/2,ALTO/2);
        sistemaParticulas.start();
    }

    private void crearMarcador() {
        marcador = new Marcador(0.2f*ANCHO, 0.9f*ALTO);
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
        if(estadoJuego == EstadoJuego.JUGANDO) {
            actualizar(delta);
        }

        dibujar();

        if(estadoJuego == EstadoJuego.PAUSA) {
            escenaPausa.draw();
        }

    }

    private void dibujar() {
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        for (Alien alien : alienArray) { alien.render(batch); }
        nave.render(batch);
        if(bala!=null) { bala.render(batch); }
        marcador.render(batch);
        sistemaParticulas.draw(batch);

        batch.end();
    }

    private void actualizar(float delta) {
        moverNave();
        moverBala(delta);
        moverAliens(delta);
        sistemaParticulas.update(delta);
        if (bala!=null) {probarColisiones();}
    }

    private void moverAliens(float delta) {
        timerAlienMover += delta;
        if(timerAlienMover>=TIEMPO_PASO){
            float pasoDir = direccion==Movimiento.DERECHA?paso:-paso;
            for(Alien alien:alienArray){
                alien.mover(pasoDir);
            }
            numeroPasos++;
            if(numeroPasos>=MAX_PASOS){
                for(Alien alien:alienArray){
                    alien.bajar(pasoY);
                }
                direccion = direccion==Movimiento.DERECHA?Movimiento.IZQUIERDA:Movimiento.DERECHA;
                numeroPasos = 0;
            }
            timerAlienMover = 0;
        }
    }

    private void probarColisiones() {
        Rectangle balaBound = bala.sprite.getBoundingRectangle();
        for (Alien alien : alienArray) {
            Rectangle alienBound = alien.sprite.getBoundingRectangle();
            if(alienBound.overlaps(balaBound)){
                alienArray.removeValue(alien,true);
                bala = null;
                marcador.marcar(1);
                return;
            }
        }
    }

    private void moverBala(float delta) {
        if(bala!=null){
            bala.mover(delta);
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
                    //movimiento = Movimiento.IZQUIERDA;
                    if (escenaPausa == null){
                        estadoJuego = EstadoJuego.PAUSA;
                        escenaPausa = new EscenaPausa(vista, batch);
                    } else {
                        estadoJuego = EstadoJuego.JUGANDO;
                        escenaPausa.dispose();
                        escenaPausa = null;
                    }
                }
            } else {
                if(bala==null){
                    float xBala = nave.sprite.getX() + nave.sprite.getWidth()/2
                            - texturaBala.getWidth()/(float)2;
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
    private enum EstadoJuego{
        JUGANDO,
        PAUSA,
        GANO,
        PERDIO
    }

    private class EscenaPausa extends Stage {
        public EscenaPausa(Viewport viewport, Batch batch) {
            super(viewport, batch);

            Pixmap pixmap = new Pixmap((int)(ANCHO*0.7f), (int)(ALTO*0.8f), Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,0.5f);
            pixmap.fillCircle(300,300,300);

            Texture texturaCirculo = new Texture(pixmap);

            Image imgCirculo = new Image(texturaCirculo);
            imgCirculo.setPosition(ANCHO/2-pixmap.getWidth()/2f, ALTO/2-pixmap.getHeight()/2f);

            addActor(imgCirculo);
        }
    }
}
