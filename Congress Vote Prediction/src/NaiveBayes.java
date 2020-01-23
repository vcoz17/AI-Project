import java.util.ArrayList;

public class NaiveBayes {
	private ArrayList<Democrat> listDem;
	private ArrayList<Republican> listRep;
	private ArrayList<String> vocab;
	private ArrayList<Voter> voters;
	public NaiveBayes() {
		listDem = new ArrayList<>();
		listRep = new ArrayList<>();
		vocab = new ArrayList<>();
		vocab.add("Nay");
		vocab.add("Yea");
		vocab.add("Present");
		vocab.add("Not Voting");
	}
	
	public ArrayList<String> getVocab() {
		return vocab;
	}
	
	public ArrayList<Democrat> getListDem() {
		return listDem;
	}
	
	public ArrayList<Republican> getListRep() {
		return listRep;
	}
	
	public ArrayList<Voter> getVoters(){
		return voters;
	}
	
	public void setVoters(ArrayList<Voter> voters){
		this.voters = voters;
	}
	
	public int getVocabSize() {
		return vocab.size();
	}
	
	public boolean isInVocab (String a) {
		return vocab.contains(a);
	}
	
	public int getNumDem() {
		return listDem.size();
	}
	
	public int getNumRep() {
		return listRep.size();
	}
	
	public void predictLabel(ArrayList<Voter> voters) {
		for(Voter v : voters) {
			double propDem = calDemVoter(v);
			double propRep = calRepVoter(v);
			v.setProbability(Math.max(propRep, propDem));
			if(propRep >= propDem) 
				v.setLabel("Republican");
			else
				v.setLabel("Democrat");
		}
	}

	private double calRepVoter(Voter v) {
		// TODO Auto-generated method stub
		double result = 0;
		
		int v_yea = v.getYea();
		int v_nay = v.getNay();
		int v_nV = v.getNV();
		int v_present = v.getPresent();
		
		int rep_yea = getTotalYea("Republican");
		int rep_nay = getTotalNay("Republican");
		int rep_nV = getTotalNV("Republican");
		int rep_present = getTotalPresent("Republican");
		int rep_total = rep_yea+rep_nay+rep_nV+rep_present;
		
		result = (Math.log(listRep.size()) - Math.log(listDem.size()+listRep.size()))+
				(Math.log(rep_yea+1) - Math.log(rep_total+vocab.size()))* v_yea+
				(Math.log(rep_nay+1) - Math.log(rep_total+vocab.size()))* v_nay+
				(Math.log(rep_nV+1) - Math.log(rep_total+vocab.size()))* v_nV+
				(Math.log(rep_present+1) - Math.log(rep_total+vocab.size()))* v_present;
//		System.out.println("vocab size:" + rep_total+vocab.size());
//		System.out.println("DIS: " + (Math.log(v_yea*(rep_yea+1)) - Math.log(rep_total+vocab.size() )) );
		return Math.exp(result);
	}
	
	private double calDemVoter(Voter v) {
		// TODO Auto-generated method stub
		double result = 0;
		
		int v_yea = v.getYea();
		int v_nay = v.getNay();
		int v_nV = v.getNV();
		int v_present = v.getPresent();
		
		int dem_yea = getTotalYea("Democrat");
		int dem_nay = getTotalNay("Democrat");
		int dem_nV = getTotalNV("Democrat");
		int dem_present = getTotalPresent("Democrat");
		int dem_total = dem_yea+dem_nay+dem_nV+dem_present;
		
		result = (Math.log(listDem.size()) - Math.log(listRep.size()+listDem.size()))+
				(Math.log(dem_yea+1) - Math.log(dem_total+vocab.size()))* v_yea+
				(Math.log(dem_nay+1) - Math.log(dem_total+vocab.size()))* v_nay+
				(Math.log(dem_nV+1) - Math.log(dem_total+vocab.size()))* v_nV+
				(Math.log(dem_present+1) - Math.log(dem_total+vocab.size()))* v_present;
		return Math.exp(result);
	}

	private int getTotalPresent(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		if(string.equals("Democrat")) {
			for(Democrat d : listDem)
				result += d.getPresent();
		}else {
			for(Republican r : listRep)
				result += r.getPresent();
		}
		return result;
	}

	private int getTotalNV(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		if(string.equals("Democrat")) {
			for(Democrat d : listDem)
				result += d.getNV();
		}else if(string.equals("Republican")) {
			for(Republican r : listRep)
				result += r.getNV();
		}
		return result;
	}

	private int getTotalNay(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		if(string.equals("Democrat")) {
			for(Democrat d : listDem)
				result += d.getNay();
		}else if(string.equals("Republican")) {
			for(Republican r : listRep)
				result += r.getNay();
		}
		return result;
	}

	private int getTotalYea(String string) {
		// TODO Auto-generated method stub
		int result = 0;
		if(string.equals("Democrat")) {
			for(Democrat d : listDem)
				result += d.getYea();
		}else if(string.equals("Republican")) {
			for(Republican r : listRep)
				result += r.getYea();
		}
		return result;
	}
	
	public void printVotersLabel(ArrayList<Voter> voters) {
		for(Voter v : voters) {
			System.out.println(v.getLabel());
		}
	}
	
	public double checkPrecision() {
		ArrayList<Republican> v_listRep = new ArrayList<>();
		ArrayList<Democrat> v_listDem = new ArrayList<>();
		for(Voter v : voters) {
			if(v.getLabel().equals("Republican"))
				v_listRep.add(new Republican());
			else if(v.getLabel().equals("Democrat")) {
				v_listDem.add(new Democrat());
			}
		}
		double mismatch = Math.abs(v_listDem.size() - listDem.size()) + Math.abs( v_listRep.size() - listRep.size());
		
		return 1 - mismatch/(listDem.size()+listRep.size());	
	}

}
