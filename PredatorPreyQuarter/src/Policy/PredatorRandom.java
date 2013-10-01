package Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import PredatorPrey.Position;
import PredatorPrey.Statespace;

public class PredatorRandom implements Policy{
	
	private Map<String, Map<String,Double> > policyCollections = new HashMap<String, Map<String,Double>>();
	private Statespace sp;
	public PredatorRandom(){}
	
	
	
	public PredatorRandom(Statespace statespace, boolean deterministic){
		this.sp = statespace;
		ArrayList<String> actions = Position.getAllActions();
	    for (String key : statespace.getStateCollections().keySet()){
	    	Map<String,Double> actprob = new HashMap<String,Double>();
	    	if (deterministic){
				Random random = new Random();
				int index = random.nextInt(actions.size());
				String randAction = actions.get(index);
				actprob.put(randAction, 1.0);
				
			} else {
				for (String a:actions){
					actprob.put(a, 0.2);
				}
			}
	        policyCollections.put(key,actprob);
	    }
	}
	
	public void updateAction(String state, Map<String, Double> value){
		state = sp.toState(sp.getPredator(state), sp.getPrey(state));
		policyCollections.put(state, value);
		
	}
	
	public String getAction(String cs){
		Map<String,Double> actprob = new HashMap<String,Double>();
		actprob = policyCollections.get(cs);
		Random       random    = new Random();
		List<String> keys      = new ArrayList<String>(actprob.keySet());
		String       randomKey = keys.get( random.nextInt(keys.size()));
		
		return Statespace.transformAction(Statespace.getPredator(cs), randomKey);
	}
	
	public Map<String, Map<String, Double>> getPolicyCollections() {
		return policyCollections;
	}

	public double getActionProb(String state, String action){
		return policyCollections.get(state).get(action);
	}	
	
	public static void main(String[] args){
		String state = "Predator(1,5), Prey(0,0)";
		Map<String, Double> value=null;
		PredatorRandom PR = new PredatorRandom();
		PR.updateAction(state, value);
	}

}
