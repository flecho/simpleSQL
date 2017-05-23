/*
 * After tree reaches the leaf node, it starts to evaluate the sentence.
 * The class Evaluator is defined for it.
 * 
 * */

import java.util.ArrayList;

public class Evaluator {
		

	public Evaluator(){
			
	}
	// 여기서 문제가 생김. 
	/* 여기로 pass되는 leafAlgebra는 compOperand <COMP_OP> compOperand 또는 compOperand is [not] null 이런 형식임. */
	public String evaluate(ArrayList<String> leafAlgebra, ArrayList<Data> tupleRow, WhereErrorHandler whereErrorHandler){

		/* where절의 에러 등록할 때 잘해야 함. 여기서 error번호 등록한 다음에 넘겨줘야 함. 
		 * 아니면 where error class 여기서도 만든다음에 합쳐도 되고~ (!)
		 * */
		/* 결국은 값의 비교이다. 값의 비교를 통해서 type error 등을 판별해야 한다. */
		Data actualData1 = null;
		Data actualData2 = null;
		
		if(leafAlgebra.get(1).contains("is null") || leafAlgebra.get(1).contains("is not null")){ // null comparison. 
			/*is null and is not null도 짜야되는데!*/			
			
			String operand = leafAlgebra.get(0);
			String nullInfo = leafAlgebra.get(1);
			
			// operand can be null / not null / int / date / char
			// column name이 들어올 경우, 실제 값으로 바꿔줘야 됨.
			if(typeChecker(operand).equals("column")){
				actualData1 = columnToData(operand, tupleRow);							
				operand = actualData1.getData(); // 실제 데이터를 char, date, int, null 중 하나로 바꿔야 함. 

				if(!operand.equals("null") && actualData1.getColumnType().contains("char")){
					operand = "'" + operand + "'";
				}
				// regex말고 type으로 할 수 있을 것 같은데,
				/*
				if(!operand.matches(".*\\d+.*")){ // other than 'int' or 'date'.					
					if(!operand.equals("null"))
						operand = "'" + operand + "'";						
				}
				*/								
			}
			
			if(operand.equals("null")) {
				if(!nullInfo.contains("not"))
					return "true";
				else
					return "false";
			} else { // null이 아닌 실제 value.
				if(nullInfo.contains("not"))					
					return "true";
				else {
					return "false";
				}
			}										
			// column name이 들어올 때만 값을 비교한다. column name에서 null 또는 not null이어야 비교 가능. 나머지는 다 출력. 						
			
		} else { // compOperand <COMP_OP> compOperand
			String fstOperand = leafAlgebra.get(0); // column name이 들어올 수 있음. 
			String operator = leafAlgebra.get(1);
			String sndOperand = leafAlgebra.get(2);

			// fstOperand나 sndOperand가 column값이면, tupleRow에서 실제 값을 가져온다.
			// 그 다음 tupleRow의 type을 가져온다. 
			
			// operand가 컬럼일 경우, tupleRow에서 실제 값을 데려온다. 그런 다음에야 실제적인 값을 불러올 수 있음. 
			// fstOperand를 인자로 해서 columnIndex를 얻어와야 함. 
			// name이 fstOperand로 들어오면, name의 실제 record를 tupleRow에서 데려와야 됨. 
			/* 여기 들어오는 columnName은 유효한 columnName임. where Error detect를 사전에 하기 때문에 */			
			
			if(typeChecker(fstOperand).equals("column")){
				actualData1 = columnToData(fstOperand, tupleRow);							
				fstOperand = actualData1.getData(); // 실제 데이터를 char, date, int, null 중 하나로 바꿔야 함. 

				// if(fstOperand.matches("\\d+")) // regex로만 처리하면 한계가 있지 않나;
				if(!fstOperand.equals("null") && actualData1.getColumnType().contains("char")){
					fstOperand = "'" + fstOperand + "'";
				}
							
			}
			
			if(typeChecker(sndOperand).equals("column")){
				actualData2 = columnToData(sndOperand, tupleRow);				
				sndOperand = actualData2.getData();

				if(!sndOperand.equals("null") && actualData2.getColumnType().contains("char")){
					sndOperand = "'" + sndOperand + "'";
				}

			} /* 따옴표 붙인 건 밑의 typeChecker를 위한 용도. */

			
			// 이제 실제 record의 format을 맞춰줌. 
			// null이 안들어오니까. 그래도 저장된 값이 null일 수 있음. 
			String fstType = typeChecker(fstOperand);
			String sndType = typeChecker(sndOperand);			

			
			if(fstType.equals(sndType)) {
				// legal.
			} else {
				if(fstType == "null" || sndType == "null") {
					return "unknown"; 
				}
				else {
					whereErrorHandler.errorDetected(1); // whereIncomaprable Error.					
					return "unknown";
				}
							
			}
			
			// 밑에서부터는 타입 다 똑같음. 
			/* switch문을 위한 조치 */
			
			if(fstType.equals("char")) {
				fstOperand = fstOperand.substring(1, fstOperand.length()-1);
			}

			if(sndType.equals("char")) {
				sndOperand = sndOperand.substring(1, sndOperand.length()-1);
			}
			/* 이제 operator 기준으로 실제 연산해서 true or false return해야 함. 
			 * 지금은 date일때, int일 때 구분 안 하고 짬; 
			 * 메소드 dateComparation, int comparation, char comparation를 정의하고,  나눠서 해야됨.
			 * */
			
			if(fstType.equals("int") && sndType.equals("int")){
				if(compareInt(Integer.parseInt(fstOperand), Integer.parseInt(sndOperand), operator))
					return "true";
				else 
					return "false";
			}
			
			if(fstType.equals("char") && sndType.equals("char")){
				if(compareChar(fstOperand, sndOperand, operator))
					return "true";
				else
					return "false";
			}
			
			if(fstType.equals("date") && sndType.equals("date")){
				// date값 조작할필요있어;
				if(compareInt(dateToInt(fstOperand), dateToInt(sndOperand), operator))
					return "true";
				else
					return "false";
			}

		} // else end.
		
		return "true";

}
	public boolean compareChar(String fstOperand, String sndOperand, String operator) {
		switch(operator){
		case "=":
			return fstOperand.equals(sndOperand);
			
		case "!=":
			return !fstOperand.equals(sndOperand);
		case ">=":
			if(fstOperand.compareTo(sndOperand) >= 0){
				return true;
			}
			else {
				return false;
			}
		case "<=":
			if(fstOperand.compareTo(sndOperand) <= 0)
				return true;
			else
				return false;					
		case ">":
			if(fstOperand.compareTo(sndOperand) > 0)
				return true;
			else
				return false;					
		case "<":
			if(fstOperand.compareTo(sndOperand) < 0)
				return true;
			else
				return false;					
	}
		return false; // unreachable.
	}
	
