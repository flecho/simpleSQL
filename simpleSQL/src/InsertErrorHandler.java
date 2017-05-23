
public class InsertErrorHandler {
	/* This object is generated when insert operation is performed.
	 * This object is initialized when insert operation is deleted.  */
	public static final int InsertNoSuchTableError = 1;
	public static final int InsertDuplicatePrimaryKeyError = 2;
	public static final int InsertReferentialIntegrityError = 3;
	public static final int InsertTypeMismatchError = 4;
	public static final int InsertColumnExistenceError = 5;	/*[#colName]*/
	public static final int InsertColumnNonNullableError = 6; /*[#colName]*/
	
	public static String nonExistingColumn;
	public static String nonNullableColumn;
	
	public static final int ON = 1;
	public static final int OFF = 0;
	
	public int[] insertTableError;
	public static String[] errorMessages;
	
	public InsertErrorHandler(){
		insertTableError = new int[7];
		errorMessages = new String[7];
		assignMessages();
		nonExistingColumn = null;
		nonNullableColumn = null;
	}
	
	public void errorDetected(int errorNum) {
		insertTableError[errorNum] = ON;		
	}
	
	public boolean isThereError() {
		for(int i=0; i<insertTableError.length; i++) {
			if(insertTableError[i] != 0)
				return true;
		}
		return false;
	}
	
	public void setNonExistingColumn(String cName) {
		nonExistingColumn = cName;
		errorMessages[5] = "Insertion has failed: " + "'" + cName + "'" + " does not exist";
	}
	
	public void setNonNullableColumn(String cName) {
		nonNullableColumn = cName;
		errorMessages[6] = "Insertion has failed: " + "'" + cName + "'" + " is not nullable";
	}
	
	public void assignMessages() {
		errorMessages[1] = "No such table";
		errorMessages[2] = "Insertion has failed: Primary key duplication";
		errorMessages[3] = "Insertion has failed: Referential integrity violation";
		errorMessages[4] = "Insertion has failed: Types are not matched";
		errorMessages[5] = "";
		errorMessages[6] = "";				
	}

	public void initialize() {

		for(int i=0; i<insertTableError.length; i++) {
			insertTableError[i] = 0;
		}
		assignMessages();
		nonExistingColumn = null;
		nonNullableColumn = null;
	}

	/* Prints the first detected error. */
	public void printError() {
		for(int i=0; i<insertTableError.length; i++) {
			if(insertTableError[i] != 0) {
				System.out.println(errorMessages[i]);
				return;
			}
		}		
	}
			
}
