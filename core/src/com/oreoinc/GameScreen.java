package com.oreoinc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;
import java.util.Collections;

public class GameScreen implements Screen {
    private Image currentImage;
    private double timeElapsedS;
    private double scrambleTimeElapsed;
    private boolean timeHasElapsed;

    private final double ALLOTED_TIME_S = 15; //20 seconds
    private final double SCRAMBLE_TIME_S = 0.1; //0.1 seconds
    private final double WAIT_TIME_S = 2; // 2 seconds

    private final double MAX_RADIUS = 250;

    private int points;

    private final int ROUNDS_TO_PLAY = 6;

    private ArrayList<Image> images;

    final Label timeLeft;
    final Label pointsGained;

    private Stage stage;
    private SpriteBatch batch;
    private SpriteBatch fontBatch;
    private BitmapFont font;
    private Texture image;

    private boolean correct;
    private boolean shouldRenderText;
    private boolean firstRender;

    private Game game;

    public enum Image {
        CAMERA("camera"), DOG("dog"), ELEPHANT("elephant"), FISH("fish"), TAJ_MAHAL("taj mahal"), TAXI("taxi"), COMPUTER("computer"),
        SODA("soda"), BUS("bus"), CHAIR("chair"), LEAVES("leaves"), LIGHTBULB("light bulb"), LION("lion"), PHONE("phone");

        String guessKey;

        Image(String guessKey) {
            this.guessKey = guessKey;
        }

        public String getGuessKey() {
            return guessKey;
        }
    }

    public GameScreen(Game game) {
        images = new ArrayList<Image>();
        Collections.addAll(images, Image.values());
        Collections.shuffle(images);
        currentImage = images.remove(0);
        timeHasElapsed = false;
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        points = 0;
        stage = new Stage();
        this.game = game;
        firstRender = true;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Hacked.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font = generator.generateFont(parameter);
        Skin skin = new Skin(Gdx.files.internal("skins/skin/uiskin.json"));
        final TextField textField = new TextField("", skin);
        textField.setPosition(5, 5);
        textField.setSize(100, 20);
        textField.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER) {
                    if(checkAnswer(textField.getText()) && !timeHasElapsed) {
                        points += Math.round(ALLOTED_TIME_S - timeElapsedS);
                        timeHasElapsed = true;
                        timeElapsedS = 0;
                        correct = true;
                    }
                    textField.setText("");
                    return true;
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
        shouldRenderText = false;
        image = new Texture(Gdx.files.internal("pictures/" + currentImage.name() + ".jpg"));
        if(!timeHasElapsed) {
            if(timeElapsedS > ALLOTED_TIME_S) {
                System.out.println("a");
                timeElapsedS = 0;
                timeHasElapsed = true;
                correct = false;
            }
            else {
                if(scrambleTimeElapsed > SCRAMBLE_TIME_S || firstRender) {
                    Pixmap pixmap = Pixelizer.INSTANCE.scramble(new Pixmap(Gdx.files.internal("pictures/" + currentImage.name() + ".jpg")), (int)calculateRadius());
                    image = new Texture(pixmap);
                    if(scrambleTimeElapsed > SCRAMBLE_TIME_S) {
                        firstRender = false;
                    }
                }
            }
        }
        else {
            if (timeElapsedS > WAIT_TIME_S) {
                timeElapsedS = 0;
                timeHasElapsed = false;
                if(images.size() > Image.values().length - ROUNDS_TO_PLAY) {
                    currentImage = images.remove(0);
                }
                else {
                    game.setScreen(new EndScreen(points));
                }
            }
            else {
                //User wants to see if they were right
                shouldRenderText = true;
            }
        }



        //Update timers to use next frame to determine how to scramble etc.
        timeElapsedS += delta;
        scrambleTimeElapsed += delta;

        //Draw the image to the screen
        batch.begin();
        batch.draw(image, 0, 32, 992, 558);
        batch.end();

        //The time has run out / the user guessed correctly so diplay if they were right or not
        if(shouldRenderText) {
            font.setColor(correct ? Color.LIME : Color.RED);
            fontBatch.begin();
            font.draw(fontBatch, correct ? "Correct!" : "Answer Was:\n" + currentImage.getGuessKey(), 250, 450);
            fontBatch.end();
        }

        //Update points and time remaining labels
        pointsGained.setText("Points: " + points);
        timeLeft.setText("Time remaining: " + Math.round((timeHasElapsed ? WAIT_TIME_S : ALLOTED_TIME_S) - timeElapsedS));

        //Update stage
        stage.draw();
        stage.act();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    /**
     * Calculate the maximum offset of the pixels based on the time remaining to guess
     * @return the maximum offset of the pixels in pixels
     */
    public double calculateRadius() {
        double alpha = timeElapsedS / ALLOTED_TIME_S;
        return ((.35 / (alpha - 1.27)) + 1.27) * MAX_RADIUS;
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        fontBatch.dispose();
        font.dispose();
    }

    /**
     * Checks the user's answer against the accepted answer
     * @param guess The User's guess
     * @return Whether or not the user was correct
     */
    private boolean checkAnswer(String guess) {
        return guess.toLowerCase().trim().equals(currentImage.getGuessKey());
    }
}
