package com.morntea.taobao;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.morntea.util.TimeHelper;
class ShapeContext {
	public ShapeContext(ArrayList<Integer> groupPos) {
		super();
		this.groupPos = groupPos;
	}
	ArrayList<Integer> groupPos;
	int minRow, maxRow, minCol, maxCol;
	int width, height;
	int pixels[];
}
public class CheckCodeUI extends Frame {
	private static final long serialVersionUID = 2271150921342175229L;

	final static int WHITE = Color.white.getRGB();
	final static int BLACK = Color.black.getRGB();
	final static int RED  = Color.red.getRGB();
	final static int BLUE  = Color.blue.getRGB();
	final static int GRAY  = Color.gray.getRGB();
	final static int threshold = 180;
	final static int THRESHOLD = new Color(threshold, threshold, threshold).getRGB();
	final static int THRESHOLDS[] = {new Color(180, 180, 180).getRGB(), new Color(160, 160, 160).getRGB(), new Color(140, 140, 140).getRGB(), 
			new Color(120, 120, 120).getRGB(), new Color(100, 100, 100).getRGB(), new Color(80, 80, 80).getRGB()};
	final static int VERY_BLACK = 35;
	final static ColorModel cm=ColorModel.getRGBdefault();
	HashMap<Integer, ShapeContext> shapeMap = null;
	int[] shapeIndexs = null;
	private Image im, sharp, tmp;
	
	private int w, h;
	private boolean flagLoad = false;
	private TimeHelper timer = new TimeHelper(); 
	private int[] pixels = null;

