package PredatorPrey;

import java.util.ArrayList;

public class Position {
	private int x;
	private int y;
	
	public Position(){
		this.update(0,0);
	}
	
	public Position(Position P){
		this.update(P.getX(),P.getY());
	}
	
	public Position(int x, int y){
		this.update(x,y);
	}
	
	public String toString(){
		return "("+x+","+y+")";
	}
	
	public static int wrap(int i) {
		if ((i > 5))
		    return i -= 11;
		if ((i < -5))
		    return i += 11;
		return i;
	}
	
	public void update(int x, int y){
		this.x = wrap(x);
		this.y = wrap(y);
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}

	public void move(String move) {
		if(move.equals("north"))
		    this.update(x, y-1);
		if(move.equals("south"))
			this.update(x, y+1);
		if(move.equals("west"))
			this.update(x-1, y);
		if(move.equals("east"))
			this.update(x+1, y);
	}
	
	public static int[] absoluteToRelative(int x1, int y1, int x2, int y2){
		int [] index=new int[4];
		index[0]=wrap(x1-x2);
		index[1]=wrap(y1-y2);
		index[2]=0;
		index[3]=0;
		return index;
	}
	
	public static Position move(Position p, String move) {
		int x = p.getX();
		int y = p.getY();
		if(move.equals("north"))
		    return new Position(x, y-1);
		else if(move.equals("south"))
			return new Position(x, y+1);
		else if(move.equals("west"))
			return new Position(x-1, y);
		else if(move.equals("east"))
			return new Position(x+1, y);
		else
			return new Position(x,y);
	}
	
//	public Position toQuarter(Position p){
//		if (p.getX()>=0 && p.getY()>=0){
//			return new Position(p.getX(), p.getY());
//		} else if (p.getX()>=0 && p.getY()<0){
//			//Quadran I
//			return new Position(p.getX(), -p.getY());
//		} else if (p.getX()<0 && p.getY()<0){
//			//Quadran II
//			return new Position(-p.getX(), -p.getY());
//		} else if (p.getX()<0 && p.getY()>=0){
//			//Quadran III
//			return new Position(-p.getX(), p.getY());
//		} else
//			return null;
//	}
	
	public boolean equals(Position p2){
		if ((this.getX()==p2.getX()) && this.getY()==p2.getY())
			return true;
		else
			return false;
	}
	
	public static ArrayList<String> getAllActions(){
		ArrayList<String> allActions = new ArrayList<String>();
		allActions.add("north");
		allActions.add("south");
		allActions.add("west");
		allActions.add("east");
		allActions.add("wait");
		return allActions;
	}
}
