package ml.classifiers;

import java.util.ArrayList;

import javax.print.DocFlavor.URL;

import ml.DataSet;
import ml.Example;

public class DecisionTreeClassifier implements Classifier {
	private int depth;

	public DecisionTreeClassifier() {
	}
	
	public void setDepthLimit(int depth) {
		this.depth = depth;
	}
	
	@Override
	public void train(DataSet data) {
		ArrayList<Example> examples = data.getData();
		int numFeatures = examples.get(0).getFeatureSet().size();
		ArrayList<Double> trainingErrors = new ArrayList<Double>();
		ArrayList<Integer> usedFeatures = new ArrayList<Integer>();
		ArrayList<Double> splitLeft = new ArrayList<Double>();
		ArrayList<Double> splitRight = new ArrayList<Double>();
		for(int i = 0; i < numFeatures; i++){
			trainingErrors.add(calculateTrainingError(i, examples, usedFeatures, splitLeft));
		}
		System.out.println(trainingErrors);
		int minIndex = trainingErrors.indexOf(Collections.min(trainingErrors));
		
		DecisionTreeNode root = new DecisionTreeNode(minIndex);
		ArrayList<Integer> usedFeatures = new ArrayList<Integer>();
		ArrayList<Double> splitLeft = new ArrayList<Double>();
		ArrayList<Double> splitRight = new ArrayList<Double>();
		splitLeft.add(0);
		splitRight.add(1);
		
		usedFeatures.add(minIndex);
		if(depth == 1){
			
		}else{
		root.setLeft(trainRec(depth-1,usedFeatures,splitLeft));
		root.setRight(trainRec(depth-1,usedfeatures,splitRight));
		}
	}
	
	private DecisionTreeNode trainRec(int depth, ArrayList<Double> usedFeatures, ArrayList<Double> splitDirections){
		
	}

	@Override
	public double classify(Example example) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private int calculateTrainingError(int featureIndex, ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals) {
		//iterate through each example, through in the correct bin, bin0 and survive, bin0 and die, bin1 and survive..
		int size = examples.size();
		int bin00 = 0;
		int bin10 = 0;
		int bin01 = 0;
		int bin11 = 0;
		
		for(int i = 0; i < size; i ++){
			example = examples.get(i);
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				if(example.getFeature(featureIndex) == 0.00){
					if(example.getLabel() == 0.00){
						bin00 ++;
					}else{
					bin01 ++;
					}
				}else if (example.getLabel() == 0.00){
					bin10 ++;
				}else bin11++;	
			}
		}
		int accuracy = (Math.max(bin00, bin01) + Math.max(bin10, bin11))/size;
		return 1-accuracy;	
	}
	
	private boolean exampleIsInSubset(Example example, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		boolean retVal = true;
		for(int i = 0; i < usedFeatures.size(); i ++){
			retVal = retVal && (example.getFeature(usedFeatures.get(i)) == usedVals.get(i));
		}
		return retVal
	}
	
	
	public static void main(String[] args) {
		DecisionTreeClassifier dtc = new DecisionTreeClassifier();
		DataSet dataset = new DataSet("/Users/maddie/Documents/FALL2016/train-titanic.csv", 6);
		System.out.println(dataset.getAllFeatureIndices().toString());
		
	}

}
