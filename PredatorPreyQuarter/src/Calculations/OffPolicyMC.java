package Calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import Policy.*;
import PredatorPrey.*;



class DataEpisode{
	private String s;
	private Double reward;
	private String action;
	
	public DataEpisode(String s, double r, String a){
		this.s = s;
		this.reward = r;
		this.action =a;
	}
	public String getS() {
		return s;
	}
	public Double getReward() {
		return reward;
	}
	public String getAction() {
		return action;
	}
	
	public String toString(){
		return s.toString()+"-"+action+"-"+reward;
	}
	
}
public class OffPolicyMC {
	private Map<String, Double> Q;
	private Map<String, Double> N;
	private Map<String, Double> D;
	private Map<String, Double> W;
	private Policy preyPolicy;
	private Policy Pi;
	private Policy PiPrime;
	private double gamma =0.8;
	Statespace statespace;
	private ArrayList<String> Actions=new ArrayList<String>();
	/**
	 * @param args
	 */
	
	public OffPolicyMC(Statespace sp, Policy Pi){
		this.Pi = Pi;
		this.statespace = sp;
		initialize();
	}
	
	public void initialize(){
		Actions = Position.getAllActions();
		
		//The key is String consist of State-action pair, and the Value is double.
		Q = new HashMap<String, Double>();
		N = new HashMap<String, Double>();
		D = new HashMap<String, Double>();
		W = new HashMap<String, Double>();
		Random rand = new Random();
		for (String state : this.statespace.getStateCollections().keySet()){
			if (Statespace.isEndState(state))
				continue;
			for (String action:this.Actions){
				String key = state+"-"+action;
				double random = (double)rand.nextInt(9);
//				System.out.print(random+" ");
				Q.put(key,Math.random());
				N.put(key,0.0);
				D.put(key,0.0);
				W.put(key,0.0);
			}
//			System.out.println();
		}
		
//		printMaxQ();
	}
	
	public double getReward(String s){
		if (Statespace.isEndState(s))
			return 10;
		else
			return 0;
	}
	
	//
	public ArrayList<DataEpisode> generateEpisode(){
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		//Choose Initial State(position of predator), and follow policy until terminate
		Random generator = new Random();
		int i = generator.nextInt(6);
		int j = generator.nextInt(6);
		Position preyDefault = new Position(0,0);
		Predator pred = new Predator(i,j, PiPrime);
		Prey prey = new Prey(preyDefault, preyPolicy);
		String predAction=null;
//		String preyAction=null;
		String currentState = this.statespace.toState(pred, prey);
		String[] bothActions = new String[2];
		int counter=0;
		while (! Statespace.isEndState(currentState)){
			counter++;
			//bothActions contain 2 kind of actions, 1 for quarter statespace, and 2nd for real statespace
			//we use 1st one to save with state, and 2nd one to move the pred object
			bothActions = ((EGreedyPolicy)PiPrime).getAction(currentState, pred);
//			System.out.println("both actions [0] "+bothActions[0]);
			dataEpisode.add(new DataEpisode(currentState,getReward(currentState),bothActions[0]));
			//this is the action for the real space
			pred.move(bothActions[0]);
			currentState = this.statespace.toState(pred, prey);
			if (Statespace.isEndState(currentState)){
				//Save the last State
				dataEpisode.add(new DataEpisode(currentState,getReward(currentState),null));
			} else{
				prey.getActionAndMove(pred, currentState);
				pred.toQuarter();
				currentState = this.statespace.toState(pred, prey);
			}
		}
		return dataEpisode;
	}
	
