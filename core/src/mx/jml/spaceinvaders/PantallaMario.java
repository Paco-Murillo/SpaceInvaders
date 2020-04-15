package mx.jml.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static mx.jml.spaceinvaders.Personaje.EstadoMovimiento;

class PantallaMario extends Pantalla {

    private Juego juego;

    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Musica
    private Music musicaFondo;
    private Sound soundEffect;
    private Music marioMuere;

    //Personaje
    private Personaje mario;

    //HUD
    private Stage escenaHUD;
    private OrthographicCamera orthographicCameraHUD;
    private Viewport viewportHUD;



    public PantallaMario(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarMapa();
        cargarMario();
        //Gdx.input.setInputProcessor(new ProcesadorEntrada());
        crearHUD();
        Gdx.input.setInputProcessor(escenaHUD);
    }

    private void crearHUD() {
        orthographicCameraHUD = new OrthographicCamera(ANCHO, ALTO);
        orthographicCameraHUD.position.set(ANCHO/2,ALTO/2,0);
        orthographicCameraHUD.update();
        viewportHUD = new StretchViewport(ANCHO,ALTO,orthographicCameraHUD);

        Skin skin = new Skin();
        skin.add("fondo", new Texture("padBack.png"));
        skin.add("boton", new Texture("padKnob.png"));
        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("fondo");
        estilo.knob = skin.getDrawable("boton");

        //Crear el pad
        Touchpad pad = new Touchpad(64, estilo);
        pad.setBounds(16,16,256,256);
        pad.setColor(1,1,1,0.7f);
        //Eventos
        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad)actor;
                if(pad.getKnobX()>0){
                    mario.setEstado(EstadoMovimiento.DERECHA);
                }else if(pad.getKnobX()<0){
                    mario.setEstado(EstadoMovimiento.IZQUIERDA);
                }else{
                    mario.setEstado(EstadoMovimiento.QUIETO);
                }
            }
        });

        escenaHUD = new Stage(viewportHUD);
        escenaHUD.addActor(pad);


    }

    private void cargarMario() {
        Texture texturaMario = new Texture("marioSprite.png");
        mario = new Personaje(texturaMario, 100, 64);
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("MapaMario.tmx", TiledMap.class);
        manager.load("marioBros.mp3", Music.class);
        manager.load("moneda.mp3", Sound.class);
        manager.load("muereMario.mp3", Music.class);
        manager.finishLoading();

        mapa = manager.get("MapaMario.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);
        musicaFondo = manager.get("marioBros.mp3");
        musicaFondo.setLooping(true);
        //musicaFondo.play();
        soundEffect = manager.get("moneda.mp3");
        marioMuere = manager.get("muereMario.mp3");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        mapRenderer.setView(camara);
        mapRenderer.render();

        batch.begin();
        mario.render(batch);
        batch.end();

        //HUD

        batch.setProjectionMatrix(orthographicCameraHUD.combined);
        escenaHUD.draw();


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
            //soundEffect.play();
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
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
}
