package com.morntea.ocr;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CharTemplate {
	final static ColorModel cm=ColorModel.getRGBdefault();

	public void readTemp() {
		Image image = null;
		try {
			image = ImageIO.read(new File("Template/A0.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int[] pixels = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int x, y=0;
		for(int j=0; j<height; j++) {
			x = y;
			for(int i=0; i<width; i++) {
				System.out.print((pixels[x]<0?"*":" ") + " ");
				x++;
			}
			System.out.println();
			y += width;
		}
	}

	public static void main(String[] args) {
		CharTemplate ct = new CharTemplate();
		ct.readTemp();
	}

}
