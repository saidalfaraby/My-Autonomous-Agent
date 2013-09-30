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

public class PolicyIteration {
	
	private Policy policy;
	private Statespace statespace;
	private double gamma;
	
	public PolicyIteration(Policy policy, Statespace statespace, double gamma){
		this.policy = policy;
		this.statespace = statespace;
		this.gamma = gamma;
	}
	
	public void doEvaluation() {
		double delta;
		double theta = 0.001;
		double V,v;
		double sumOverNextStates; 	//variable to save the summation over 
							//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
							//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
							//by taking action a from state s
		ArrayList<String> allNextStates = new ArrayList<String>();
		String currentState;
		int counter = 0;
		do{
			counter ++;
			System.out.println("Iteration - "+counter);
			delta = 0;
			//V = 0;
			for (Map.Entry<String, Double> entry : this.statespace.getStateCollections().entrySet()) {
		        currentState = entry.getKey();
		        if (Statespace.isEndState(currentState))
		        	continue;
			    v = entry.getValue();
			    String action = policy.getAction(currentState);
			    allNextStates = this.statespace.getAllPossibleNextStates(currentState, action);
			    sumOverNextStates = 0;
			    //iterate over all possible next states
			    for (String nextStateAction : allNextStates){
		    		String nextState = nextStateAction.split("-")[0];
			    	String preyAction = nextStateAction.split("-")[1];
//			    	transProb = Environment.getTransitionProb(currentState, action, nextState);
			    	transProb = Environment.getTransitionProb(allNextStates.size(), preyAction);
			    	transReward = Environment.getTransReward(currentState, action,nextState);
			    	sumOverNextStates += transProb*(transReward + gamma*this.statespace.get(nextState));
			    }
			    V = sumOverNextStates;
			    this.statespace.updateValue(currentState, V);
			    if (Math.abs(v-V)>delta){
				    delta = Math.abs(v-V);
//				    System.out.println("Delta "+delta);
			    }   
			}
		} while (delta>theta);
	}
	
	public boolean doImprovement(){
		double delta;
		double theta = 0.0001;
		double V,v;
		double sumOverNextStates; 	//variable to save the summation over 
							//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
							//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
							//by taking action a from state s
		double actionProb;	//probability taking action a in state s
							//under current policy
		String maxAction;
		boolean policyStable = true;
		String currentState;
		ArrayList<String> allAction = new ArrayList<String>();
		//ArrayList<State> allNextStates = new ArrayList<State>();
		ArrayList<String> allNextStates = new ArrayList<String>();
		
		for (Map.Entry<String, Double> entry : this.statespace.getStateCollections().entrySet()) {
	        currentState = entry.getKey();
	        String b = policy.getAction(currentState);
	        if (Statespace.isEndState(currentState))
	        	continue;
	        allAction = Position.getAllActions();
		    //iterate over all possible actions
	        double maxV = -100;
	        maxAction = "";
		    for (String action : allAction){
		    	//get All possible next states
		    	allNextStates = this.statespace.getAllPossibleNextStates(currentState, action);
		    	sumOverNextStates = 0;
		    	
		    	//iterate over all possible next states
		    	for (String nextStateAction : allNextStates){
		    		String nextState = nextStateAction.split("-")[0];
			    	String preyAction = nextStateAction.split("-")[1];
		    		//transProb = Environment.getTransitionProb(currentState, action, nextState);
		    		transProb = Environment.getTransitionProb(allNextStates.size(), preyAction);
		    		transReward = Environment.getTransReward(currentState, action,nextState);
		    		sumOverNextStates += transProb*(transReward + gamma*this.statespace.get(nextState));
		    	}
		    	if (sumOverNextStates > maxV){
		    		maxV=sumOverNextStates;
		    		
		    		maxAction = action;
		    	}
		    }
		    Map<String,Double> temp = new HashMap<String,Double>();
		    temp.put(maxAction, 1.0);
		    policy.updateAction(currentState, temp);
		    if (!maxAction.equals(b))
		    	policyStable = false;		   
		}
		if (policyStable)
			System.out.println("True");
		else
			System.out.println("False");
		return policyStable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statespace s = new Statespace();
		PredatorRandom p = new PredatorRandom(s, true);
		PolicyIteration PI = new PolicyIteration(p, s, 0.8);
		boolean policyStable=false;
		int counter = 0;
		do {
			System.out.println("Counter ="+ ++counter);
			PI.doEvaluation();
//			for (int i=0;i<11;i++)
//				for (int j=0;j<11;j++){
//					State st = s.getStateCollections().get(i+"-"+j+"-"+5+"-"+5);
//					System.out.println(st);
//				}
			policyStable = PI.doImprovement();
		} while (policyStable==false);
		
		for (int i=-5;i<6;i++){
			for (int j=-5;j<6;j++){
				System.out.printf("%.4f",s.get(i, j, 0, 0));
				System.out.printf("\t\t");
			}
			System.out.println();
		}
	}

}
