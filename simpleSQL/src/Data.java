import java.io.Serializable;
/*
 * Example) insert into account values(9732, 'Perryridge');
 * */

public class Data implements Serializable {

	String identifier;
	int columnIndex;
	String columnName;
	String columnType;
	boolean allowDelete;
	
	private static final long serialVersionUID = 4L;
	
	public Data(String identifier, int index, String cName, String cType){
		this.identifier = identifier;
		this.columnIndex = index;
		this.columnName = cName;
		this.columnType = cType;
		this.allowDelete = false;
	}
	
	public Data(String identifier){
		this.identifier = identifier;
		this.allowDelete = false;
	}
	
	public void changeIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	public void setAllowDelete(){
		this.allowDelete = true;
	}
	
	public boolean getAllowDelete(){
		return this.allowDelete;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public String getColumnType() {
		return this.columnType;
	}
	
	public void setColumnName(String newName) {
		this.columnName = newName;
	}
	
	public int getColumnIndex() {
		return this.columnIndex;
	}
	// 나중에 조작하기 편하게.
	/* type inference를 해서 type return 하는 게 필요.*/
	
	public String getData() {
		return this.identifier;
	}
	
	public String getDataType(){
				
		if(identifier.equals("null")) {
			return "null";
		}
		else if(identifier.contains("'")) { 
			return "char(" + identifier.length() +")";
		}
		else if(identifier.matches(".*\\d+.*")){
			if(identifier.contains("-")){
				if(identifier.indexOf('-') != 0){
					return "date";
				}				
			}
			return "int";
		} else {
			return "int";
		}
	}
												
}
