package com.morntea.identitycard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    static{
        System.out.println("JVM Default Charset: " + Charset.defaultCharset());
        System.out.println("System Charset: "
                + System.getProperty("file.encoding"));
    }
    
    public static void main(String[] args) {
        translate3();
    }
    
    public static Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
    
    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            /*CharSet.testAllCharset("中国");
            CharSet.testAllCharset(input);*/
            input = new String(input.getBytes("CP1252"), "UTF-8");
            System.out.println(input);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }
    
    public static void translate3() {
        try {
            FileReader file = new FileReader("code5.csv");
            BufferedReader fileReader = new BufferedReader(file);  
            String line;
            while((line = fileReader.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=5) {
                    System.err.println(line);
                    return;
                }
                String name = strArr[2];
                String pinyin = strArr[1].toLowerCase();
                String newPinyin = ChineseWord.pinyin(name);
                
                if(newPinyin.equals("shixiaqu")) {
                    if(pinyin.indexOf("shixiaqu")==-1) {
                        System.out.println(line);
                    }
                } else {
                    pinyin = pinyin.replaceAll("[`\\s']+", "");
                    if(pinyin.equals(newPinyin) || (pinyin+"xian").equals(newPinyin)){
                        
                    } else {
                        System.out.println(line);
                    }
                }
            }
            fileReader.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void translate2() {
        HashMap<String, String> codeMap = new HashMap<String, String>();
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("code6.csv"), "utf-8");
            FileReader file = new FileReader("code5.csv");
            BufferedReader fileReader = new BufferedReader(file);            

            FileReader fileAll = new FileReader("all.csv");
            BufferedReader fileReaderAll = new BufferedReader(fileAll);
            
            String line;
            while((line = fileReaderAll.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=2) {
                    System.err.println(line);
                    return;
                }
                codeMap.put(strArr[0], strArr[1]);
            }
            
            while((line = fileReader.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=5) {
                    System.err.println(line);
                    return;
                }
                String name = strArr[2];
                if(codeMap.containsKey(strArr[0])) {
                    String nameInAll = codeMap.get(strArr[0]);
                    if(!name.equals(nameInAll)) {
                        System.out.println(line + " => " + nameInAll);
                    }
                }
            }
            
            fileReader.close();
            file.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void translate1() {
        HashMap<String, String> codeMap = new HashMap<String, String>();
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("code6.csv"), "utf-8");
            FileReader file = new FileReader("code5.csv");
            BufferedReader fileReader = new BufferedReader(file);
            
            FileReader file82 = new FileReader("1982.csv");
            BufferedReader fileReader82 = new BufferedReader(file82);
            
            FileReader file95 = new FileReader("1995.csv");
            BufferedReader fileReader95 = new BufferedReader(file95);
            
            FileReader fileUnknown = new FileReader("unknown.csv");
            BufferedReader fileReaderUnknown = new BufferedReader(fileUnknown);
            
            String line;
            while((line = fileReader82.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=2) {
                    System.err.println(line);
                    return;
                }
                codeMap.put(strArr[0], strArr[1]);
            }

            while((line = fileReader95.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=3) {
                    System.err.println(line);
                    return;
                }
                //System.out.println(strArr[2]+","+ strArr[0]);
                //codeMap.put(strArr[2], strArr[0]);
            }
            
            while((line = fileReaderUnknown.readLine())!=null){
                String strArr[] = line.split(",");
                if(strArr.length!=2) {
                    System.err.println(line);
                    return;
                }
                String areaStr = strArr[1].replace("(", "").replace(")", "");
                String sArr[] = areaStr.split("--");
                if(sArr.length==2) {
                    areaStr = sArr[1];
                }
                //System.out.println(strArr[0]+","+ areaStr);
                //codeMap.put(strArr[0], areaStr);
            }
            
            int lineNo = 0;
            while((line = fileReader.readLine())!=null){
                lineNo++;
                String strArr[] = line.split(",");
                String pinyin = strArr[1].replaceAll("[`\\s']+", "").toLowerCase();
                if(strArr.length!=5) {
                    System.err.println(line);
                    return;
                }
                boolean write = false;
                if(strArr[2].isEmpty()) {
                    System.out.println(lineNo + ". " + line);
                    if(codeMap.containsKey(strArr[0])) {
                        String newName = codeMap.get(strArr[0]);
                        String newPinyin = ChineseWord.pinyin(newName);
                        System.out.println(pinyin + " == " + newPinyin);
                        if(pinyin.equals(newPinyin) || (pinyin+"xian").equals(newPinyin)) {
                            System.out.println(newName);
                        } else {
                            System.out.println(newName + "?");
                            String input = getInput();
                            if(!input.isEmpty()) {
                                newName = input;
                            }
                        }
                        write = true;
                        out.write(strArr[0]+","+strArr[1]+","+newName+","+strArr[3]+","+strArr[4]+"\r\n");
                    }
                }
                if(!write){
                    out.write(line+"\r\n");
                }
            }
            
            fileReader.close();
            file.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void translate() {
        HashMap<String, String> codeMap = getAllCodeMap();
        //System.out.println(codeMap.size());

        try {
            //fstream = new FileWriter("code.csv");
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("code1.csv"), "utf-8");
            FileReader file = new FileReader("out.csv");
            BufferedReader fileReader = new BufferedReader(file);
            String line;
            while((line = fileReader.readLine())!=null){
                System.out.println("--------------------------------");
                System.out.println(line);
                String strArr[] = line.split(",");
                String newName = "";
                String pinyin = strArr[1].replaceAll("[`\\s]+", "").toLowerCase();
                if(codeMap.containsKey(strArr[0])) {
                    newName = codeMap.get(strArr[0]);
                    String newPinyin = ChineseWord.pinyin(newName);
                    
                    if(newPinyin.equals(pinyin)) {
                        System.out.println(pinyin + " => " + newName);
                    }
                    //else if (pinyin.indexOf(newPinyin)!=-1 || newPinyin.indexOf(pinyin)!=-1) { } 
                    else {
                        System.out.println(pinyin + " <> " + newPinyin + "=" + newName + "? If yes, press enter");
                        //newName = getInput();
                        /*String input = getInput();
                        if(!input.isEmpty()) {
                            newName = input;
                        }*/
                        //System.err.println(newName);
                    }
                } else {
                    String area = getArea(strArr[0]);
                    if(!area.isEmpty()) {
                        String pinyinArr[] = new String[area.length()];
                        for(int i=0; i<area.length(); i++) {
                            pinyinArr[i] = ChineseWord.pinyin(area.substring(i, i+1));
                            System.out.print(pinyinArr[i]+" ");
                        }
                        for(int i=area.length()-1; i>=0; i--) {
                            if(pinyin.startsWith(pinyinArr[i])) {
                                newName = area.substring(i);
                                break;
                            }
                        }
                    }
                    /*String input = getInput();
                    if(!input.isEmpty()) {
                        newName = input;
                    }*/
                    System.out.println(" => " + newName);
                }
                if(newName==null || newName.startsWith("0")) {
                    System.err.println("No input, exit.");
                    break;
                }
                out.write(strArr[0]+","+strArr[1]+","+newName+","+strArr[2]+","+strArr[3]+"\r\n");
            }
            fileReader.close();
            file.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void formatData() {
        File data = new File("./data");
        File[] txts = data.listFiles();
        Arrays.sort(txts);

        try {
            FileWriter fstream = new FileWriter("out.csv");
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 0; i < txts.length; i++) {
                System.out.println(txts[i].getName());
                out.write(txts[i].getName().substring(0, 2) + "0000,Name,19820101,19921231\r\n");
                
                FileReader file = new FileReader(txts[i]);
                BufferedReader fileReader = new BufferedReader(file);
                String line = null;
                while((line = fileReader.readLine())!=null){
                    if(line.startsWith("="))break;
                }
                
                while((line = fileReader.readLine())!=null){
                    line = line.trim();
                    if(!line.isEmpty()) {
                        String[] strArr = regArrFetch(line, "(\\d+)\\s+(.+?)\\s+(\\d+)\\s+(\\d+)");
                        if(strArr[0].length()!=6 || strArr[1].trim().isEmpty()) {
                            System.out.println(strArr[0] + "," + strArr[1] + "," + strArr[2] + "," + strArr[3]);
                        } else {
                            out.write(strArr[0] + "," + strArr[1] + "," + strArr[2] + "," + strArr[3] + "\r\n");
                        }
                    }
                }
                fileReader.close();
                file.close();
            }
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
    }
    
    public static String[] regArrFetch(String src, String regex) {
        String[] value = {};
        Pattern pattern = Pattern.compile(regex,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            int size = matcher.groupCount();
            value = new String[size];
            for(int i=0; i<size; i++){
                value[i] = matcher.group(i+1);
            }
        }
        return value;
    }
    
    public static HashMap<String, String> getAllCodeMap() {
        AreaCode codes[] = AreaCode.getCodes();
        HashMap<String, String> map, codeMap = codes[codes.length-1].codeMap;;
        for(int i=codes.length-2; i>=0; i--) {
            map = codes[i].codeMap;
            for(String key : map.keySet()) {
                if(!codeMap.containsKey(key)) {
                    codeMap.put(key, map.get(key));
                }
            }
        }
        return codeMap;
    }
    
    public static void genJs() {

        AreaCode codes[] = AreaCode.getCodes();
        HashMap<String, String> olderMap, codeMap, oldMap = new HashMap<String, String>();
        codeMap = codes[codes.length-1].codeMap;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[0].date) + ": " + codes[0].codeMap.size());
        for(int i=codes.length-2; i>=0; i--) {
            olderMap = codes[i].codeMap;
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[i].date) + ": " + codes[i].codeMap.size());
            for(String key : olderMap.keySet()) {
                if(!codeMap.containsKey(key)) {
                    if(!oldMap.containsKey(key)) {
                        oldMap.put(key, olderMap.get(key));
                    }
                }
            }
        }

        
        boolean first = true;
        String codejs = "var codes = [";
        String names = "var names = [";
        String oldCodes = "var oldCodes = [";
        String oldNames = "var oldNames = [";
        
        Object keys[] = codeMap.keySet().toArray();
        Arrays.sort(keys);
        for(Object key : keys) {
            if(first) {
                codejs += key;
                names += "\"" + codeMap.get(key) + "\"";
                first = false;
            } else {
                codejs += "," + key;
                names += ", \"" + codeMap.get(key) + "\"";
            }
            //System.out.println(key + ": " + oldMap.get(key));
        }
        
        keys = oldMap.keySet().toArray();
        Arrays.sort(keys);
        
        first = true;
        for(Object key : keys) {
            if(first) {
                oldCodes += key;
                oldNames += "\"" + oldMap.get(key) + "\"";
                first = false;
            } else {
                oldCodes += "," + key;
                oldNames += ", \"" + oldMap.get(key) + "\"";
            }
            //System.out.println(key + ": " + codeMap.get(key));
        }

        codejs += "];";
        oldCodes += "];";
        names += "];";
        oldNames += "];";
        System.out.println(codejs);
        System.out.println(oldCodes);
        System.out.println(names);
        System.out.println(oldNames);
    }
    
    public static void mapChange() {
           AreaCode codes[] = AreaCode.getCodes();
            HashMap<String, String> olderMap, newerMap;
            newerMap = olderMap = codes[codes.length-1].codeMap;
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[0].date) + ": " + codes[0].codeMap.size());
            for(int i=codes.length-2; i>0; i--) {
                olderMap = codes[i].codeMap;
                System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[i].date) + ": " + codes[i].codeMap.size());
                for(String key : olderMap.keySet()) {
                    if(!olderMap.get(key).equals(newerMap.get(key)) && newerMap.get(key)!=null) {
                        System.out.println(key + ":" + olderMap.get(key) + " <= " + newerMap.get(key));
                    }
                }
                newerMap = olderMap;
            }
    }

    public static String getArea(String areaId) {
        String area = "";
        String html = getHtml("translate.jsp?id="+areaId+"195410030268", "utf-8");
        
        int s = html.indexOf("<td width=\"253\"><h2>")+20;
        if(s!=19) {
            int e = html.indexOf("</h2>", s);
            area = html.substring(s, e);
            System.out.println(area);
        }
        return area;
    }
    public static String getHtml(String url, String encoding) {
        URL urlObj = null;
        String temp = null;
        StringBuffer sb = new StringBuffer();
        try {
            urlObj = new URL(url);
            
            HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();   
            httpConn.setRequestMethod("GET");   
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");  
            
            InputStream is = httpConn.getInputStream();
            //InputStream is = urlObj.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, encoding));
            while ((temp = in.readLine()) != null) {
                sb.append(temp + "\n");
            }
            in.close();
        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
        return sb.toString();
    }
}
