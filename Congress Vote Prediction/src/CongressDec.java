import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CongressDec {
	public static void main(String[] args) throws FileNotFoundException {
		String fileName1 = args[0];
		File file1 = new File(fileName1);
		String fileName2 = args[1];
		File file2 = new File(fileName2);
		DecisionTreeConstruction dtc = new DecisionTreeConstruction(file1, file2);
		ArrayList<ExampleDec> testing_examples = dtc.testing_examples;
		
		
		ArrayList<ExampleDec> parent_examples = new ArrayList<>();
		TreeNode<String> root = dtc.decisionTreeLearning(dtc.examples, dtc.attributes, parent_examples);
		for(ExampleDec e : testing_examples) {
			dtc.classify(root, e);
		}
		
	}
}
