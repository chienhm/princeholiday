package com.morntea.identitycard;

import java.text.SimpleDateFormat;


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
		for(int i=0; i<codes.length; i++) {
		    System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(codes[i].date) + ": " + codes[i].code.size());
		}
	}

}
