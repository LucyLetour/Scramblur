package com.oreoinc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;
import java.util.Collections;

import static jdk.nashorn.internal.objects.Global.println;

public class GameScreen implements Screen {
    private Image currentImage;
    private double timeElapsedS;
    private double scrambleTimeElapsed;
    private boolean timeHasElapsed;

    private final double ALLOTED_TIME_S = 10; //10 seconds
    private final double SCRAMBLE_TIME_S = 0.5; //0.5 seconds
    private final double WAIT_TIME_S = 2; // 2 seconds

    private final double MAX_RADIUS = 100;
    private final double MIN_RADIUS = 0;

    private int points;

    private ArrayList<Image> images;

    final Label timeLeft;
    final Label pointsGained;

    private Stage stage;
    private SpriteBatch batch;
    private Texture image;

    public enum Image {
        CAMERA("camera"), DOG("dog"), ELEPHANT("elephant"), FISH("fish"), TAJ_MAHAL("taj mahal"), TAXI("taxi");

        String guessKey;

        Image(String guessKey) {
            this.guessKey = guessKey;
        }

        public String getGuessKey() {
            return guessKey;
        }
    }

    public GameScreen() {
        images = new ArrayList<Image>();
        Collections.addAll(images, Image.values());
        Collections.shuffle(images);
        currentImage = images.remove(0);
        timeHasElapsed = false;
        batch = new SpriteBatch();
        points = 0;
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("skins/skin/uiskin.json"));
        final TextField textField = new TextField("", skin);
        textField.setPosition(5, 5);
        textField.setSize(100, 20);
        textField.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER && checkAnswer(textField.getText()) && !timeHasElapsed) {
                    points++;
                    timeHasElapsed = true;
                    timeElapsedS = 0;
                    textField.setText("");
                }
                return true;
            }
        });

        timeLeft = new Label("", skin);
        timeLeft.setPosition(800, 0);
        timeLeft.setSize(50, 20);

        pointsGained = new Label("", skin);
        pointsGained.setPosition(450, 0);
        pointsGained.setSize(100, 20);

        stage.addActor(timeLeft);
        stage.addActor(pointsGained);
        stage.addActor(textField);
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        image = new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888));
        if(!timeHasElapsed) {
            if(timeElapsedS > ALLOTED_TIME_S) {
                timeElapsedS = 0;
                timeHasElapsed = true;
            }
            else {
                if(scrambleTimeElapsed > SCRAMBLE_TIME_S) {
                    Pixmap pixmap = Pixelizer.INSTANCE.scramble(new Pixmap(Gdx.files.internal("pictures/" + currentImage.name() + ".jpg")), (int)calculateRadius());
                    image = new Texture(pixmap);//scramble calculateRadius();
                }
            }
        }
        else {
            if (timeElapsedS > WAIT_TIME_S) {
                timeElapsedS = 0;
                timeHasElapsed = false;
                currentImage = images.remove(0);
            }
        }

        timeElapsedS += delta;
        scrambleTimeElapsed += delta;

        batch.begin();
        batch.draw(image, 0, 32, 992, 558);
        batch.end();

        pointsGained.setText("Points: " + points);
        timeLeft.setText("Time remaining: " + Math.round((timeHasElapsed ? WAIT_TIME_S : ALLOTED_TIME_S) - timeElapsedS));

        stage.draw();
        stage.act();
    }

    public double calculateRadius() {
        double alpha = timeElapsedS / ALLOTED_TIME_S;
        return (1 - alpha) * MAX_RADIUS + alpha * MIN_RADIUS;
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

    }
    private boolean checkAnswer(String guess) {
        if(guess.toLowerCase().trim().equals(currentImage.getGuessKey())) {
            timeHasElapsed = true;
            return true;
        }
        return false;
    }
}
