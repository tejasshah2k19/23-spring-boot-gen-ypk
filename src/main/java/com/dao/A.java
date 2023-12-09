package com.dao;

import java.util.Calendar;

public class A {
	static A a = null;

	private A() {

	}

	void add() {
		Calendar.getInstance();
	}

	static A getInstance() {
		if (a == null)
			a = new A();
		return a;
	}
}

class B {
	void createA() {
		A.getInstance();
	}
}