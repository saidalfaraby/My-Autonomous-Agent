package Policy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class EGreedyPolicy implements Policy{
	private Map<String, Double> Q;
	private double e=0.1;
	
	public EGreedyPolicy(Map<String, Double> Q){
		this.Q = Q;
	}
	
	public String getAction(String s){
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		double Qval;
		double maxQ=-10;
		String maxAction=null;
		for (String action : Actions){
			Qval = this.Q.get(s+"-"+action);
			if (maxQ<Qval){
				maxQ = Qval;
				maxAction = action;
			}
		}
		Random rand = new Random();
		int index;
		if (Math.random()<this.e){
			
			Actions.remove(maxAction);
			index = rand.nextInt(Actions.size());
			return Actions.get(index);
		}
		return maxAction;
	}
	
	public double getActionProb(String s, String a){
		ArrayList<String> Actions = ArbitraryPolicy.getAllActions();
		double Qval;
		double maxQ=-10;
		String maxAction=null;
		for (String action : Actions){
			Qval = this.Q.get(s+"-"+action);
			if (maxQ<Qval){
				maxQ = Qval;
				maxAction = action;
			}
		}
		
		if (a.equals(maxAction))
			return 1-this.e;
		else
			return e/(Actions.size()-1);
	}

	@Override
	public void updateAction(String s, Map<String, Double> value) {
		// TODO Auto-generated method stub
		
	}

}
