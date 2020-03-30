package mx.jml.spaceinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Texto {
    private BitmapFont font;

    public Texto(String archivo){
        font = new BitmapFont(Gdx.files.internal(archivo)); //Archivo teminación .fnt
    }

    public void render(SpriteBatch batch, String mensaje, float xVariable, float yVariable) {
        GlyphLayout glyph = new GlyphLayout();
        glyph.setText(font,mensaje);
        float anchoTexto = glyph.width;
        font.draw(batch, glyph, xVariable-anchoTexto/2, yVariable);
    }
}
