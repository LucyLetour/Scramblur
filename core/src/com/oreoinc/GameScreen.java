package com.oreoinc;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Collections;

public class GameScreen implements Screen {
    private Image currentImage;
    private long timeElapsedMs;
    private long scrambleTimeElapsed;
    private boolean timeHasElapsed;

    private final long ALLOTED_TIME_MS = 10000; //10 seconds
    private final long SCRAMBLE_TIME_MS = 500; //0.5 seconds
    private final long WAIT_TIME_MS = 2000; // 2 seconds

    private final double MAX_RADIUS = 100;
    private final double MIN_RADIUS = 0;

    public enum Image {
        CAMERA, DOG, ELEPHANT, FISH, TAJ_MAHAL, TAXI;
    }

    public GameScreen() {
        ArrayList<Image> images = new ArrayList<Image>();
        Collections.shuffle(images);
        currentImage = images.remove(0);
        timeHasElapsed = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(!timeHasElapsed) {
            if(timeElapsedMs > ALLOTED_TIME_MS) {
                timeElapsedMs = 0;
                timeHasElapsed = true;
            }
            else {
                if(scrambleTimeElapsed > SCRAMBLE_TIME_MS) {
                    //scramble calculateRadius();
                }
            }
        }
        else {
            if (timeElapsedMs > WAIT_TIME_MS) {
                timeElapsedMs = 0;
                timeHasElapsed = false;
            }
        }

        timeElapsedMs += delta;
        scrambleTimeElapsed += delta;

        
    }

    public double calculateRadius() {
        double alpha = (double)timeElapsedMs / ALLOTED_TIME_MS;
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
}
