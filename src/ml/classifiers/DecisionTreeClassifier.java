package ml.classifiers;

import java.util.ArrayList;
import java.util.Collections;

import javax.print.DocFlavor.URL;

import ml.DataSet;
import ml.Example;

public class DecisionTreeClassifier implements Classifier {
	private int depth;
	private DecisionTreeNode classifier;
	public DecisionTreeClassifier() {
	}
	
	public void setDepthLimit(int depth) {
		this.depth = depth;
		
	}
	
	@Override
	public void train(DataSet data) {
        ArrayList<Example> examples = data.getData();
        ArrayList<Integer> usedFeatures = new ArrayList<Integer>();
        ArrayList<Double> splitDirection = new ArrayList<Double>();
        DecisionTreeNode root = trainRec(examples, depth, usedFeatures, splitDirection);
        System.out.println(root.treeString());
        classifier = root;
    }
	
	private DecisionTreeNode trainRec( ArrayList<Example> examples, int depth, ArrayList<Integer> usedFeatures, ArrayList<Double> splitDirection){
		 
		if(isInSameClass(examples, usedFeatures, splitDirection)){
	        	return new DecisionTreeNode(getPrediction(examples, usedFeatures, splitDirection));
	        }
		if(haveSameFeatureValues(examples, usedFeatures, splitDirection)){
			
			return new DecisionTreeNode(getPrediction(examples, usedFeatures, splitDirection));
		}
		if(noDataInSubset(examples, usedFeatures, splitDirection)){
			usedFeatures.remove(usedFeatures.size()-1);
			splitDirection.remove(splitDirection.size()-1);
			return new DecisionTreeNode(getPrediction(examples, usedFeatures, splitDirection));
		}
		
		int numFeatures = examples.get(0).getFeatureSet().size();
        ArrayList<Double> trainingErrors = new ArrayList<Double>();
        ArrayList<Double> splitLeft = new ArrayList<Double>();
        ArrayList<Double> splitRight = new ArrayList<Double>();
        
        for (int i = 0; i < numFeatures; i++) {
            trainingErrors.add((double) calculateTrainingError(i, examples, usedFeatures, splitDirection));
        }
        
        System.out.println(trainingErrors);
        
        int minIndex = trainingErrors.indexOf(Collections.min(trainingErrors));
        trainingErrors.remove(minIndex);
        //make sure that we don't split on the same feature twice
        DecisionTreeNode root = new DecisionTreeNode(minIndex);
        
        splitLeft = (ArrayList<Double>) splitDirection.clone();
        splitLeft.add(0.0);
        splitRight = (ArrayList<Double>) splitDirection.clone();
        splitRight.add(1.0);
        usedFeatures.add(minIndex);
        ArrayList<Integer> usedFeatures1 = (ArrayList<Integer>) usedFeatures.clone();
       
        if (depth == 1 || usedFeatures.size() == numFeatures) {
  
            root.setLeft(new DecisionTreeNode(getPrediction(examples, usedFeatures, splitLeft)));
            root.setRight(new DecisionTreeNode(getPrediction(examples, usedFeatures1, splitRight)));
        } else {
            root.setLeft(trainRec(examples, depth - 1, usedFeatures, splitLeft));
            root.setRight(trainRec(examples, depth - 1, usedFeatures1, splitRight));
        }
		return root;
	}
	
	private boolean noDataInSubset(ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		for(Example example:examples){
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				return false;
			}
		}
		return true;
	}
	
	
	private boolean haveSameFeatureValues(ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		boolean retVal = true;
		int size = examples.size();
		Example previous = null;
		for(int i = 0; i < size; i++){
			Example example = examples.get(i);
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				if(previous == null){
					previous = example;
				}else{
					retVal = previous.equalFeatures(example);
				}
			}
			
			
		}
		return retVal;
	}
	private boolean isInSameClass(ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		double count = 0;
		double sum = 0;
		for(Example example:examples){
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				count ++;
				sum =+ example.getLabel();
				
			}
		}
		return (sum == 0) || (sum == count);
	}
	private double getPrediction(ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		int size = examples.size();
		//for each example with the above sets of features count the number that survive and the number that did not
		double totalCount = 0;
		double survivors = 0;
		
		for(int i = 0; i < size; i++){
			Example example = examples.get(i);
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				totalCount ++;
				survivors += example.getLabel();
			}
		}
		if(survivors > totalCount - survivors){
			return 1.0;
		}
		else{ 
			return 0.0;
		}
		
	}

	@Override
	public double classify(Example example) {
		return classifyRec(example, classifier);
	}
	
	private double classifyRec(Example example, DecisionTreeNode tree){
		if(tree.isLeaf()){
			return tree.prediction();
		}
		if(example.getFeature(tree.getFeatureIndex()) == 0.0){
			return classifyRec(example, tree.getLeft());
		}
		return classifyRec(example, tree.getRight());
	}
	
	private double calculateTrainingError(int featureIndex, ArrayList<Example> examples, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals) {
		//iterate through each example, through in the correct bin, bin0 and survive, bin0 and die, bin1 and survive..
		int size = examples.size();
		double bin00 = 0;
		double bin10 = 0;
		double bin01 = 0;
		double bin11 = 0;
		double count = 0.0;
		
		if(usedFeatures.contains(featureIndex)){
			return 2.0;
		}
		for(int i = 0; i < size; i ++){
			
			Example example = examples.get(i);
			if(exampleIsInSubset(example, usedFeatures, usedVals)){
				count ++;
				
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
		System.out.println(bin00 + " " + bin01 + " " + bin10 + " "+ "" + bin11);
		
		double accuracy = (Math.max(bin00, bin01) + Math.max(bin10, bin11))/count;
		return 1-accuracy;	
	}
	
	private boolean exampleIsInSubset(Example example, ArrayList<Integer> usedFeatures, ArrayList<Double> usedVals){
		
		boolean retVal = true;
		for(int i = 0; i < usedFeatures.size(); i ++){
			retVal = retVal && (example.getFeature(usedFeatures.get(i)) == usedVals.get(i));
		}
		return retVal;
	}
	
	
	public static void main(String[] args) {
		
		DecisionTreeClassifier dtc = new DecisionTreeClassifier();
		DataSet dataset = new DataSet("/Users/mollydriscoll/Documents/Pomona/fall_16/Machine Learning/ml/train-titanic.csv", 6);
		dtc.setDepthLimit(20);
		dtc.train(dataset);
		
	}

}
