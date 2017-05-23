/*
 *  The following interface and classes are defined for effectively evaluating complex comparative sentences. 
 * 
 * */

import java.util.ArrayList;


public interface BooleanExpression {
	public String interpret(ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler);
}

abstract class NotLeafNode implements BooleanExpression {

	BooleanExpression leftChild;
	BooleanExpression rightChild;
		
	public void setLeft(BooleanExpression left){
		leftChild = left;		
	}
	
	public void setRight(BooleanExpression right){
		rightChild = right;		
	}
}

class And extends NotLeafNode {
	
	public String interpret(ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler) {
		
		String leftResult = leftChild.interpret(tupleRow, whereErrorHandler);
		String rightResult = rightChild.interpret(tupleRow, whereErrorHandler);

		if(leftResult.equals("unknown") && rightResult.equals("unknown")) {
			return "unknown";						
		}
		// 이부분 마저 짜기. 
		if(leftResult.equals("unknown") && rightResult.equals("true")) {
				return "unknown";						
		}
	
		if(leftResult.equals("unknown") && rightResult.equals("false")) {
			return "false";						
		}

		 
		if(leftResult.equals("true") && rightResult.equals("unknown")) {
			return "unknown";						
		}
	
		if(leftResult.equals("false") && rightResult.equals("unknown")) {
			return "false";						
		}

		if(leftResult.equals("true") && rightResult.equals("true")) {
			return "true";
		} else
			return "false";
	}		
}

class Or extends NotLeafNode {

	public String interpret(ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler) {
		String leftResult = leftChild.interpret(tupleRow, whereErrorHandler);
		String rightResult = rightChild.interpret(tupleRow, whereErrorHandler);
		
		if(leftResult.equals("unknown") && rightResult.equals("unknown")) {
			return "unknown";						
		}
		// 이부분 마저 짜기. 
		if(leftResult.equals("unknown") && rightResult.equals("true")) {
				return "true";						
		}
	
		if(leftResult.equals("unknown") && rightResult.equals("false")) {
			return "unknown";						
		}

		 
		if(leftResult.equals("true") && rightResult.equals("unknown")) {
			return "true";						
		}
	
		if(leftResult.equals("false") && rightResult.equals("unknown")) {
			return "unknown";						
		}

		if(leftResult.equals("true") || rightResult.equals("true")) {
			return "true";
		} else
			return "false";
	
	}	
}

// Not은 오른쪽 Child하나만 갖는다고 생각.
class Not extends NotLeafNode {

	public String interpret(ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler) {
		String result = rightChild.interpret(tupleRow, whereErrorHandler); 
		
		if(result.equals("unknown"))
			return "unknown";
		else if(result.equals("true"))
			return "false";
		else
			return "true";
	}	
}

/* Leaf 단계에서는 boolean 값을 직접 판정해서 return해야 함. */
class LeafExpression implements BooleanExpression {
	boolean value;
	ArrayList<String> leafAlgebra;
	Evaluator evaluator;
	
	public LeafExpression() { // 식이 들어옴.  		
		evaluator = new Evaluator();
	}
	
	public void setLeafAlgebra(ArrayList<String> leafAlgebra){
		this.leafAlgebra = leafAlgebra;
	} 
	
	public String interpret(ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler) {
		// evaluate whether true or false given tupleRow.
		return evaluator.evaluate(leafAlgebra, tupleRow, whereErrorHandler);
		//return this.value;
	}	
} 

