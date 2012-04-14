package com.morntea.identitycard;

import java.util.Calendar;
import java.util.Date;

public class Test {

    public static Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
    
    public static void main(String[] args) {
       Date date = getDate(1999, 1, 1);
       System.out.println(date);

    }

}
