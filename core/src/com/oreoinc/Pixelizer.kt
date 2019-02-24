package com.oreoinc

import com.badlogic.gdx.graphics.Pixmap

object Pixelizer {
    fun scramble(pixels: Pixmap, maxRadius: Int): Pixmap {
        var xOffset: Int
        var yOffset: Int
        val pixmap = Pixmap(pixels.width, pixels.height, Pixmap.Format.RGBA8888)
        val pixmap2 = Pixmap(pixels.width, pixels.height, Pixmap.Format.RGBA8888)
        for(i in 0 until pixels.width) {
            for(j in 0 until pixels.height) {
                xOffset = (Math.random() * maxRadius - (maxRadius / 2)).toInt()
                pixmap.drawPixel(i, j, pixels.getPixel(i + xOffset, j))
            }
        }
        for(i in 0 until pixels.width) {
            for(j in 0 until pixels.height) {
                yOffset = (Math.random() * maxRadius - (maxRadius / 2)).toInt()
                pixmap2.drawPixel(i, j, pixmap.getPixel(i, j + yOffset))
            }
        }
        return pixmap2
    }
}
