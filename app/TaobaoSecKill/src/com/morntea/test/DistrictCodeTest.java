package com.morntea.test;

import com.morntea.util.DistrictCode;

public class DistrictCodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String district = DistrictCode.getDistrict("321183", null);
		System.out.println(district);
		
		district = DistrictCode.getDistrict("140227", null);
		System.out.println(district);

	}

}
