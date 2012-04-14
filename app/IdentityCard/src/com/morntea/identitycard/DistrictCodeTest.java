package com.morntea.identitycard;

import java.text.SimpleDateFormat;
import java.util.HashMap;


public class DistrictCodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String district = DistrictCode.getDistrict("321183", null);
		System.out.println(district);
		
		district = DistrictCode.getDistrict("140227", null);
		System.out.println(district);
		
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
        System.out.println(oldMap.size());
        for(String key : oldMap.keySet()) {
            System.out.println(key + ": " + oldMap.get(key));
        }
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
	                if(!olderMap.get(key).equals(newerMap.get(key))) {
	                    System.out.println(key + ":" + olderMap.get(key) + " <= " + newerMap.get(key));
	                }
	            }
	            newerMap = olderMap;
	        }
	}

}
