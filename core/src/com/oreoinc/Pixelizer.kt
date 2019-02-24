package com.oreoinc

import com.badlogic.gdx.graphics.Pixmap

object Pixelizer {

    /**
     *  Scrambles the pixels so they are random, they can only vary by
     *  maxRadius, so as that decreases, the picture becomes clearer as
     *  pixels settle near their actual position
     */
    fun scramble(pixels: Pixmap, maxRadius: Int): Pixmap {
        //The offsets we will use to shift the pixel rows/cols
        var xOffset: Int
        var yOffset: Int

        //Empty pixmaps to store each step
        val pixmap = Pixmap(pixels.width, pixels.height, Pixmap.Format.RGBA8888)
        val pixmap2 = Pixmap(pixels.width, pixels.height, Pixmap.Format.RGBA8888)

        //Shift our original picture pixel rows
        for(i in 0 until pixels.width) {
            for(j in 0 until pixels.height) {
                xOffset = (Math.random() * maxRadius - (maxRadius / 2)).toInt()
                pixmap.drawPixel(i, j, pixels.getPixel(i + xOffset, j))
            }
        }

        //Shift the above picture pixel columns
        for(i in 0 until pixels.width) {
            for(j in 0 until pixels.height) {
                yOffset = (Math.random() * maxRadius - (maxRadius / 2)).toInt()
                pixmap2.drawPixel(i, j, pixmap.getPixel(i, j + yOffset))
            }
        }

        //The double scrambled picture
        return pixmap2
    }
}
