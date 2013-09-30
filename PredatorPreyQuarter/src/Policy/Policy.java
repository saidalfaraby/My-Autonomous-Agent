package Policy;
import java.util.Map;


public interface Policy {
	public String getAction(String s);
	public double getActionProb(String s, String action);
	public void updateAction(String s, Map<String, Double> value);
}
