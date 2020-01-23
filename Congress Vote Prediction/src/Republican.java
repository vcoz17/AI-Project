import java.util.ArrayList;

public class Republican implements VoterInterface {
	private int yea=0, nay=0, present=0, nV=0, blank=0;
	private String label ="Republican";
	
	public Republican() {}
	
	public Republican (ArrayList<String> input) {
		for(String a : input) {
			if(a.equals("Yea"))
				yea++;
			else if (a.equals("Nay"))
				nay++;
			else if (a.equals("Not Voting"))
				nV++;
			else if (a.equals("Present"))
				present++;
		}
		blank = 42 - getSize();
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String newLabel) {
		this.label = newLabel;
	}

	@Override
	public int getYea() {
		// TODO Auto-generated method stub
		return yea;
	}

	@Override
	public int getNay() {
		// TODO Auto-generated method stub
		return nay;
	}

	@Override
	public int getPresent() {
		// TODO Auto-generated method stub
		return present;
	}

	@Override
	public int getNV() {
		// TODO Auto-generated method stub
		return nV;
	}

	@Override
	public int getBlank() {
		// TODO Auto-generated method stub
		return blank;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return (yea+nay+present+nV);
	}
	
	}
