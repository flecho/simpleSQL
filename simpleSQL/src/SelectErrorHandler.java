
public class SelectErrorHandler {

	public static final int SelectTableExistenceError = 1;
	public static final int SelectColumnResolveError = 2;
	
	public static String nonExistingTable;
	public static String nonResolvingColumn;
	
	public static final int ON = 1;
	public static final int OFF = 0;
	
	public int[] selectError;
	public static String[] errorMessages;
	
	public SelectErrorHandler(){
		selectError = new int[3];
		errorMessages = new String[3];
		assignMessages();
		nonExistingTable = null;
		nonResolvingColumn = null;
	}
	
	public void errorDetected(int errorNum) {
		selectError[errorNum] = ON;		
	}
	
	public boolean isThereError() {
		for(int i=0; i<selectError.length; i++) {
			if(selectError[i] != 0)
				return true;
		}
		return false;
	}
	
	public void setNonExistingTable(String tableName) {
		nonExistingTable = tableName;
		errorMessages[1] = "Selection has failed: " + "'" + tableName + "'" + " does not exist";
	}
	
	public void setNonResolvingColumn(String columnName) {
		nonResolvingColumn = columnName;
		errorMessages[2] = "Selection has failed: fail to resolve " + "'" +columnName + "'";
	}
	
	public void assignMessages() {
		errorMessages[1] = "";
		errorMessages[2] = "";						
	}

	/* Prints the first detected error. */
	public void printError() {
		for(int i=0; i<selectError.length; i++) {
			if(selectError[i] != 0) {
				System.out.println(errorMessages[i]);
				return;
			}
		}		
	}
	
}
