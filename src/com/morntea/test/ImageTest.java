package com.morntea.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedImage imd = ImageIO.read(new File("1.jpg"));
			System.out.println(imd.getWidth() + "," + imd.getHeight());
			BufferedImage[] newim = new BufferedImage[4];
			int w = imd.getWidth()/4;
			int h = imd.getHeight();
			newim[0] = imd.getSubimage(0, 0, w, h);
			newim[1] = imd.getSubimage(1*w, 0, w, h);
			newim[2] = imd.getSubimage(2*w, 0, w, h);
			newim[3] = imd.getSubimage(3*w, 0, w, h);
			for(int i=0; i<4; i++) {
				ImageIO.write(newim[i], "JPEG", new File("1-"+i+".jpg"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
