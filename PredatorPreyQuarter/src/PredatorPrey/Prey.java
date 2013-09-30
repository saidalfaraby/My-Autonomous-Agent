package PredatorPrey;

import java.util.ArrayList;

import Policy.Policy;

public class Prey extends Position{
	private Policy policy;
	//private static ArrayList<String> Actions = new ArrayList<String>();
	
	public Prey(int x, int y){
		super(x, y);
	}
	
	public Prey(Position P){
		super(P);
	}
	
	public Prey(Position P, Policy policy){
		super(P);
		this.policy = policy;
	}
	
	public Prey(int x, int y, Policy policy){
		super(x,y);
		this.policy = policy;
	}
	
	//Given a State, move position according to action return by policy
	public void getActionAndMove(Position pred, String cs){
		String action = policy.getAction(cs);
		this.moveRelative(pred, action);
	}
	
	//This is for relative position.
	//Given a State and pred position, find action for prey from policy, move Predator opposite to prey action
	public void moveRelative(Position Pred, String action){
//		System.out.println(action);
//		String action = policy.getAction(cs);
		Pred.move(transformMove(action));
	}
	
	//get opposite direction
	public String transformMove(String move){
		if (move.equals("wait"))
			return "wait";
		else if (move.equals("north"))
			return "south";
		else if (move.equals("south"))
			return "north";
		else if (move.equals("east"))
			return "west";
		else return "east";
	}
	
	public String toString(){
		return "Prey("+this.getX()+","+this.getY()+")";
	}
	
}
