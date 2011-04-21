package com.morntea.ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

public class ImageRecognition {
	final static int WHITE = Color.white.getRGB();
	final static int BLACK = Color.black.getRGB();
	final static int GRAY  = Color.gray.getRGB();
	final static int THRESHOLD = new Color(180, 180, 180).getRGB();
	
	PixelImage image = null;
	
	public ImageRecognition(String name) throws IOException{
		image = new PixelImage(name);
	}
	
	public void segment(String path){
		int[] pixels = image.pixels;
		int width = image.width;
		int height = image.height;
		
		int up, down, left, right;
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean[] visited = new boolean[pixels.length];
		
		int head = height / 2 * width, end = head + width;
		int index = 0;
		while(true){
			//寻找第一个黑点。
			while(head < end && pixels[head] > THRESHOLD ){
				++head;
			}
			if(head == end){
				break;
			}
			queue.clear();
			Arrays.fill(visited, false);
			
			visited[head] = true;
			pixels[head] = WHITE;
			queue.add(head);
			left = right = head % width;
			up = down = head / width;
			
			while(!queue.isEmpty()){
				int v = queue.poll();
				int k;
				//检查该点左边。
				if(v % width != 0){
					//未访问且为黑点。
					k = v-1;
					if(pixels[k] < THRESHOLD){
						visited[k] = true;
						queue.add(k);
						pixels[k] = WHITE;
						left = Math.min(left, k % width);
					}
				}
				//检查该点上边。
				if(v >= width){
					k = v - width;
					if(pixels[k] < THRESHOLD){
						visited[k] = true;
						queue.add(k);
						pixels[k] = WHITE;
						up = Math.min(up, k / width);
					}
				}
				//检查该点右边。
				k = v + 1;
				if(k % width != 0){
					if(pixels[k] < THRESHOLD){
						visited[k] = true;
						queue.add(k);
						pixels[k] = WHITE;
						right = Math.max(right, k % width);
					}
				}
				//检查该点下边。
				k = v + width;
				if(k < pixels.length){
					if(pixels[k] < THRESHOLD){
						visited[k] = true;
						queue.add(k);
						pixels[k] = WHITE;
						down = Math.max(down, k / width);
					}
				}
			}
			
			int w = right - left + 1;
			int h = down - up + 1;
			if(w < 10 || h < 10){
				continue;
			}
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			int upLeft = up * width + left;
			for(int i = 0; i < h; ++i){
				int cur = upLeft + i * width;
				for(int j = 0; j < w; ++j, ++cur){
					if(visited[cur]){
						bi.setRGB(j, i, BLACK);
					}else{
						bi.setRGB(j, i, WHITE);
					}
				}
			}
			try {
				ImageIO.write(bi, "JPG", new File(path+"part"+index+".jpg"));
				index++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Segmentation finished!!");
	}
	
	public static void main(String[] args) {
		try {
			for(int i = 1; i < 10; ++i){
				ImageRecognition ir = new ImageRecognition("I:/Image/checkcode"+i+".jpg");
				ir.segment("I:/Image/pic"+i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