	public CheckCodeUI() {
		super("Check Code Quick Input & Recognition Util");
		Panel pdown;
		Button start, convertBtn, ocrBtn, restoreBtn, quit;
		final JTextField input;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		this.setBackground(Color.gray);

		start = new Button("Start");
		input = new JTextField(10);
		convertBtn = new Button("Convert");
		ocrBtn = new Button("OCR");
		restoreBtn = new Button("Restore");
		quit = new Button("Exit");

		this.add(pdown, BorderLayout.SOUTH);

		pdown.add(start);
		pdown.add(input);
		pdown.add(convertBtn);
		pdown.add(ocrBtn);
		pdown.add(restoreBtn);
		pdown.add(quit);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.grabFocus();
				loadImage();
			}
		});
		
		convertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convert();
			}
		});
		
		ocrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recognize();
			}
		});
		
		restoreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restore();
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		input.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 8764498065075299613L;

			public void insertString(int offset, String str, AttributeSet attr)
					throws BadLocationException {
				if (str == null) {
					return;
				}
				if ((getLength() + str.length()) <= 4) {
					char[] upper = str.toCharArray();
					int length = 0;
					for (int i = 0; i < upper.length; i++) {
						if ((upper[i] >= '0' && upper[i] <= '9')
								|| (upper[i] >= 'a' && upper[i] <= 'z')
								|| (upper[i] >= 'A' && upper[i] <= 'Z')) {
							upper[length++] = upper[i];
						}
					}
					super.insertString(offset, new String(upper, 0, length),
							attr);
					checkInput(input.getText());
				}
			}
		});
	}

	public void checkInput(String text) {
		if (text.length() == 4) {
			JOptionPane.showMessageDialog(this, text+","+timer.time());
		}
	}

	private Image readImage() {
		Image im = null;
		timer.start();
		boolean fromWeb = false;
		if(fromWeb) {
			try {
				String url = "http://checkcode.taobao.com/auction/checkcode?sessionID=6b15fec8f1455ddfbba4c676d6e381b4"
					+ "&" + Math.random();
				im = Toolkit
						.getDefaultToolkit()
						.getImage(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			int index = (int)(Math.random()*1000);
			String fileName = "checkcode\\"+index+".jpg";
			//975.jpg, 840.jpg, 6.jpg, 273.jpg
			//fileName = "checkcode\\6.jpg";
			fileName = "2.jpg";
			//fileName = "checkcode\\291.jpg";
			//fileName = "checkcode\\208.jpg";
			//fileName = "checkcode\\332.jpg";
			//fileName = "checkcode\\376.jpg";
			//fileName = "checkcode\\687.jpg";
			//fileName = "checkcode\\172.jpg"; //LVUZ hard
			//fileName = "checkcode\\627.jpg";
			//fileName = "checkcode\\211.jpg";
			//fileName = "checkcode\\862.jpg";
			//fileName = "checkcode\\105.jpg";
			//fileName = "template\\C.png";
			System.out.println(fileName);
			im = Toolkit.getDefaultToolkit().getImage(fileName);
		}
		System.out.println("Time To Get Image:" + timer.time() + "ms");
		return im;
	}
	
	private void loadImage() {
		MediaTracker tracker = new MediaTracker(this);
		im = readImage();
		if(im==null)return;
		tracker.addImage(im, 0);

		timer.start();
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		System.out.println("Image Loading Time:" + timer.time() + "ms");

		w = im.getWidth(this);
		h = im.getHeight(this);
		System.out.println("Image Metrix: " + w + " x " + h);
		
		tmp = im;
		flagLoad = true;
		timer.start();
		repaint();
	}
	
	private void convert() {
		if(im==null)return;
		pixels=new int[w*h];
		
		try{
			PixelGrabber pg=new PixelGrabber(im,0,0,w,h,pixels,0,w);
			pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		//paint edges to white, so no need to detect bounds
		int color = WHITE;//RED
		for(int i=0; i<w; i++){
			pixels[i] = color;
			pixels[w*(h-1)+i] = color;
		}
		for(int i=w-1; i<w*(h-1); i+=w){
			pixels[i] = color;
			pixels[1+i] = color;
		}
		
		tripImage(THRESHOLDS[0]);
		ImageProducer ip = new MemoryImageSource(w,h,pixels,0,w);
		sharp = createImage(ip);
		tmp = sharp;
		flagLoad = true;
		repaint();
	}
	
	private void tripImage(int ths) {
		if(pixels==null)return;

		shapeIndexs = new int[w*h];
		Arrays.fill(shapeIndexs, WHITE);
		shapeMap = new HashMap<Integer, ShapeContext>();
		int index = 0;
		for(int y=1; y<h-1; y++){
			String line = "";
			for(int x=1;x<w-1;x++){
				int p = y*w+x;
				int pix = pixels[p];
				int r = cm.getRed(pix);
				line += (pix<ths?r:" ")+" ";
				if(pix>ths){
					pixels[p] = WHITE;
				} else {
					index = getShape(x, y, shapeIndexs, shapeMap, index);
				}
			}
			System.out.println(line);
		}
	}
	
	boolean isValidShapes() {
		int size = 0;
		boolean debug = true;
		boolean valid = false;
		int fragNum = shapeMap.size();
		
		SortedMap<Integer, ShapeContext> sm = new TreeMap<Integer, ShapeContext>();
		for(int key:shapeMap.keySet()) {
			ShapeContext sc = shapeMap.get(key);
			sc.width = sc.maxCol - sc.minCol + 1;
			sc.height = sc.maxRow - sc.minRow + 1;
			sm.put(sc.minCol, sc);
		}
		ShapeContext[] scs = new ShapeContext[fragNum];
		int i = 0;
		for(int sn:sm.keySet()) {
			scs[i++] = sm.get(sn);
		}
		
		for(i=0; i<fragNum; i++) {
			ShapeContext sc = scs[i];
			if(sc.width<15 && sc.height<25) {
				if(debug)System.out.println("Too small ["+sc.width+"x"+sc.height+"]");
				ShapeContext scRevise = null;
				if(i==0) {
					scRevise = scs[i+1];
				} else if (i==fragNum-1) {
					scRevise = scs[i-1];
				} else {
					if (scs[i+1].width<scs[i-1].width) {
						scRevise = scs[i+1];
					} else {
						scRevise = scs[i-1];
					}
				}
				if(scRevise!=null) {
					scRevise.minCol = Math.min(sc.minCol, scRevise.minCol);
					scRevise.minRow = Math.min(sc.minRow, scRevise.minRow);
					scRevise.maxCol = Math.max(sc.maxCol, scRevise.maxCol);
					scRevise.maxRow = Math.max(sc.maxRow, scRevise.maxRow);
					scRevise.width = scRevise.maxCol - scRevise.minCol + 1;
					scRevise.height = scRevise.maxRow - scRevise.minRow + 1;
					scRevise.groupPos.addAll(sc.groupPos);
					if(scRevise==scs[i-1])
						pixelMap(scs[i-1], size-1);
				} else {
					System.err.println("Fatal Error.");
				}
				//return false;
			} else {
				size++;
				pixelMap(sc, size);
			}
			//drawImage(sc.width, sc.height, charArr);
		}// for every index
		if(size==4) {
			valid = true;
		}
		return valid;
	}
	
	void pixelMap(ShapeContext sc, int order) {
		int charArr[] = new int[sc.width*sc.height];
		Arrays.fill(charArr, WHITE);
		int upLeft = sc.minCol+sc.minRow*w;
		boolean debug = false;
		if(debug )System.out.println("width:"+sc.width+"["+sc.minCol+"-"+sc.maxCol
				+"], height:"+sc.height+"["+sc.minRow+"-"+sc.maxRow+"]");
		int k=1;
		for(int pos:sc.groupPos) {
			int t = pos-upLeft;
			int l = t/w;
			int m = t%w;
			int newPos = l*sc.width + m;
			charArr[newPos] = pixels[pos];
			k++;
		} // for every pixel
		sc.pixels = charArr;
		saveImage(sc.width, sc.height, sc.pixels, "output-"+order+".jpg");
	}
	
	int getShape(int x, int y, int[] shapeIndexs, HashMap<Integer, ShapeContext> shapeMap, int index) {
		int p = y*w+x;
		int up, left, upIndex, leftIndex;
		up = p-w;
		left= p-1;
		upIndex = shapeIndexs[up];
		leftIndex = shapeIndexs[left];
		if(pixels[up]==WHITE && pixels[left]==WHITE) {
			index++;
			shapeIndexs[p] = index;
			ArrayList<Integer> groupPos = new ArrayList<Integer>();
			groupPos.add(p);
			ShapeContext sc = new ShapeContext(groupPos);
			sc.minRow = sc.maxRow = y;
			sc.minCol = sc.maxCol = x;
			shapeMap.put(index, sc);
		} else if (pixels[up]!=WHITE && pixels[left]==WHITE) {
			ShapeContext sc = shapeMap.get(upIndex);
			sc.maxRow = y;
			sc.groupPos.add(p);
			shapeIndexs[p] = upIndex;
		} else if (pixels[up]==WHITE && pixels[left]!=WHITE) {
			ShapeContext sc = shapeMap.get(leftIndex);
			sc.groupPos.add(p);
			if(sc.maxCol < x)sc.maxCol = x;
			shapeIndexs[p] = leftIndex;
		} else { // conflict
			ShapeContext sc = shapeMap.get(upIndex);
			if(sc==null){
				System.out.println("null");
			}
			ArrayList<Integer> groupPos = sc.groupPos;
			if(upIndex != leftIndex){
				ShapeContext scLeft = shapeMap.get(leftIndex);
				if(scLeft.minCol<sc.minCol)sc.minCol = scLeft.minCol;
				if(scLeft.minRow<sc.minRow)sc.minRow = scLeft.minRow;
				if(scLeft.maxCol>sc.maxCol)sc.maxCol = scLeft.maxCol;
				ArrayList<Integer> leftPos = scLeft.groupPos;
				groupPos.addAll(leftPos);
				for(int pos : leftPos) {
					shapeIndexs[pos] = upIndex;
				}
				shapeMap.remove(leftIndex);
			}
			sc.maxRow = y;
			groupPos.add(p);
			shapeIndexs[p] = upIndex;
		}
		return index;
	}
	
	private void recognize(){
		if(sharp==null)return;
		if(isValidShapes())return;
		int tryTimes = 0;
		while(tryTimes++<5) {
			split();
			if(isValidShapes()) {
				System.out.println("1. Split " + tryTimes + " Times");
				return;
			}
		}
		System.out.println("1. Can't trip this image.");
		tryTimes = 0;
		for(int ths:THRESHOLDS) {
			tripImage(ths);
			tryTimes++;
			if(isValidShapes()) {
				System.out.println("2. Use Threshold " + tryTimes + " Times");
				return;
			}
		}
		System.out.println("2. Can't trip this image.");
	}
	
	private void split() {
		if(sharp==null)return;
		int wh = w*h;
		int[] recPixs=new int[wh];
		Arrays.fill(recPixs, -1);
		shapeIndexs = new int[w*h];
		Arrays.fill(shapeIndexs, WHITE);
		shapeMap = new HashMap<Integer, ShapeContext>();
		int index = 0;
		recPixs = Arrays.copyOf(pixels, wh);
		
		o:for(int y=1; y<h-1; y++) {
			for(int x=1; x<w-1; x++) {
				int p = w*y+x;
				if(p==4104){
					int d=0;d++;
//					pixels[p]=RED;
					if(d==0)break o;
				}
				int centerGrey = cm.getRed(recPixs[p]);
				if(recPixs[p]==WHITE)continue;
				if(centerGrey<VERY_BLACK){
					index = getShape(x, y, shapeIndexs, shapeMap, index);
					continue;
				}
				int a[] = new int[8];
				a[0] = p-w-1;	a[1] = p-w;		a[2] = p-w+1;
				a[3] = p-1;						a[4] = p+1;
				a[5] = p+w-1;	a[6] = p+w;		a[7] = p+w+1;
				int avg = 0;
				int total = 0;
				int whitest = 0;
				for(int k=0; k<8; k++){
					if(recPixs[a[k]]!=WHITE){
						int tmpGrey = cm.getRed(recPixs[a[k]]);
						avg+= tmpGrey;//cm.getRed(pixels[a[k]]);
						if(tmpGrey>whitest)whitest = tmpGrey;
						total++;
					}
				}
				if(total==0) {
					pixels[p]=WHITE;
					continue;
				}
				avg /= total;
				int diff = centerGrey-avg;
				if(diff > 15) { // whiter than average
					boolean canDetele = false;
					/*   @
					 * @ x @ Joint
					 *   @
					 *   */ 
					if(isCornerJoint(p, a[0], a[1], a[3], pixels)){
//						if(getNbCount(a[1], pixels)>4 && getNbCount(a[3], pixels)>4){
//							canDetele = true;
//						}
					} else if(isCornerJoint(p, a[2], a[1], a[4], pixels)){
					} else if(isCornerJoint(p, a[5], a[3], a[6], pixels)){
					} else if(isCornerJoint(p, a[7], a[4], a[6], pixels)){
					} else { //@*@
						if(pixels[a[3]]!=WHITE && pixels[a[4]]!=WHITE
						&& pixels[a[1]]==WHITE && pixels[a[6]]==WHITE){
							if(!isConjunction(p, a[3], a[4], pixels)) {
								canDetele = true;
							}
						} else if (pixels[a[3]]==WHITE && pixels[a[4]]==WHITE
								&& pixels[a[1]]!=WHITE && pixels[a[6]]!=WHITE){
							if(!isConjunction(p, a[1], a[6], pixels)) {
								canDetele = true;
							}
						} else {
							canDetele = true;
						}
					}
					if(canDetele) {
						pixels[p]=WHITE;
					} else {
						index = getShape(x, y, shapeIndexs, shapeMap, index);
					}
				} else {
					if(pixels[a[1]]==WHITE) {
						if(pixels[a[0]]!=WHITE && pixels[a[3]]==WHITE && pixels[a[3]-1]==WHITE){
							//pixels[a[3]]=GRAY;
							int rgb = (cm.getRed(pixels[a[0]])+centerGrey)>>1;
							pixels[a[3]] = new Color(rgb, rgb, rgb).getRGB();
							index = getShape(x-1, y, shapeIndexs, shapeMap, index);
						}
						index = getShape(x, y, shapeIndexs, shapeMap, index);
						if(pixels[a[2]]!=WHITE && pixels[a[4]]==WHITE){
							//pixels[a[4]]=GRAY;
							int rgb = (cm.getRed(pixels[a[2]])+centerGrey)>>1;
							pixels[a[4]] = new Color(rgb, rgb, rgb).getRGB();
							index = getShape(x+1, y, shapeIndexs, shapeMap, index);
						}
					} else {
						index = getShape(x, y, shapeIndexs, shapeMap, index);
					}
				}
			}
		}
		drawImage();
	}

	private boolean isCornerJoint(int center, int p0, int p1, int p2, int[] pixs) {
		boolean rt = false;
		if(pixs[p0]==WHITE && pixs[p1]!=WHITE && pixs[p2]!=WHITE) {
			int grey1 = getGrey(pixs[p1]);
			int grey2 = getGrey(pixs[p2]);
//			int centerGrey = getGrey(pixs[center]);
//			int min = Math.min(grey1, grey2);
			if(grey1<80 && grey2<80
//					&& (centerGrey-min)<50
					) { // Two ends are both very black
				rt = true;
			} else 
			{ // If the two ends can neither be ignored
				if(!canIgnore(center, p1, pixels) 
						|| !canIgnore(center, p2, pixels)) {
					rt = true;
				}
			}
		}
		return rt;
	}
	
	/**
	 * @ * @ @ @ @ @ @
	 * */
	private boolean isConjunction(int center, int p1, int p2, int[] pixs) {
		boolean rt = false;
//		int grey1 = getGrey(pixs[p1]);
//		int grey2 = getGrey(pixs[p2]);
//		if(grey1<=VERY_BLACK && grey2<=VERY_BLACK) {
//			//rt = true;
//		} else 
//		{
//			if(isLongLineJoint(center, p1, pixels) 
//					|| isLongLineJoint(center, p2, pixels)) {
//				rt = true;
//			}
//		}
		if(!canIgnore(center, p1, pixels) || !canIgnore(center, p2, pixels)){
			rt = true;
		}
		return rt;
	}
	private boolean canIgnore(int pre, int p, int[] pixs) {
		boolean rt = false;
		boolean debug = false;
		int jointGrey = cm.getRed(pixs[pre]);
		int avg = 0;
		if(debug)System.out.print("["+pre + "," + jointGrey+"]["+p + "," + cm.getRed(pixs[p])+"]");
		int a[] = new int[4];
		int len = 0;
		while(len<5){
			a[0] = p-w;
			a[1] = p-1;
			a[2] = p+1;
			a[3] = p+w;
			if(p<w)a[0]=0;
			int nbCount = 1;
			int last = pre, nextGrey = 0;
			for(int k=0; k<4; k++){
				if(a[k]!=last&&pixs[a[k]]!=WHITE){
					nbCount++;
					len++;
					nextGrey = cm.getRed(pixs[a[k]]);
					avg += nextGrey;
					if(debug)System.out.print("["+a[k] + "," + nextGrey+"]");
					pre = p;
					p = a[k];
				}
			}
			//if has more than one neighbor
			if(nbCount>2 || nbCount==1) {
				break;
			}
		}
		if(debug)System.out.println();
		if(len>0) {
			avg = avg/len;
			if(jointGrey>avg+30){ // one side max differentiate
				rt = true;
			}
		}
		return rt;
	}
	/*private int getNbCount(int p, int[] pixs) { //neighbor count
		int n = 0;
		int nb[] = getNeighbor(p, pixs);
		for(int k=0; k<8; k++){
			if(pixs[nb[k]]!=WHITE){
				n++;
			}
		}
		return n;
	}

	private int[] getNeighbor(int p, int[] pixs) {
		int a[] = new int[8];
		a[0] = p-w-1;
		a[1] = p-w;
		a[2] = p-w+1;
		a[3] = p-1;
		a[4] = p+1;
		a[5] = p+w-1;
		a[6] = p+w;
		a[7] = p+w+1;
		if(p<w){
			a[0] = a[1] = a[2] = 0;
		}
		if(p>w*(h-1)){
			a[5] = a[6] = a[7] = 0;
		}
		return a;
	}*/

	private void restore() {
		if(im!=null){
			tmp = im;
			repaint();
		}
	}
	
	//depreciated
	public boolean isLongLineJoint(int pre, int p, int[] pixs){
		boolean rt = false;
		boolean debug = false;
		int jointGrey = cm.getRed(pixs[pre]);
		int avg = jointGrey+cm.getRed(pixs[p]);
		if(debug)System.out.print("["+pre + "," + jointGrey+"]["+p + "," + cm.getRed(pixs[p])+"]");
		int a[] = new int[4];
		int len = 1;
		out:while(len++<5){
			a[0] = p-w;
			a[1] = p-1;
			a[2] = p+1;
			a[3] = p+w;
			boolean hasSibling = false;
			int last = pre, nextGrey = 0;
			for(int k=0; k<4; k++){
				if(a[k]!=last&&pixs[a[k]]!=WHITE){
					if(hasSibling){ //if has more than one neighbors
						break out;
					}
					hasSibling = true;
					nextGrey = cm.getRed(pixs[a[k]]);
					if(debug)System.out.print("["+a[k] + "," + nextGrey+"]");
					pre = p;
					p = a[k];
				}
			}
			//if has one neighbor
			if(hasSibling) {
				avg += nextGrey;
			} else {
				break;
			}
		}
		if(debug)System.out.println();
		if(len>3){
			avg = avg/len;
			if(jointGrey<avg+25) {// if blacker than avg+10
				rt=true;
			}
			if(debug)System.out.println(len + ",Current Grey:" +jointGrey+", Avg:" + avg);
		}
		return rt;
	}

	public void saveImage(int width, int height, int pixs[], String fileName) {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bi.getGraphics();
		ImageProducer ip = new MemoryImageSource(width,height,pixs,0,width);	
		Image image = createImage(ip);	
		try {
		    g.drawImage(image, 0, 0, null);
		    ImageIO.write(bi,"jpg",new File(fileName));
		} catch (IOException e) {
		    e.printStackTrace();
		} 
	}
	
	public void drawImage() {
		drawImage(w, h, pixels);
	}
	public void drawImage(int iw, int ih, int pixs[]) {
		ImageProducer ip = new MemoryImageSource(iw,ih,pixs,0,iw);	
		tmp = createImage(ip);		
		flagLoad = true;
		repaint();
	}
	
	public void paint(Graphics g) {
		int x = 50; //-250
		int y = 50;
		int zoom = 6; //10
		if (flagLoad) {
			g.drawImage(tmp, x, y, tmp.getWidth(null) * zoom,
					tmp.getHeight(null) * zoom, this);
			//System.out.println("Repaint image:" + timer.time() + "ms");
			timer.start();
		}
	}
	
	public static int getGrey(int rgb) {
		return cm.getRed(rgb);
	}
	public static void main(String[] args) {
		CheckCodeUI ccu = new CheckCodeUI();
		ccu.setLocation(50, 50);
		ccu.setSize(1000, 350);
		ccu.setVisible(true);
	}
}