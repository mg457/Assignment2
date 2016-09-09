package ml.classifiers;

import java.util.ArrayList;
import java.util.Collections;

import javax.print.DocFlavor.URL;

import ml.DataSet;
import ml.Example;

public class DecisionTreeClassifier implements Classifier {
	
	int depth;

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
			trainingErrors.add((double) calculateTrainingError(i, examples, usedFeatures, splitLeft));
		}
		System.out.println(trainingErrors);
		int minIndex = trainingErrors.indexOf(Collections.min(trainingErrors));
		
		DecisionTreeNode root = new DecisionTreeNode(minIndex);
		
		splitLeft.add(0.0);
		splitRight.add(1.0);
		
		usedFeatures.add(minIndex);
		if(depth == 1){
			
		}else{
		root.setLeft(trainRec(depth-1,usedFeatures,splitLeft));
		root.setRight(trainRec(depth-1,usedFeatures,splitRight));
		}
	}
	
	private DecisionTreeNode trainRec(int depth, ArrayList<Integer> usedFeatures, ArrayList<Double> splitDirections){
		return new DecisionTreeNode(0.0);
	}



	@Override
	public double classify(Example example) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private double getPrediction(ArrayList<Integer> featuresPredicting, DataSet data) {
		int survived = 0;
		ArrayList<Example> passengers = data.getData();
		for(Example p : passengers) {
			for(int i : featuresPredicting) {
				
			}
		}
		
		return 0.0;
	}
	
	private int calculateTrainingError(int featureIndex, ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals) {
		//iterate through each example, through in the correct bin, bin0 and survive, bin0 and die, bin1 and survive..
		int size = examples.size();
		int bin00 = 0;
		int bin10 = 0;
		int bin01 = 0;
		int bin11 = 0;
		
		for(int i = 0; i < size; i ++){
			if(examples.get(i).getFeature(featureIndex) == 0.00){
				if(examples.get(i).getLabel() == 0.00){
					bin00 ++;
				}else{
				bin01 ++;
				}
			}else if (examples.get(i).getLabel() == 0.00){
				bin10 ++;
			}else bin11++;
				
		}
		int accuracy = (Math.max(bin00, bin01) + Math.max(bin10, bin11))/size;
		return 1-accuracy;	
	}
	
	
	public static void main(String[] args) {
		DecisionTreeClassifier dtc = new DecisionTreeClassifier();
		DataSet dataset = new DataSet("/Users/maddie/Documents/FALL2016/train-titanic.csv", 6);
		System.out.println(dataset.getAllFeatureIndices().toString());
		
	}

}
