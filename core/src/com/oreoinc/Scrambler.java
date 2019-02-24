package com.oreoinc;

import com.badlogic.gdx.graphics.Pixmap;
import util.ColorParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scrambler {

    private Scrambler() {
    }

    public static class Pixel {
        public final int x, y, color;
        public Pixel(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public static Pixmap scramble(Pixmap pixels, int maxRadius) {
        final int width = pixels.getWidth(), height = pixels.getHeight(), radius = maxRadius;
        Pixmap scrambled = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        List<Pixel> valids = new ArrayList<Pixel>();
        System.out.println("herro");
        for(int xActual = 0; xActual < width; xActual++) {
            for(int yActual = 0; yActual < height; yActual++) {
                for(int i = 0; i < width; i++) {
                    for(int j = 0; j < height; j++) {
                        if(isValidPix(pixels, i, j, xActual, yActual, radius)) {
                            valids.add(new Pixel(i, j, pixels.getPixel(i, j)));
                            System.out.println("wowwed");
                        }
                    }
                }
                Random random = new Random();
                Pixel winner = valids.get(random.nextInt(valids.size()));
                scrambled.drawPixel(winner.x, winner.y, winner.color);
                valids.clear();
            }
        }
        return scrambled;
    }

    private static boolean isValidPix(Pixmap pixels, int x, int y, int xActual, int yActual, int radius) {
        return x < pixels.getWidth() && x >= 0 && y < pixels.getHeight() && y >= 0 &&
                ColorParser.getChannelFromRGBA8888(pixels.getPixel(x, y), "alpha")!= 0 &&
                Math.sqrt(Math.pow(x - xActual, 2) + Math.pow(y - yActual, 2)) <= radius;
    }
}
