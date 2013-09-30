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


public class ValueIteration {
	private Policy policy;
	private Statespace statespace;
	private double gamma;
	
	public ValueIteration(Policy policy,Statespace statespace, double gamma){
		this.statespace = statespace;
		this.gamma = gamma;
		this.policy = policy;
	}
		
	public void doIteration() {
		double delta;
		double theta = 0.00002;
		double V,v;
		double sumOverNextStates; 	//variable to save the summation over 
							//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
							//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
							//by taking action a from state s
		
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
			    double maxV=-10;
			    for (String action : allAction){
			    	//get All possible next states
			    	allNextStates = this.statespace.getAllPossibleNextStates(currentState, action);
			    	sumOverNextStates = 0;
			    	//iterate over all possible next states
			    	//for (State nextState : allNextStates){
//			    	Iterator it1 = this.statespace.getStateCollections().entrySet().iterator();
			    	for (String nextStateAction : allNextStates){
			    		String nextState = nextStateAction.split("-")[0];
				    	String preyAction = nextStateAction.split("-")[1];
			    		//transProb = Environment.getTransitionProb(currentState, action, nextState);
				    	transProb = Environment.getTransitionProb(allNextStates.size(), preyAction);
			    		transReward = Environment.getTransReward(currentState, action,nextState);
			    		sumOverNextStates += transProb*(transReward + gamma*this.statespace.get(nextState));
			    	}
			    	//sumOverNextStates;
			    	maxV = Math.max(maxV, sumOverNextStates);
			    }
			    V = maxV;
			    this.statespace.updateValue(currentState, V);
			    delta = Math.max(delta, Math.abs(v-V));
			}
			System.out.println("Value of delta "+delta);
		} while (delta>theta);
	}
	
	public void doOutputPolicy(){
		ArrayList<String> allAction = new ArrayList<String>();
		ArrayList<String> allNextStates = new ArrayList<String>();
		double sumOverNextStates; 	//variable to save the summation over 
		//all state prime(nextstate) in the equation 
		double transProb;	//probability go to state s prime if we take 
		//action a from current state s
		double transReward;	//expected immediate reward of going to state s prime
		//by taking action a from state s
		String currentState;
		for (Map.Entry<String, Double> entry : this.statespace.getStateCollections().entrySet()) {
	        currentState = entry.getKey();
	        if (Statespace.isEndState(currentState))
	        	continue;
//		    v = currentState.getValue();
//		    V = 0;
		    //get All possible actions in state s. In this problem independent of state s
		    allAction = Position.getAllActions();
		    //iterate over all possible actions
		    double maxV=-10;
		    String maxAction="";
		    for (String action : allAction){
		    	//get All possible next states
		    	allNextStates = this.statespace.getAllPossibleNextStates(currentState, action);
		    	sumOverNextStates = 0;
		    	//iterate over all possible next states
		    	//for (State nextState : allNextStates){
//		    	Iterator it1 = this.statespace.getStateCollections().entrySet().iterator();
		    	for (String nextStateAction : allNextStates){
		    		String nextState = nextStateAction.split("-")[0];
			    	String preyAction = nextStateAction.split("-")[1];
		    		//transProb = Environment.getTransitionProb(currentState, action, nextState);
			    	transProb = Environment.getTransitionProb(allNextStates.size(), preyAction);
		    		transReward = Environment.getTransReward(currentState, action,nextState);
		    		sumOverNextStates += transProb*(transReward + gamma*this.statespace.get(nextState));
		    	}
		    	//sumOverNextStates;
			    if (sumOverNextStates > maxV){
			    	maxV = sumOverNextStates;
			    	maxAction = action;
			    }
		    	
		    }
		    Map<String,Double> temp = new HashMap<String,Double>();
		    temp.put(maxAction, 1.0);
		    policy.updateAction(currentState, temp);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statespace s = new Statespace();
		Policy p = new PredatorRandom(s, true);
		ValueIteration VI = new ValueIteration(p, s,0.8);
		VI.doIteration();		
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				System.out.printf("%.4f",s.get(i, j, 0, 0));
				System.out.printf("\t\t");
			}
			System.out.println();
		}
		
//		VI.doOutputPolicy();
//		System.out.println(s.get(0,0,5,5).toString());
//		System.out.println(s.get(2,3,5,4).toString());
//		System.out.println(s.get(2,10,10,0).toString());
//		System.out.println(s.get(10,10,0,0).toString());
//		System.out.println(s.get(5,5,5,4).toString());
		
	}

}