	public boolean compareInt(int fstOperand, int sndOperand, String operator){
		switch(operator) {

		case "=":
			return fstOperand == sndOperand;
		case "!=":
			return fstOperand != sndOperand;
		case ">=":
			return fstOperand >= sndOperand;
		case "<=":
			return fstOperand <= sndOperand;
		case ">":
			return fstOperand > sndOperand;
		case "<":
			return fstOperand < sndOperand;	
		}
		
		return false; // unreachable.		
	}

	public int dateToInt(String date){
		String tempDate = "";
		for(int i=0; i<date.length(); i++){
			if (!(date.charAt(i) == '-')){
				tempDate = tempDate + date.charAt(i);	
			}
		}
		return Integer.parseInt(tempDate);
	}

	public Data columnToData(String column, ArrayList<Data> tupleRow){

		  for(int i=0; i<tupleRow.size(); i++){
			if(tupleRow.get(i).getColumnName().equals(column)){ // getColumnName() 이 제대로 찾을 수 있게 조치를 취해야 함. 
				return tupleRow.get(i); 
			}
		 }
		  
		  return null;
	  }

		// null type이 왜 없지.
	  public String typeChecker(String operand) {

		  	if(operand.equals("null"))
		  		return "null";
		  
			if(operand.contains("'")) {
				return "char";
			 } else {
				if(operand.matches(".*\\d+.*")) {
					if(operand.indexOf('-') != 0 )
						return "date";
					else
						return "int";
				} else {
					// column case.
					return "column";
				}	   		
			 }
		  } 
	
}
