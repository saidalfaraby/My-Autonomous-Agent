package PredatorPrey;
import Policy.Policy;

public class Predator extends Position{
	private Policy policy;
	
	public Predator(int x, int y){
		super(x,y);
	}
	
	public Predator(int x, int y, Policy policy){
		super(x,y);
		this.policy = policy;
	}
	
	public Predator(Position P){
		super(P);
	}
	
	public void getActionAndMove(String cs){
		String action = policy.getAction(cs);
		this.move(action);
	}
	
	public void transformPos(Position prey){
		this.update(this.getX()-prey.getX(), this.getY()-prey.getY());
	}
	
	public String toString(){
		return "Predator("+this.getX()+","+this.getY()+")";
	}
	
}
