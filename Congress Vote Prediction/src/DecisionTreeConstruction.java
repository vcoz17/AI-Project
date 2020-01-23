import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class DecisionTreeConstruction {
	public ArrayList<ArrayList<String>> attributesOption;
	public ArrayList<ExampleDec> examples;
	public int[] attributes;
	public ArrayList<ExampleDec> testing_examples;
	
	public DecisionTreeConstruction(File file, File file1) throws FileNotFoundException {
		attributesOption = new ArrayList<>();
		examples = new ArrayList<>();
		testing_examples = new ArrayList<>();
		readTrainingData(file);
		readTestingData(file1);
		attributesOption = generateAttributes(examples);
		int attSize = examples.get(0).votes.size();
		attributes = new int[attSize];
		for (int i = 0; i < attSize; i++) {
			attributes[i] = i;
		}
	}
	
	public ArrayList<ArrayList<String>> generateAttributes(ArrayList<ExampleDec> examples){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < examples.get(0).votes.size(); i++) {
			ArrayList<String> att = new ArrayList<>();
			for(ExampleDec e : examples) {
					att.add(e.votes.get(i));
			}
			
			result.add(att);
		}
		ArrayList<String> att = new ArrayList<>();
		for(ExampleDec e : examples) {	
			att.add(e.label);
		}
		result.add(att);
//		for(String s : result.get(result.size()-1))
//			System.out.println(s);
//		System.out.println();
		return result;
	}
	
	private void readTrainingData(File file) throws FileNotFoundException {
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
					ExampleDec rep = new ExampleDec(testArray);
					rep.label = "Republican";
					examples.add(rep);
				}else if(testArray.get(testArray.size()-1).equals("Democrat")){
					testArray.remove(testArray.size()-1);
					ExampleDec dem = new ExampleDec(testArray);
					dem.label = "Democrat";
					examples.add(dem);
				}
			}
			inFile.close();
		}
	}
	
	private void readTestingData(File file) throws FileNotFoundException {
		if(!file.exists()) {
			System.err.println("NO TESTING FILE EXISTS!");
		}else {
			ArrayList<String> testArray = new ArrayList<>();
			Scanner inFile = new Scanner(file);
			String line = new String();
			while(inFile.hasNext()) {
				line = inFile.nextLine();
				testArray = convertCSVtoArrayList(line);
				ExampleDec example = new ExampleDec(testArray);
				testing_examples.add(example);
			}
			inFile.close();
		}
	}
	
	private ArrayList<String> convertCSVtoArrayList(String input) {
		ArrayList<String> result = new ArrayList<>();
		if(input != null) {
			String[] splitData = input.split(",");
			for (int i = 0; i < splitData.length; i++) {
					result.add(splitData[i].trim());
			}
		}	
		return result;
	}
	
	public double[] dataToDistribution(ArrayList<String> data) {
		ArrayList<String> labels = getSetOfLabels(data);
		double[] dist = new double[labels.size()];
		int i = 0;
		for(String l : labels) {
			dist[i] = (double) Collections.frequency(data, l)/data.size();
			i++;
		}
		return dist;
	}
	
	public double entropy(double[] dist) {
		double sum = 0;
		for(int i = 0; i < dist.length; i++) {
			sum -= dist[i]*Math.log(dist[i])/Math.log(2.0); 
		}
		return sum;
	}
	
	public ArrayList<ArrayList<String>> splitData(int attIndex, ArrayList<ExampleDec> examples) {
		ArrayList<ArrayList<String>> attributesOption = generateAttributes(examples);
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		ArrayList<String> extractAtt = new ArrayList<>();
		ArrayList<String> labels = getSetOfLabels(attributesOption.get(attIndex));
		for (String l : labels) {
			extractAtt = new ArrayList<>();
			for (int i = 0; i < attributesOption.get(attIndex).size(); i++) {
				if(attributesOption.get(attIndex).get(i).equals(l)) {
					if(attributesOption.get(attributesOption.size()-1).get(i).equals("Democrat")) {
						extractAtt.add("Democrat");
					} else if(attributesOption.get(attributesOption.size()-1).get(i).equals("Republican")) {
						extractAtt.add("Republican");
					}
				}
				
			}
//			for (String s : extractAtt) {
//				System.out.print(s+", ");
//			}
//			System.out.println();
			result.add(extractAtt);
		}
		return result;
		
	}
	
	public double remainder(int attIndex, ArrayList<ExampleDec> examples) {
		ArrayList<ArrayList<String>> attributesOption = generateAttributes(examples);
		ArrayList<ArrayList<String>> splitData = splitData(attIndex, examples);
		ArrayList<String> attribute = attributesOption.get(attIndex);
		ArrayList<String> labels = getSetOfLabels(attribute);
		double result = 0;
		int i = 0;
		for(String l : labels) {
			result += (double) Collections.frequency(attribute, l)*entropy(dataToDistribution(splitData.get(i)))/examples.size();
			i++;
		}
		return result;
	}
	
	public double gain(int attIndex, ArrayList<ExampleDec> examples) {
		ArrayList<ArrayList<String>> attributesOption = generateAttributes(examples);
		return entropy(dataToDistribution(attributesOption.get(attributesOption.size()-1))) - remainder(attIndex,examples); 
	}

	private ArrayList<String> getSetOfLabels(ArrayList<String> data) {
		// TODO Auto-generated method stub
		ArrayList<String> labels = new ArrayList<>();
		for (String s : data) {
			if(!labels.contains(s))
				labels.add(s);
			}
		return labels;
	}
	
	public TreeNode<String> decisionTreeLearning(ArrayList<ExampleDec> examples, int[] attributes, ArrayList<ExampleDec> parent_examples) {
		if(examples.isEmpty()) {
			return pluralityValue(parent_examples);
		}
		else if(homogenous(examples)) {
			TreeNode<String> root = new TreeNode<String>(examples.get(0).label);
			root.probability = 1.0;
			return root;
		}else if(attributes.length == 0) {
			return pluralityValue(examples);
			}
		else {
			int splitFeature = -1;
			double maxGain = Integer.MIN_VALUE;
			for(int i : attributes) {
				double gain = gain(i, examples);
				if(maxGain <= gain) {
					maxGain = gain;
					splitFeature = i;	
				}
			}
			TreeNode<String> root = new TreeNode<String>(splitFeature);
//			System.out.println("split: " + splitFeature);
			ArrayList<ArrayList<String>> attributeSubset = generateAttributes(examples);
			ArrayList<String> attributeValues = attributeSubset.get(splitFeature);
			ArrayList<String> labels = getSetOfLabels(attributeValues);
			
			for (String l : labels) {
				ArrayList<ExampleDec> newExamples = new ArrayList<>();
				for(int i = 0; i < attributeValues.size(); i++) {
					if(attributeValues.get(i).equals(l))
						newExamples.add(createExampleDec(i,attributeSubset));
				}
				attributes = removeElemIfExist(attributes, splitFeature);
				TreeNode<String> subtree = decisionTreeLearning(newExamples, attributes, parent_examples);
				subtree.setSFV(l);
				root.children.add(subtree);
			}
			return root;
		}
		
	}
	
	public int[] removeElemIfExist(int[] a, int num) {
		if(contains(a,num)) {
			int ct = 0;
			int [] arr = new int[a.length-1];
			for(int i = 0; i < a.length; i++) {
				if(a[i] != num) {
					arr[ct] = a[i];
					ct++;
				}
			}
			return arr;
			}
		return a;
		
	}

	public boolean contains(int[] a, int num) {
		// TODO Auto-generated method stub
		return binarySearch(a,num, 0, a.length-1);
	}
	
	public boolean binarySearch(int[] a, int num, int l, int r) {
		if(l > r)
			return false;
		int mid = (l+r)/2;
		if(num == a[mid])
			return true;
		if(num < a[mid])
			return binarySearch(a, num, l, mid-1);
		else 
			return binarySearch(a, num, l+1, r);
	}

	private ExampleDec createExampleDec(int index, ArrayList<ArrayList<String>> attributeSubset) {
		// TODO Auto-generated method stub
		ArrayList<String> input = new ArrayList<>();
		for(int i = 0; i < attributeSubset.size(); i++) {
			input.add(attributeSubset.get(i).get(index));
		}
		return new ExampleDec(input);
	}

	private boolean homogenous(ArrayList<ExampleDec> data) {
		// TODO Auto-generated method stub
		ArrayList<String> checker = new ArrayList<>();
		for(ExampleDec e : data) {
			if(!checker.contains(e.label))
				checker.add(e.label);
		}
		return checker.size() <= 1;
	}

	private TreeNode<String> pluralityValue(ArrayList<ExampleDec> examples) {
		// TODO Auto-generated method stub
		int demCount = 0, repCount = 0;
		for(ExampleDec e : examples) {
			if(e.label.equals("Democrat"))
				demCount++;
			else 
				repCount++;
		}
		TreeNode<String> root = new TreeNode<String>( demCount >= repCount ? "Democrat" : "Republican");
		root.probability = Math.max(demCount, repCount)/((double)demCount+repCount);
		return root;
	}
	
	public String classify(TreeNode<String> tree, ExampleDec example) {
		if(tree.children.size() == 0) {
			System.out.println(tree.label + ","+tree.probability);
			return tree.label;
		}
		else {
			LinkedList<TreeNode<String>> children = (LinkedList<TreeNode<String>>) tree.children;
			String a = "";
			for(TreeNode<String> c : children) {
				if(c.splitFeatureValue.equals(example.votes.get(tree.splitFeature)))
					a = classify(c,example);
			}
			return a;
		}
	}
}
