package com.morntea.identitycard;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IdentityCard stores the info of the identity number.
 * <p>
 * 
 * IdentityCard could validate an identity number, 
 * and obtain other information from the identity number, 
 * for example, Gender, Birth date, Home town and so on.
 * <p>
 * 
 * The 15bit identity is like [dddddd yymmdd x] <br>
 * dddddd: district code. <br>
 * yymmdd: birthday. <br>
 * x: sequence number, odd for male, even for female.
 * <p>
 * 
 * The 18bit identity is like [dddddd yyyymmdd xxx c]. <br>
 * dddddd: district code. <br>
 * yyyymmdd: birthday. <br>
 * xxx: sequence number, odd for male, even for female. <br>
 * c: check code of the front 17 bit.
 */
public class IdentityCard {

	private String numder;

	private int type;
	
	private String province;

	private String city;

	private String county;

	private Date birthday;
	
	private String sequence;

	private int gender;

	public static final int IDENTITY_OLD = 0;
	
	public static final int IDENTITY_NEW = 1;
	
	public static final int IDENTITY_LENGTH_OLD = 15;

	public static final int IDENTITY_LENGTH_NEW = 18;

	public static final int FEMALE = 0;
	
	public static final int MALE = 1;

	private static final int GENDER_INDEX_OLD = IDENTITY_LENGTH_OLD - 1;

	private static final int GENDER_INDEX_NEW = IDENTITY_LENGTH_NEW - 2;

	private static final int BIRTHDAY_INDEX = 6;

	private static final int BIRTHDAY_LENGTH_OLD = 6;

	private static final int BIRTHDAY_LENGTH_NEW = 8;
	
	private static final int DISTRICT_INDEX = 0;
	
	private static final int DISTRICT_LENGTH = 6;
	
	private static final int SEQUENCE_INDEX_OLD = 12;
	
	private static final int SEQUENCE_INDEX_NEW = 14;
	
	private static final int SEQUENCE_LENGTH = 3;
	
	private static final int FACTORS[] = new int[] { 7, 9, 10, 5, 8, 
		4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	private static final char CHECKCODE[] = new char[] { '1', '0', 'X', 
		'9', '8', '7', '6', '5', '4', '3', '2' };

	/**
	 * Constructor.
	 * The constructor is private, so only valid identity could be created.
	 */
	private IdentityCard(String number, int type, String province, String city, 
			String county, Date birthday, String sequence, int gender) {
		this.numder = number;
		this.type = type;
		this.province = province;
		this.city = city;
		this.county = county;
		this.birthday = birthday;
		this.birthday = birthday;
		this.sequence = sequence;
		this.gender = gender;
	}

	/**
	 * Create an IdentityCard object according to the identity number.
	 * If identity number is illegal, return null. 
	 * Validate length, birth place, birthday, checkcode and so on.
	 */
	public static IdentityCard parse(String number) {
		/*
		 *  validate the parameter.
		 */
		if (number == null) {
			return null;
		}

		/*
		 *  Validate the length.
		 */
		int type = IdentityCard.IDENTITY_NEW;
		if (number.length() == IDENTITY_LENGTH_OLD) {
			type = IdentityCard.IDENTITY_OLD;
		} else if (number.length() == IDENTITY_LENGTH_NEW) {
			type = IdentityCard.IDENTITY_NEW;
		} else {
			return null;
		}

		/*
		 * Validate the birthday. 
		 * Compute the birthday according to the Birthday bits. 
		 * It is 6bits(yyMMdd) for 15bit identity number, 
		 * while 8bits(yyyyMMdd) for 18bit identity number.
		 */
		Date birthday = null;
		String birthdayStr = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (type == IDENTITY_OLD) {
			birthdayStr = "19" + number.substring(BIRTHDAY_INDEX, 
					BIRTHDAY_INDEX + BIRTHDAY_LENGTH_OLD);
		} else {
			birthdayStr = number.substring(BIRTHDAY_INDEX,
					BIRTHDAY_INDEX + BIRTHDAY_LENGTH_NEW);
		}
		try {
			birthday = dateFormat.parse(birthdayStr);
		} catch (ParseException pe) {
			return null;
		}

		/*
		 * Validate the birth place.
		 */
		String districtCode = number.substring(DISTRICT_INDEX, 
				DISTRICT_INDEX + DISTRICT_LENGTH);
		if (!DistrictCode.isValid(districtCode, birthday)) {
			return null;
		}
		String province = DistrictCode.getProvince(districtCode, birthday);
		String city = DistrictCode.getCity(districtCode, birthday);;
		String county = DistrictCode.getCounty(districtCode, birthday);;

		/*
		 * compute the gender according to the Gender bit, 
		 * odd for male, even for female.
		 */
		int gender = MALE;
		if (type == IDENTITY_OLD) {
			gender = (number.charAt(GENDER_INDEX_OLD) - '0') % 2;
		} else {
			gender = (number.charAt(GENDER_INDEX_NEW) - '0') % 2;
		}
		
		/*
		 * Compute the sequence number.
		 */
		String sequence = null;
		if (type == IDENTITY_OLD) {
			sequence = number.substring(SEQUENCE_INDEX_OLD, 
					SEQUENCE_INDEX_OLD + SEQUENCE_LENGTH);
		} else {
			sequence = number.substring(SEQUENCE_INDEX_NEW, 
					SEQUENCE_INDEX_NEW + SEQUENCE_LENGTH);
		}

		/*
		 * Validate the checkcode. 
		 * The check code can be computed by c = sum(ai*wi)(mod 11).
		 * ai: the i-th number of the identity number.
		 * wi: the weighted factor for each ai, as follows, 
		 *     7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2.
		 * The check code can be from 0 to 10(denoted 10 as X), as follows,
		 *     1 0 X 9 8 7 6 5 4 3 2.
		 */
		if (type == IDENTITY_NEW) {
			number = number.toUpperCase();
			char checkcode = number.charAt(IDENTITY_LENGTH_NEW - 1);
			int sum = 0;
			for (int i = 0; i < IDENTITY_LENGTH_NEW - 1; ++i) {
				sum += (number.charAt(i) - '0') * FACTORS[i];
			}
			if (CHECKCODE[sum % 11] != checkcode) {
				return null;
			}
		}

		/*
		 *  Create the IdentityCard object.
		 */
		return new IdentityCard(number, type, province, city, county,
				birthday, sequence, gender);
	}

	/**
	 * Get the gender.
	 */
	public int getGender() {
		return this.gender;
	}

	/**
	 * Get the birthday.
	 */
	public Date getBirthday() {
		return this.birthday;
	}
	
	/**
	 * Get the province.
	 */
	public String getProvince() {
		return this.province;
	}
	
	/**
	 * Get the county.
	 */
	public String getCounty() {
		return this.county;
	}
	
	/**
	 * Get the district.
	 */
	public String getDistrict() {
		return this.province + this.city + this.county;
	}
	
	/**
	 * Get the city.
	 */
	public String getCity() {
		return this.city;
	}
	
	/**
	 * Get the identity type.
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Get the sequence.
	 */
	public String getSequence() {
		return this.sequence;
	}
	
	/**
	 * Get the number.
	 */
	public String getNumber() {
		return this.numder;
	}
}