import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CongressNBC {
	public static void main(String[] args) throws FileNotFoundException {
		// TEST DATA
		String fileName1 = args[1];
		File file1 = new File(fileName1);
		ArrayList<Voter> voters = new ArrayList<>();
		if(!file1.exists()) {
			System.err.println("NO TEST FILE EXISTS");
		}else {
			ArrayList<String> testArray = new ArrayList<>();
			Scanner inFile = new Scanner(file1);
			String line = new String();
			while(inFile.hasNext()) {
				line = inFile.nextLine();
				testArray = convertCSVtoArrayList(line);	
//				System.out.println("Raw CSV data: " + line);
//				System.out.println("Converted ArrayList data: " + convertCSVtoArrayList(line));
//				System.out.println("Size:" + testArray.size() + "\n");
				Voter v = new Voter(testArray);
				voters.add(v);
			}
			inFile.close();
//			System.out.println("Pub 0 - Yea:" + voters.get(0).getYea());
//			System.out.println("Pub 1 - Yea:" + voters.get(1).getYea());
//			System.out.println("Pub 2 - Yea:" + voters.get(2).getYea());
//			System.out.println("Pub 0 - Nay:" + voters.get(0).getNay());
//			System.out.println("Pub 1 - Nay:" + voters.get(1).getNay());
//			System.out.println("Pub 2 - Nay:" + voters.get(2).getNay());	
		}
//		
//		System.out.println("\n*********");
//		System.out.println("*********");
//		System.out.println("NAIVE BAYES");
//		System.out.println("*********");
//		System.out.println("*********\n");
		
		//TRAINING DATA
		String fileName = args[0];
		File file = new File(fileName);
		NaiveBayes naiveBayes = new NaiveBayes();
		if(!file.exists()) {
			System.err.println("NO TRAINING FILE EXISTS!");
		}else {
			ArrayList<String> testArray = new ArrayList<>();
			Scanner inFile = new Scanner(file);
			String line = new String();
			while(inFile.hasNext()) {
				line = inFile.nextLine();
				testArray = convertCSVtoArrayList(line);
				
				if(testArray.get(testArray.size()-1).equals("Republican")) {
					testArray.remove(testArray.size()-1);
					Republican rep = new Republican(testArray);
					naiveBayes.getListRep().add(rep);
				}else if(testArray.get(testArray.size()-1).equals("Democrat")){
					testArray.remove(testArray.size()-1);
					Democrat dem = new Democrat(testArray);
					naiveBayes.getListDem().add(dem);
				}
			}
			inFile.close();
		}
		
		naiveBayes.setVoters(voters);
		naiveBayes.predictLabel(voters);
		for (Voter v : voters) {
			System.out.println(v.getLabel() + "," + v.getProbability());
		}
		
	}
	
	public static ArrayList<String> convertCSVtoArrayList(String input) {
		ArrayList<String> result = new ArrayList<>();
		if(input != null) {
			String[] splitData = input.split(",");
			for (int i = 0; i < splitData.length; i++) {
				if(splitData[i] != null && splitData[i].length() !=0)
					result.add(splitData[i].trim());
			}
		}	
		return result;
	}
	
}