	public void doControl(){
		this.preyPolicy = new PreyRandom();
		ArrayList<DataEpisode> dataEpisode = new ArrayList<DataEpisode>();
		int counter=0;
		do{
			counter++;
			this.PiPrime = new EGreedyPolicy(this.Q);
			 do{
				 dataEpisode.clear();
				 dataEpisode =  generateEpisode();
			 }while (dataEpisode.size()<2);
			 
			 //Print data episode
			 
			 //-2 because we exclude for checking the terminal state
			 int tau = 0;
			 for ( int i=dataEpisode.size()-2;i>=0;i--){
				 if (!dataEpisode.get(i).getAction().equals(this.Pi.getAction(dataEpisode.get(i).getS()))){
					 tau = i;
					 break;
				 }
			 }
			 
//			 System.out.println("Qvalue before update anything");
//			 printMaxQ();
			 
//			 String tempState;
			 String tempStateString;
			 String tempAction;
			 int t=0;
			 //Hashmap for save the first occurrence state,action pair 
			 HashMap<String, Integer> tableFirstTimeOccurence = new HashMap<String, Integer>();
			 //Copy from ArrayList to HashSet to avoid duplicate pair state,action
			 Set<String> set = new HashSet<String>();
			 
			 for (int i=tau;i<dataEpisode.size()-1;i++){
				 if (!Statespace.isEndState(dataEpisode.get(i).getS()))
					 set.add(dataEpisode.get(i).getS().toString()+"-"+dataEpisode.get(i).getAction());
			 }
			 System.out.println(counter+" episode "+dataEpisode.size()+" tau "+(dataEpisode.size()-tau)+" size set "+set.size());
			 //For each pair state, action appearing in the episode after time tau
//			 .out.println("size "+set.size());
			 for (String pair : set){
				 String[] tempPair = pair.split("-");
//				 System.out.println(tempPair[0]+"-"+tempPair[1]);
				 tempStateString = tempPair[0];
				 tempAction = tempPair[1];
				 //looking for the time of first occurrence of s,a in the table
				 if (tableFirstTimeOccurence.get(pair) != null)
					 t = tableFirstTimeOccurence.get(pair);
				 else //If it doesn't exist in the table so just find, and add to the table
					 for (int j=tau;j<dataEpisode.size();j++)
						 if ((dataEpisode.get(j).getS()+"-"+dataEpisode.get(j).getAction()).equals(pair)){
							 t = j;
							 tableFirstTimeOccurence.put(pair, j);
							 break;	 
						 } 
				 double w=1.0;
				 double Return=0.0;
				 //Product of 1/piPrime(s_k,a_k)
				 int power=0;
				 for (int k=t+1;k<dataEpisode.size()-1;k++){
					 //For now we just use 0.5 since the probability of taking action in
					 //behaviour policy is equally likely
					 w*=1/((EGreedyPolicy)this.PiPrime).getActionProb(dataEpisode.get(k).getS(), dataEpisode.get(k).getAction());
					 Return+=dataEpisode.get(k).getReward()*Math.pow(this.gamma, power);
//					 System.out.println(dataEpisode.get(k).getS()+"-"+dataEpisode.get(k).getAction());
					 power++;
				 }
				 Return+=dataEpisode.get(dataEpisode.size()-1).getReward()*Math.pow(this.gamma, power);
				 
				 //Incremental implementation-----------------
				 this.W.put(pair, this.W.get(pair)+w);
				 this.Q.put(pair, this.Q.get(pair)+w/this.W.get(pair)*(Return-this.Q.get(pair)));
//				 System.out.println("Update Q at "+pair+"with value "+this.Q.get(pair));
				 //--------------------------------------------
//				 System.out.println("W value "+w+" Return "+Return);
				 this.N.put(pair, this.N.get(pair)+w*Return);
				 this.D.put(pair, this.D.get(pair)+w);
//				 System.out.println(pair+"---"+this.N.get(pair)/this.D.get(pair));
//				 System.out.println("Update Q at "+pair+"with value "+this.N.get(pair)/this.D.get(pair));
//				 this.Q.put(pair, this.N.get(pair)/this.D.get(pair));
			 }
//			 for (Map.Entry<String, Double> entry : this.Q.entrySet()) {
//				    String key = entry.getKey();
//				    Double value = entry.getValue();
//				    System.out.println(key+" "+value);
//			 }
			 
			 String stateAction=null;
			 double Qval ;
			 //Part d
			 //Iterate over all States
//			 for (String action : this.Q.keySet())
//				 System.out.println(action);
			 for (String state : this.statespace.getStateCollections().keySet()){
				 if (Statespace.isEndState(state))
					continue;
				 double maxQ=0.0;
				 String maxAction=null;
				 //Iterate over all actions
//				 System.out.println("Size actions "+Actions.size());
				 for (String action : Actions){
					 stateAction = state+"-"+action;
					 Qval = this.Q.get(stateAction);
//					 System.out.println(action+" "+Qval);
//					 System.out.println(Qval);
					 if (Qval>=maxQ){
						 maxQ = Qval;
						 maxAction = action;
					 }
				 }
				 //Assign max action to the policy
				 ((ArbitraryPolicy)this.Pi).updateAction(state, maxAction);
			 }
//			 System.out.println("Size Q : "+this.Q.size());
//			 System.out.println("Size N : "+this.N.size());
//			 System.out.println("Size D : "+this.D.size());
//			 System.out.println("Size table : "+tableFirstTimeOccurence);
//		 	 System.out.println("Size D : "+this.D.size());
			 
//			 System.out.println(counter);
		}while (counter <30000);
	}
	
	public Policy getPi() {
		return Pi;
	}
	
	public void printMaxQ(){
		String key;
		double Qval;
		
		String maxAction;
//		String action;
		ArrayList<String> Actions = Position.getAllActions();
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				key = this.statespace.toState(j, i, 0, 0);
				if (i==0 && j==0){
					System.out.printf("%f\t",0.0);
					continue;
				}
				double maxQ= 0.0;
				for (String action : Actions){
					 String stateAction = key+"-"+action;
//					 System.out.println(stateAction);
					 Qval = this.Q.get(stateAction);
//					 System.out.println(action+" "+Qval);
//					 System.out.println(Qval);
					 if (Qval>=maxQ){
						 maxQ = Qval;
						 maxAction = action;
					 }
//					 System.out.println(action+" "+maxQ);
					 
				 }
//				action = ((ArbitraryPolicy)off.getPi()).getPolicyCollections().get(key);
				System.out.printf("%f\t",maxQ);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statespace sp= new Statespace();
		ArbitraryPolicy Pi = new ArbitraryPolicy(sp);
		OffPolicyMC off = new OffPolicyMC(sp,Pi);
		
		off.doControl();
		
		for (String key : off.Q.keySet()){
//			System.out.println(key+" = "+off.Q.get(key));
		}
		
		String key;
		double Qval;
		double maxQ=-10;
		String maxAction;
//		String action;
		ArrayList<String> Actions = Position.getAllActions();
		off.printMaxQ();
		
//		String key;
		String action;
		for (int i=0;i<6;i++){
			for (int j=0;j<6;j++){
				if (i==0 && j==0){
					System.out.print("prey\t");
					continue;
				}
				key = off.statespace.toState(j, i, 0, 0);
				action = ((ArbitraryPolicy)off.getPi()).getPolicyCollections().get(key);
				System.out.printf("%s\t",action);
			}
			System.out.println();
		}
	}

}
