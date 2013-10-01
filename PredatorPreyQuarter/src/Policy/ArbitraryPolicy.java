package Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import PredatorPrey.Position;
import PredatorPrey.Predator;
import PredatorPrey.Prey;
import PredatorPrey.Statespace;

public class ArbitraryPolicy implements Policy{
	
	private Map<String, String > policyCollections = new HashMap<String, String>();
	private Statespace statespace;
	public Map<String, String> getPolicyCollections() {
		return policyCollections;
	}

	public static ArrayList<String> getAllActions(){
		ArrayList<String> allActions = new ArrayList<String>();
		allActions.add("south");
		allActions.add("north");
		allActions.add("west");
		allActions.add("east");
		allActions.add("wait");
		return allActions;
	}
	
	public boolean generateEpisode(int initialx, int initialy, PreyRandom preyPolicy){
		Position preyDefault = new Position(0,0);
		Predator pred = new Predator(initialx, initialy, this);
		Prey prey = new Prey(preyDefault, preyPolicy);
		Double r=0.0;
		String currentState = this.statespace.toState(pred, prey);
		boolean valid=true;
		int counter=0;
		do {
			counter++;
			pred.getActionAndMove(currentState);
			currentState = this.statespace.toState(pred, prey);
			if (!pred.equals(prey)){
				prey.getActionAndMove(pred, currentState);
				currentState = this.statespace.toState(pred, prey);
			}
		}while (! pred.equals(prey) && counter<500); 	//500 is threshold for number of state in an 
															//episode to avoid never end episode
		if (counter>=500)
			valid = false;
		return valid;
	}
	
	public void initialize(){
		this.policyCollections.clear();
		ArrayList<String> actions = getAllActions();
		for (String key : this.statespace.getStateCollections().keySet()){
			Random random = new Random();
			int index = random.nextInt(actions.size());
			String randAction = actions.get(index);
			this.policyCollections.put(key, randAction);
		}
	}
	
	public ArbitraryPolicy(Statespace statespace){
		this.statespace = statespace;
		PreyRandom preyPolicy = new PreyRandom();
		boolean valid;
		//Check whether this policy is valid (can reach terminal state) for all initial states.
		do{
			initialize();
			valid=true;
			for (int i=0;i<6;i++)
				for (int j=0;j<6;j++){
					valid = generateEpisode(i, j, preyPolicy);
					if (valid==false)
						break;
				}			
		}while (valid==false);
		System.out.println("Valid");

	}
	
	
	public void updateAction(String key, String action){
		policyCollections.put(this.statespace.toState(key), action);
	}
	
	public String getAction(String key){
		return policyCollections.get(this.statespace.toState(key));
	}
	
	public String getAction(String key, Position pred){
		String action = policyCollections.get(this.statespace.toState(key));
		return Statespace.transformAction(pred, action);
	}

	@Override
	public double getActionProb(String s, String action) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateAction(String s, Map<String, Double> value) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]){
		Statespace sp = new Statespace();
		ArbitraryPolicy AP = new ArbitraryPolicy(sp);
		PreyRandom PR = new PreyRandom();
	}
	

}
