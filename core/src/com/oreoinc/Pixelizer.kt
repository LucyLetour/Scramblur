package com.oreoinc

import com.badlogic.gdx.graphics.Pixmap
import util.ColorParser

import java.util.ArrayList
import java.util.Random

object Pixelizer {

    class Pixel(val x: Int, val y: Int, val color: Int)

    fun scramble(pixels: Pixmap, maxRadius: Int): Pixmap {
        val width = pixels.width
        val height = pixels.height
        val scrambled = Pixmap(width, height, Pixmap.Format.RGBA8888)
        val valids = ArrayList<Pixel>()

        for(x in 0 until pixels.width) {
            for(y in 0 until pixels.height) {
                
            }
        }

        println(ColorParser.getChannelFromRGBA8888(scrambled.getPixel(0,0), "alpha"))
        return scrambled
    }

    private fun isValidPix(pixels: Pixmap, x: Int, y: Int, xActual: Int, yActual: Int, radius: Int): Boolean {
        return x < pixels.width && x >= 0 && y < pixels.height && y >= 0 &&
                ColorParser.getChannelFromRGBA8888(pixels.getPixel(x, y), "alpha") != 0 &&
                Math.sqrt(Math.pow((x - xActual).toDouble(), 2.0) + Math.pow((y - yActual).toDouble(), 2.0)) <= radius
    }
}
