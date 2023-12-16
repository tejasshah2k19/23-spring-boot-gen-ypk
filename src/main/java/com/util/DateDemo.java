package com.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateDemo {

	public static void main(String[] args) {
		
		Date d1  = new Date();

		Date d2  = new Date();

		LocalDateTime loc1 = LocalDateTime.now(); //today 
		LocalDateTime loc2 = LocalDateTime.now();
		
		loc2 = loc2.plusDays(2);
		loc2 = loc2.plusHours(2);
		System.out.println(loc1);
		System.out.println(loc2);
		  
		
//		long t =  ChronoUnit.DAYS.between(loc1, loc2);
		long t =  ChronoUnit.SECONDS.between(loc1, loc2);
		
		System.out.println(t/3600);
		
		
		
	}
}
