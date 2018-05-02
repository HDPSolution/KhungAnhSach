package com.hdpsolution.khunganhtrangsach.TouchHelper;

/**
 * Utils
 * 
 * @author Juan Carlos Moreno (jcmore2@gmail.com)
 * 
 */
public class Utils {

	public static int generatRandomPositiveNegitiveValue(int max, int min) {
		int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
		return ii;
	}

}
