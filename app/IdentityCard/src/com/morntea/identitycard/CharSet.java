package com.morntea.identitycard;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class CharSet {
    public static String[] charsets = {"ISO-8859-1", "Unicode", "GB2312", "GBK", "UTF-8", "UTF-16", "CP1252"};
    static{
        System.out.println("JVM Default Charset: " + Charset.defaultCharset());
        System.out.println("System Charset: "
                + System.getProperty("file.encoding"));
    }
    public static void main(String[] args) {
        String word = "中国";
        /*Scanner scanner = new Scanner(System.in, "gb2312");
        word = scanner.nextLine();*/
        
        /*BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
        try {
            word=br1.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        /*try {
            char ch = (char)System.in.read();
            System.out.println((int)ch + "," + ch);
            byte btArray[]=new byte[9];
            System.in.read(btArray);
            print(btArray);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        byte[] bytes = word.getBytes();
        System.out.println(word.codePointAt(0));
        print(bytes);
        testAllCharset(word);
        testCharset(word);
    }
    
    public static void testAllCharset(String word) {
        System.out.println(word + " decoding .....................................");
        for(String charset : charsets) {
            printCharset(word, charset);
        }
        System.out.println(word + " decoding done.................................");
    }
    
    public static void testCharset(String word) {
        for(String charset : charsets) {
            System.out.print(charset + ":    \t");
            try {
                String newWord = new String(word.getBytes(charset), "ISO-8859-1");
                System.out.print(word + " => ("+charset +")"+ newWord + "(ISO-8859-1) => ");
                newWord = new String(newWord.getBytes("ISO-8859-1"), charset);
                System.out.println(newWord);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void printCharset(String word, String charset) {
        byte[] bytes = null;
        try {
            bytes = word.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.print(charset + ":    \t");
        print(bytes);
    }

    public static void print(byte[] bytes) {
        String out = "", outHex = "", outBin = "";
        for(byte b : bytes) {
            out += (b + "\t\t");
            outHex += "0x" + toHexString(b) + " ";
            outBin += Integer.toBinaryString(b).toUpperCase() + " ";
        }
        //System.out.println(out);
        System.out.println(outHex);
        //System.out.println(outBin);
    }
    
    public static String toHexString(byte value) {  
        String tmp = Integer.toHexString(value & 0xFF);  
        if (tmp.length() == 1) {  
            tmp = "0" + tmp;  
        }  
      
        return tmp.toUpperCase();  
    }
    
}
