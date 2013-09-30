package Calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Policy.Policy;
import Policy.PredatorRandom;
import PredatorPrey.Environment;
import PredatorPrey.Position;
import PredatorPrey.Statespace;


public class PolicyEvaluation {
	private Policy policy;
	private Statespace statespace;
	private double gamma;
	
	public PolicyEvaluation(Policy policy, Statespace statespace, double gamma){
		this.policy = policy;
		this.statespace = statespace;
		this.gamma = gamma;
	}
		
	public void doEvaluation() {
		double delta;
		double theta = 0.000001;
		double V,v;
		double sumOverNextStates; 	//variable to save the summation over 
							//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
							//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
							//by taking action a from state s
		double actionProb;	//probability taking action a in state s
							//under current policy
		
		ArrayList<String> allAction = new ArrayList<String>();
		ArrayList<String> allNextStates = new ArrayList<String>();
		String currentState;
		int counter = 0;
		do{
			counter ++;
			System.out.println("Iteration - "+counter);
			delta = 0;
			for (Map.Entry<String, Double> entry : this.statespace.getStateCollections().entrySet()) {
		        currentState = entry.getKey();
		        if (Statespace.isEndState(currentState))
		        	continue;
			    v = entry.getValue();
			    V = 0;
			    //get All possible actions in state s. In this problem independent of state s
			    allAction = Position.getAllActions();
			    //iterate over all possible actions
//			    System.out.println("====Current State=== "+currentState);
			    for (String action : allAction){
			    	actionProb = policy.getActionProb(currentState, action);
			    	//get All possible next states
			    	allNextStates = this.statespace.getAllPossibleNextStates(currentState, action);
			    	sumOverNextStates = 0;
			    	//iterate over all possible next states
			    	for (String nextStateAction : allNextStates){
			    		String nextState = nextStateAction.split("-")[0];
				    	String preyAction = nextStateAction.split("-")[1];
				    	transProb = Environment.getTransitionProb(allNextStates.size(), preyAction);
			    		transReward = Environment.getTransReward(currentState, action,nextState);
			    		sumOverNextStates += transProb*(transReward + gamma*this.statespace.get(nextState));
			    	}
			    	V += (actionProb*sumOverNextStates);
			    }
			    this.statespace.updateValue(currentState, V);
			    delta = Math.max(delta, Math.abs(v-V));
			   }
			System.out.println("Value of delta "+delta);
		} while (delta>theta);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statespace s = new Statespace();
		Policy p = new PredatorRandom(s, false);
		PolicyEvaluation PE = new PolicyEvaluation(p,s,0.8);
		PE.doEvaluation();	
		System.out.println(s.get(Position.absoluteToRelative(0, 0, 5, 5)).toString());
		System.out.println(s.get(Position.absoluteToRelative(2,3,5,4)).toString());
		System.out.println(s.get(Position.absoluteToRelative(2,10,10,0)).toString());
		System.out.println(s.get(Position.absoluteToRelative(10,10,0,0)).toString());
		System.out.println(s.get(Position.absoluteToRelative(5,5,5,4)).toString());
	}

}
