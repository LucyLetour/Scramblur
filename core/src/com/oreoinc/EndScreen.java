package com.oreoinc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class EndScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private int finalScore;

    public EndScreen(int finalScore) {
        batch = new SpriteBatch();
        this.finalScore = finalScore;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Hacked.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font = generator.generateFont(parameter); // font size 30 pixels
        parameter.size = 240;
        font2 = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Congratulations! Your final score was:", 250, 450);
        font2.setColor(Color.LIME);
        font2.draw(batch, finalScore + "", 450, 400);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        font2.dispose();
    }
}
