/* 2010-12794 Joonsang Jo 
 * 
 * 	The 'CreateErrorHandler' object is designed to manage errors related to 'create table.'
 * 	The errors are managed like flags in C programming language.
 * 	If a certain error occurs, the error information is written to member array 'createTableError' by 'errorDectected(int errorNum)' method.
 *  Like a flag, the index of 'createTableError' array represents different kinds of errors.   
 *  If the value in the array changes to "1", it means that the error occurred. 
 *  If the value in the array stays to "0", it means that the error doesn't happen.    
 * */


public class CreateErrorHandler {
	
	public static final int DuplicateColumnDefError = 1;
	public static final int DuplicatePrimaryKeyDefError = 2;
	public static final int ReferenceTypeError = 3;
	public static final int ReferenceNonPrimaryKeyError = 4;
	public static final int ReferenceColumnExistenceError = 5;
	public static final int ReferenceTableExistenceError = 6;
	public static final int NonExistingColumnDefError = 7;
	public static final int TableExistenceError = 8;
	public static final int CharLengthError = 9;
	
	public static final int ON = 1;
	public static final int OFF = 0;	
	public int[] createTableError;
	public static String[] errorMessages;
	public static String notExistingColName;
	
	public CreateErrorHandler() {
		initCreateTableError();		
	}

	public void initCreateTableError() {

		createTableError = new int[10];
		errorMessages = new String[10];
		initialize(); 			
		assignMessages();
	}
	/* Initialize error information. This method is called each time a certain query is processed. */
	public void initialize() {

		for(int i=0; i<createTableError.length; i++) {
			createTableError[i] = 0;
		}		
	}
	/* Error messages. Note that 7th error is written as blank. */
	public void assignMessages() {
		errorMessages[1] = "Create table has failed: column definition is duplicated";
		errorMessages[2] = "Create table has failed: primary key definition is duplicated";
		errorMessages[3] = "Create table has failed: foreign key references wrong type";
		errorMessages[4] = "Create table has failed: foreign key references non primary key column";
		errorMessages[5] = "Create table has failed: foreign key references non existing column";
		errorMessages[6] = "Create table has failed: foreign key references non existing table";
		errorMessages[7] = "";
		errorMessages[8] = "Create table has failed: table with the same name already exists";
		errorMessages[9] = "Char length should be over 0";		
	}
	
	/* Since 7th error requires column name, the column name is given through this method. */
	public void setNotExistingColName(String cName){
		notExistingColName = cName;
		errorMessages[7] = "Create table has failed: " + "'" + notExistingColName + "'" + " does not exists in column definition";
	}
	
	/* If an error occurs, this method turns on the switch. */
	public void errorDetected(int errorNum) {
		createTableError[errorNum] = ON;		
	}

	/* The method tells whether there happened an error while processing the query. */
	public boolean isThereError() {
		for(int i=0; i<createTableError.length; i++) {
			if(createTableError[i] != 0)
				return true;
		}
		return false;
	}
	
	/* Prints the first detected error. */
	public void printError() {
		for(int i=0; i<createTableError.length; i++) {
			if(createTableError[i] != 0) {
				System.out.println(errorMessages[i]);
				return;
			}
		}		
	}
	
}
	






