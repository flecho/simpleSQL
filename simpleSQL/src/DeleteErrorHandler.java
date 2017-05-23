
public class DeleteErrorHandler {

	public static final int DeleteNoSuchTableError = 1;
	
	public static final int ON = 1;
	public static final int OFF = 0;
	
	public int[] deleteTableError;
	public static String[] errorMessages;
	
	public void errorDetected(int errorNum) {
		deleteTableError[errorNum] = ON;		
	}
	
	public DeleteErrorHandler() {
		deleteTableError = new int[2];
		errorMessages = new String[2];	
		assignMessages();
	}
	
	public void assignMessages() {
		errorMessages[1] = "No such table";					
	}
	
	public boolean isThereError() {
		for(int i=0; i<deleteTableError.length; i++) {
			if(deleteTableError[i] != 0)
				return true;
		}
		return false;
	}
	
	
	public void printError() {
		for(int i=0; i<deleteTableError.length; i++) {
			if(deleteTableError[i] != 0) {
				System.out.println(errorMessages[i]);
				return;
			}
		}		
	}
}
