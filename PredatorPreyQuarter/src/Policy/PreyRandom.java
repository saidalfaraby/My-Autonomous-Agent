package Policy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import PredatorPrey.Position;

import PredatorPrey.Position;
import PredatorPrey.Statespace;

public class PreyRandom implements Policy {

	public String getAction(String cs){
		String[] actions = {"north","south","west","east"};
		Position prey = Statespace.getPrey(cs);
		Position pred = Statespace.getPredator(cs);
		ArrayList<String> possibleActions = new ArrayList<String>();
		for(int i=0;i<actions.length;i++){
			Position p = Position.move(prey, actions[i]);
			// prey can't move to an occupied position
			if(!p.equals(pred)){
				possibleActions.add(actions[i]);
			}
		}
		
		Random generator = new Random();
		double probability = generator.nextDouble();
		//System.out.println("prob:"+probability);
		if(probability > 0.8){
			int randomIndex = generator.nextInt(possibleActions.size());
			return possibleActions.get(randomIndex);
		}else{
			return "wait";
		}
	}
	
	public double getActionProb(String s, String action){
		return 0.2;
	}

	@Override
	public void updateAction(String s, Map<String, Double> value) {
		// TODO Auto-generated method stub
		
	}
	
	

}
