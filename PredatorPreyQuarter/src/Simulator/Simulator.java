package Simulator;

import Policy.PredatorRandom;
import Policy.PreyRandom;
import PredatorPrey.Predator;
import PredatorPrey.Prey;
import PredatorPrey.Statespace;

public class Simulator {

	/**
	 * @param args
	 */
	public int simulate(){
		Statespace s = new Statespace();
		PredatorRandom predPolicy = new PredatorRandom(s, false);
		PreyRandom preyPolicy = new PreyRandom();
		Prey prey = new Prey(0,0, preyPolicy);
		Predator pred = new Predator(5,5,predPolicy);
		String currentState = s.toState(pred,prey);
		int counter=0;
		while (!prey.equals(pred)){
			counter++;
			pred.getActionAndMove(currentState);
//			System.out.println(pred.toString());
			currentState = s.toState(pred,prey);
			System.out.println(currentState);
			if (!pred.equals(prey)){
				prey.getActionAndMove(pred,currentState);
				currentState = s.toState(pred,prey);
				System.out.println("cui");
			}
//			System.out.println(pred.toString());
//			System.out.println(prey.toString());
			System.out.println(currentState);
			System.out.println("------"+counter);
		} 
		System.out.println("------"+counter);
		return counter;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Simulator S = new Simulator();
		int sum=0;
		for (int i=0;i<1;i++){
			int c = S.simulate();
			sum +=c;
		}
		System.out.println("Average : "+sum/100);
		
	}

}
