package com.morntea.identitycard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class ChineseWord {
    public static String loadWords(boolean indexed) {
        String words = null;
        try {
            words = new BufferedReader(new FileReader(new File(
                    indexed ? "./hanzi_index.bin" : "./hanzi.bin"))).readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static String hanziToPinyin(String words) {
        String pinyin = "";
        String wordArr[] = loadWords(true).split(",");
        for (int i = 0; i < words.length(); i++) {
            char word = words.charAt(i);
            if (word < 19968 || word > 40869) {
                pinyin += word;
            } else {
                pinyin += wordArr[word - 19968].substring(1);
            }
        }
        return pinyin;
    }

    public static String pinyin(String words) {
        String pinyin = hanziToPinyin(words);
        String yuanyin = "āáǎàōóǒòēéěèīíǐìūúǔùǖǘǚǜ";
        String alpha = "aaaaooooeeeeiiiiuuuuvvvv";
        for (int i = 0; i < yuanyin.length(); i++) {
            pinyin = pinyin.replace(yuanyin.charAt(i), alpha.charAt(i));
        }
        return pinyin.replace('v', 'u');
    }

    public static void traverse() {
        String words = loadWords(false);
        System.out.println(words.length());

        String wordArr[] = words.split(",");
        System.out.println(wordArr.length);

        int low = 65536, high = 0;
        for (int i = 0; i < words.length(); i++) {
            int value = (int) words.charAt(i);
            if (value < 500)
                continue;
            if (value < low)
                low = value;
            else if (value > high)
                high = value;
        }
        System.out.println("low: " + low + ", high: " + high);
        System.out.println(high - low);

        // test
        if (low < 0)
            return;
        FileWriter fstream;
        try {
            fstream = new FileWriter("hanzi_index.bin");
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 19968; i < 40869; i++) {
                int s = words.indexOf(i);
                if (s == -1) {
                    System.out.println((char) i + "unkown,");
                    out.write((char) i + "unkown,");
                } else {
                    int e = words.indexOf(",", s);
                    out.write((char) i + words.substring(s + 1, e) + ",");
                    // break;
                }
            }
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /*
         * int v = '乥'; System.out.println(v); for(int i=-5; i<5; i++) {
         * System.out.println((char)(v+i)); }
         */
    }

    public static byte[] stringToBytesUTFCustom(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        for (int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            b[bpos] = (byte) ((buffer[i] & 0xFF00) >> 8);
            b[bpos + 1] = (byte) (buffer[i] & 0x00FF);
        }
        return b;
    }

    public static void main(String args[]) {
        boolean debug = true;
        
        //test();
        if(debug) {
            String w = "中国";
            CharSet.testAllCharset(w);
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            CharSet.testAllCharset(word);
            try {
                word = new String(word.getBytes("CP1252"), "UTF-8");
                System.err.println(word);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        /*String pinyin = pinyin("中");
        CharSet.testAllCharset("zhong");
        CharSet.testAllCharset(pinyin);
        System.out.println("pinyin: " + pinyin);*/
    }
    
    public static String toUTF8(String word) {
        try {
            return new String(word.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void test() {
        try {
            String txt = new BufferedReader(new FileReader(new File("./test.txt"))).readLine();
            System.out.println(txt.length());
            CharSet.testAllCharset("中a");
            CharSet.testAllCharset(txt);
            System.out.println(pinyin(txt));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
