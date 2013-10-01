package PredatorPrey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Policy.PredatorRandom;
import PredatorPrey.Position;

public class Statespace {
	private Map<String, Double> stateCollections = new HashMap<String, Double>();
	
	public Statespace(){
		initialize();
	}
	
	//State is encode as String type of "Predator(x,y), Prey(x,y)"
	//Initialize statespace with key State, and value 0.0;
	public void initialize(){
		int k=0;
    	int l=0;
    	for(int i = 0; i < 6; i++) 
		    for(int j = 0; j < 6; j++){     	
		    	this.stateCollections.put(toState(i,j,k,l), 0.0);
		    }
	}
	
//	public Map<String, String> getAllPossibleNextStates(String currentState, String predmove) {
	public ArrayList<String> getAllPossibleNextStates(String currentState, String predmove) {
		ArrayList<String> allPossibleNextStates = new ArrayList<String>();
		ArrayList<String> Actions = Position.getAllActions();
		Predator pred = getPredator(currentState);
		Prey prey = getPrey(currentState);
    	Predator tempPred;
    	// the next position of the predator when taken a:predmove
    	pred.move(predmove);
    	if (pred.equals(prey)){
    		allPossibleNextStates.add(toState(pred,prey)+"-"+null);
    		return allPossibleNextStates;
    	}
    	for (String action : Actions){
    		// the next position of the prey
    		//copy object Pred, because we need the ori for next iteration
    		tempPred = new Predator(pred.getX(),pred.getY());
    		prey.moveRelative(tempPred, action);
    		// a prey will never move to an occupied position
    		if (tempPred.equals(prey)){
    			continue;
    		}
    		else{
    			allPossibleNextStates.add(toState(tempPred,prey)+"-"+action);
    		}
    	}
    	return allPossibleNextStates;
    }
	
	
	public Position toQuarter(Position p){
		if (p.getX()>=0 && p.getY()>=0){
			return new Position(p.getX(), p.getY());
		} else if (p.getX()>=0 && p.getY()<0){
			//Quadran I
			return new Position(p.getX(), -p.getY());
		} else if (p.getX()<0 && p.getY()<0){
			//Quadran II
			return new Position(-p.getX(), -p.getY());
		} else if (p.getX()<0 && p.getY()>=0){
			//Quadran III
			return new Position(-p.getX(), p.getY());
		} else
			return null;
	}
	
	//Transform action according to position of predator
	public static String transformAction2(Position p, String action){
		if (p.getX()>=0 && p.getY()>=0){
			return action;
		} else if (p.getX()>=0 && p.getY()<0){
			//Quadran I
			if (action.equals("north"))
				return "south";
			else if (action.equals("south"))
				return "north";
			else
				return action;
		} else if (p.getX()<0 && p.getY()<0){
			//Quadran II
			if (action.equals("north"))
				return "south";
			else if (action.equals("south"))
				return "north";
			else if (action.equals("east"))
				return "west";
			else if (action.equals("west"))
				return "east";
			else 
				return action;
		} else if (p.getX()<0 && p.getY()>=0){
			//Quadran III
			if (action.equals("east"))
				return "west";
			else if (action.equals("west"))
				return "east";
			else
				return action;
		} else{
			throw new RuntimeException();
//			return null;
		}
	}
	public static String transformAction(Position pred, String action){
//		Predator pred = getPredator(state);
//		Prey prey = getPrey(state);
		Position predNext = Position.move(pred,action);
		if ((pred.getX()==0||pred.getY()==0)&&(predNext.getX()==0||predNext.getY()==0)){
			return transformAction2(predNext, action);
		} else if (pred.getX()==0||pred.getY()==0){
			return transformAction2(predNext, action);
		} else 
			return transformAction2(pred, action);
		
	}
	
	//Given a state in String, create object Predator
	public static Predator getPredator(String state){
		Pattern MY_PATTERN = Pattern.compile("\\((.*?)\\)",Pattern.DOTALL);
		Matcher m = MY_PATTERN.matcher(state);
		m.find(); 
		String[] parts1 = m.group(1).split(",");
		return new Predator(Integer.parseInt(parts1[0]), Integer.parseInt(parts1[1]));
	}
	
	//Given a state in String, create object Prey
	public static Prey getPrey(String state){
		Pattern MY_PATTERN = Pattern.compile("\\((.*?)\\)",Pattern.DOTALL);
		Matcher m = MY_PATTERN.matcher(state);
		m.find(); 
		m.find();
		String[] parts1 = m.group(1).split(",");
		return new Prey(Integer.parseInt(parts1[0]), Integer.parseInt(parts1[1]));
	}
	
	public static boolean isEndState(String state){
		Position pred = getPredator(state);
		Position prey = getPrey(state);
		if (pred.equals(prey))
			return true;
		else
			return false;
	}
	
	public void add(String key, double val){
		key = toState(getPredator(key), getPrey(key));
		this.stateCollections.put(key,val);
	}
	
	public String toState(Position p1, Position p2){
		p1 = toQuarter(p1);
		return "Predator("+p1.getX()+","+p1.getY()+"), Prey("+p2.getX()+","+p2.getY()+")";
	}
	
	public String toState(int predX, int predY, int preyX, int preyY){
		return toState(new Position(predX, predY), new Position(preyX, preyY));
	}
	
	public String toState(String state){
		return toState(getPredator(state), getPrey(state));
	}
	
	public Double get(Position pred, Position prey){
		return this.stateCollections.get(toState(pred, prey));
	}
	
	public Double get(String key){
		key = toState(getPredator(key), getPrey(key));
		return this.stateCollections.get(key);
	}
	
	public Double get(int predx, int predy, int preyx, int preyy){
		return this.stateCollections.get(toState(predx,predy,preyx,preyy));
	}
	
	public Double get(int[] index){
		return this.stateCollections.get(toState(index[0],index[1],index[2],index[3]));
	}

	public Map<String, Double> getStateCollections() {
		return stateCollections;
	}
	
	public void updateValue(String state, double value){
		state = toState(getPredator(state), getPrey(state));
		stateCollections.put(state, value);
	}
	
	public void print(){
		for (String s : stateCollections.keySet()){
			System.out.println(s+"---"+stateCollections.get(s));
		}
	}
	
	public static void main(String[] args){
		String state = "Predator(1,5), Prey(0,0)";
		Map<String, Double> value=null;
		Statespace ST = new Statespace();
		System.out.println(ST.toQuarter(new Position(-2,0)).toString());
	}
}
