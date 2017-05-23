
public class WhereErrorHandler {

	public static final int WhereIncomparableError = 1;
	public static final int WhereTableNotSpecified = 2;
	public static final int WhereColumnNotExist = 3;
	public static final int WhereAmbiguousReference = 4;

	public static final int ON = 1;
	public static final int OFF = 0;
	
	public int[] whereError;
	public static String[] errorMessages;
	
	public WhereErrorHandler(){
		whereError = new int[5];
		errorMessages = new String[5];
		assignMessages();		
	}
	
	public void errorDetected(int errorNum) {
		whereError[errorNum] = ON;		
	}
	
	public boolean isThereError() {
		for(int i=0; i<whereError.length; i++) {
			if(whereError[i] != 0)
				return true;
		}
		return false;
	}
	
	public void assignMessages() {
		errorMessages[1] = "Where clause try to compare incomparable values";
		errorMessages[2] = "Where clause try to reference tables which are not specified";
		errorMessages[3] = "Where clause try to reference non existing column";
		errorMessages[4] = "Where clause contains ambiguous reference";				
	}

	/* Prints the first detected error. */
	public void printError() {
		for(int i=0; i<whereError.length; i++) {
			if(whereError[i] != 0) {
				System.out.println(errorMessages[i]);
				return;
			}
		}		
	}
	
}
