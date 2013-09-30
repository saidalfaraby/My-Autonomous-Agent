package PredatorPrey;

import java.util.ArrayList;

public class Environment {
	public static double getTransitionProb(String cs, String action, String ns){
		Position pred = Statespace.getPredator(cs);
		Position prey = Statespace.getPrey(cs);
		Position predNextState = Statespace.getPredator(ns);
		Position preyNextState = Statespace.getPrey(ns);
		
		Position predNext = Position.move(pred, action);
		if (predNext.equals(prey))
			return 1;
	/*	else if (action.equals("wait"))
			return 0.8;*/
		else {
			ArrayList<String> allActions = Position.getAllActions();
			int nActions=0;
			String preyAction="";
			for (String a : allActions){
				Position preyNext = Position.move(prey, a);
				//find what prey action that lead to next state (ns)
				if (predNext.equals(predNextState) && preyNext.equals(preyNextState))
					preyAction = a;
				//counting the number of state
				if (preyNext.equals(predNext))
					continue;
				else
					nActions++;
			}
			if (preyAction.equals("wait")){
				return 0.8;
			}
			//excludes wait
			return 0.2/(nActions-1);
		}
	}
	
	public static double getTransitionProb(int nStates, String preyAction){
		if (nStates ==1)
			return 1;
		else if (preyAction.equals("wait"))
			return 0.8;
		else 
			return 0.2/(nStates-1);
	}
	
	public static double getTransReward(String cs, String action, String ns){
		if (Statespace.isEndState(ns))
			return 10;
		else
			return 0;
	}
}
