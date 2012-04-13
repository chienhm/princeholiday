package com.morntea.identitycard;


public class IdentityCardTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IdentityCard identityCard = IdentityCard.parse("321183198610181039");
		if (identityCard != null) {
			System.out.println(identityCard.getType());
			System.out.println(identityCard.getDistrict());
			System.out.println(identityCard.getBirthday());
			System.out.println(identityCard.getSequence());
			System.out.println(identityCard.getGender());
		} else {
			System.out.println("The identity is invalid!");
		}
		
		identityCard = IdentityCard.parse("429001198512194515");
		if (identityCard != null) {
			System.out.println(identityCard.getType());
			System.out.println(identityCard.getDistrict());
			System.out.println(identityCard.getBirthday());
			System.out.println(identityCard.getSequence());
			System.out.println(identityCard.getGender());
		} else {
			System.out.println("The identity is invalid!");
		}
		
		identityCard = IdentityCard.parse("140227198603040043");
		if (identityCard != null) {
			System.out.println(identityCard.getType());
			System.out.println(identityCard.getDistrict());
			System.out.println(identityCard.getBirthday());
			System.out.println(identityCard.getSequence());
			System.out.println(identityCard.getGender());
		} else {
			System.out.println("The identity is invalid!");
		}
	}

}
