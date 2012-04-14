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
		HashMap<String, String> olderMap, newerMap, oldMap = new HashMap<String, String>();
		newerMap = olderMap = codes[codes.length-1].codeMap;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[0].date) + ": " + codes[0].codeMap.size());
        for(int i=codes.length-2; i>0; i--) {
            olderMap = codes[i].codeMap;
		    System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[i].date) + ": " + codes[i].codeMap.size());
            for(String key : olderMap.keySet()) {
		        if(!newerMap.containsKey(key)) {
		            //System.out.println(codeMap.get(key));
		            oldMap.put(key, olderMap.get(key));
		        } else if(!newerMap.get(key).equals(olderMap.get(key))) {
		            System.out.println(olderMap.get(key) + " <= " + newerMap.get(key));
		        }
		    }
            newerMap = olderMap;
		    //break;
		}
        System.out.println(oldMap.size());
	}

}
