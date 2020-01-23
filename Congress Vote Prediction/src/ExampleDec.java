import java.util.ArrayList;

public class ExampleDec {
	public ArrayList<String> votes;
	public String label;
	public double probability = 0;
	
	public ExampleDec(ArrayList<String> input) {
		votes = new ArrayList<String>();
		for(String a : input) {
			if(a.equals("Democrat"))
				label = "Democrat";
			else if(a.equals("Republican"))
				label = "Republican";
			else
				votes.add(a);
		}
//		System.out.println(votes.size());
	}
	
	
}
