package PredatorPrey;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	/**
	 * @param args
	 */
	public static Position MirroringX(int x, int y){
		return new Position(x,-y);
	}
	
	public static Position MirroringY(int x, int y){
		return new Position(-x,y);
	}
	
	public static Position Rotate180(int x, int y){
		return new Position(-y,-x);
	}
	
	public static void main(String[] args) {
//		for (int i=0;i<6;i++){
//			for (int j=0;j<6;j++){
//				Position p = Rotate180(i,j);
//				System.out.printf("%s\t",p.toString());
//			}
//			System.out.println();
//		}
		String s = "Predator(-4,1), Prey(3,-3)";
		
//		System.out.println(x.toString());
//		System.out.println(S[0]);
//		System.out.println(S[1]);
//		while (m.find()) {
//			S
//			System.out.println(m.group());
////		    String s = m.group(1);
//		    // s now contains "BAR"
//		}
		System.out.println(Math.random());
		String a="halo";
		String b="uuuu";
		System.out.println(a+b);
		double x=-10;
		double y=3;
		if (y>x)
			System.out.println("hall");
	}

}
