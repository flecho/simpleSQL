import java.io.Serializable;
import java.util.ArrayList;

/* 2010-12794 Joonsang Jo 
 * 
 * 	The 'Table' object is designed to be inserted to Berkeley DB as a value.
 * 	The data it contains are as follows:
 * 	
 * 	- name: table name
 * 	- columnNT: the list of column names, types, and other infomation.(ps. Explanation about columnInfo class is described below.)
 * 	- referencedBy: the list that contains names of other tables which reference this table.
 * 	- referringTo: the list that contains names of other tables which this table references.
 * 	- preemptDoublePrimaryKey: the constant that works like a boolean value which is designed for
 * 	 preempting declaring primary key more than once.
 *  
 * 	The explanations about methods are added above each method. (FYI, explanations about simple methods are omitted.)    
 * */

public class Table implements Serializable {

	String name;
	Record record;
	ArrayList<columnInfo> columnNT;
	ArrayList<String> referencedBy;
	ArrayList<String> referringTo;
	ArrayList<String> primaryKeys;
	ArrayList<String> foreignKeys;
	ArrayList<String> anotherForeignKeys;
	ArrayList<String> mappedKeys; // foreign key와 map되는 referring table의 columnName
	public int preemptDoublePrimaryKey;
	
	private static final long serialVersionUID = 1L;
	
	public Table(){

		columnNT = new ArrayList<columnInfo>();
		referencedBy = new ArrayList<String>();
		referringTo = new ArrayList<String>();
		primaryKeys = new ArrayList<String>();
		foreignKeys = new ArrayList<String>();
		anotherForeignKeys = new ArrayList<String>();
		mappedKeys = new ArrayList<String>();
		record = new Record();
		preemptDoublePrimaryKey = 0;
	
	}
	
	public Record getRecord() {
		return record;
	}
	
	public ArrayList<columnInfo> getColumnNT() {
		return columnNT;
	}

	public void setColumnNT(ArrayList<columnInfo> newColumnNT) {
		this.columnNT = newColumnNT;
	}
	
	public int getColumnNTLength() {
		return columnNT.size();
	}
	
	public int getColumnIndex(String cName) {
		for(int i=0; i<getColumnNTLength(); i++) {
			if(columnNT.get(i).getColumnName().equals(cName))
				return i;
		}
		return -1; // this line is unreachable, since columns include test is performed in advance.		
	}

	public columnInfo getColumnByName(String cName) {
		for(int i=0; i<getColumnNTLength(); i++) {
			if(columnNT.get(i).getColumnName().equals(cName))
				return columnNT.get(i);
		}
		return null; // this line is unreachable, since columns include test is performed in advance.		
	}
	
	public void setRecordConstraint() {
		record.setColumnConstraint(columnNT);		
	}
	
	public void setTableName(String tableName) {
		name = tableName;		
	}

	public String getTableName() {
		return name;
	}
	
	/* Check whether there is a duplicate columns in a table. Return true if there is. */
	public boolean isThereDupColumn(){
		
		for(int i=0; i<columnNT.size(); i++) {

			String temp = columnNT.get(i).getColumnName();
			
			for(int j=i+1; j<columnNT.size(); j++) {
				if(temp.equals(columnNT.get(j).getColumnName()))
					return true;
			}	
		}
		return false;
	}
		
	/* Check whether this table contains the given column. Return true if it does. */
	public boolean isColumnInTable(String columnName) {
		
		for(int i=0; i<columnNT.size(); i++){
			if(columnNT.get(i).getColumnName().equals(columnName))
				return true;
		}
		
		return false;
	}
	
	public boolean areColumnsInTable(ArrayList<String> columnList) {
		for(int i=0; i<columnList.size(); i++) {
			if(!isColumnInTable(columnList.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public String getColumnNotInTable(ArrayList<String> columnList) {
		for(int i=0; i<columnList.size(); i++) {
			if(!isColumnInTable(columnList.get(i))){
				return columnList.get(i);
			}
		}
		return null;
	}


	/* Add tables referencing this table to the 'referencedBy' arrayList. */
	public void connectRefTableInfo(String tableName) {

		boolean notDuplicate = true;
		
		for (int i=0; i<referencedBy.size(); i++){
			if(referencedBy.get(i).equals(tableName)) {
					notDuplicate = false;
					break;
			}
		}
		
		if(notDuplicate) {			
			referencedBy.add(tableName);
		}
	}
	
	public boolean isReferencedBy() {
		if(referencedBy.size() == 0)
			return false;
		else
			return true;
	}

	/* If a certain table which is referencing this table is dropped from the database, 
	 * then that table's name is also removed in the list.  */	
	public void deleteReferencedTable(String tableName) {
		for(int i=0; i<referencedBy.size(); i++){
			if(referencedBy.get(i).equals(tableName)){				
				referencedBy.remove(i);
				break;
			}				
		}
	}
	/* Add tables, which this table is referencing, to the 'referringTo' arrayList */
	public void connectReferringInfo(String tableName) {

		boolean notDuplicate = true;
		
		for (int i=0; i<referringTo.size(); i++){
			if(referringTo.get(i).equals(tableName)) {
					notDuplicate = false;
					break;
			}
		}
		
		if(notDuplicate) {
			referringTo.add(tableName);
		}
	}
	
	public boolean isReferringTo() {
		if(referringTo.size() == 0)
			return false;
		else
			return true;
	}
	
	public ArrayList<String> getReferringList() {		
		return referringTo;
	}

	public ArrayList<String> getReferencedList() {		
		return referencedBy;
	}
	
	public void connectColumnInfo(columnInfo e){
		columnNT.add(e);
	}
	
	public int getColumnLength(){
		return columnNT.size();
	}
	
	public String getColumnType(String columnName) {
		
		for(int i=0; i<columnNT.size(); i++){
			if(columnNT.get(i).getColumnName().equals(columnName))
				return columnNT.get(i).getColumnType();
		}

		return ""; // If there is no column name, this line is unreachable but needed in order to be compiled.  
	}
	
	public boolean isPrimaryKey(String columnName) {

		for(int i=0; i<columnNT.size(); i++){
			if(columnNT.get(i).getColumnName().equals(columnName)){
				return columnNT.get(i).getPrimaryKey();					
			}			
		}
		
		return false;  //columnName 못 찾았을 경우..later to be modified.
	}
	
	/* Return i-th column info.*/
	public columnInfo getColumnInfo(int index){
		return columnNT.get(index);		
	}
	
	/* Set primary keys of this table using given arrayList.*/
	public void setPrimaryKeys(ArrayList<String> temp) {
		
		primaryKeys = temp;
		
		for(int i=0; i< temp.size(); i++) {
			
			for(int j=0; j<columnNT.size(); j++) {
			
				if(columnNT.get(j).columnName.equals(temp.get(i))){
					columnNT.get(j).setPrimaryKey();
					break;
				}
			}
		}
	}
	
	public ArrayList<String> getPrimaryKeys() {
		return primaryKeys;
	}
	
	/* Get a list of referenced lists of columns, and check whether these are the primary key of the target table.
	 * If it is a primary key, then return true, otherwise return false. 
	 * */
	public boolean arePrimaryKeys(ArrayList<String> temp) {

		for(int i=0; i<temp.size(); i++){
			if(!primaryKeys.contains(temp.get(i))){
				return false;
			} 			
		}
		
		if(primaryKeys.size() == temp.size())
			return true;
		else
			return false;
	}
		
	public void setForeignKeys(ArrayList<String> temp) {
		for(int i=0; i< temp.size(); i++) {
			
			for(int j=0; j<columnNT.size(); j++) {
			
				if(columnNT.get(j).columnName.equals(temp.get(i))){
					columnNT.get(j).setForeignKey();
					break;
				}
			}
		}
	}
	
	public String getWhichColumnsReferTo(String tableName){
		for(int i=0; i<foreignKeys.size(); i++){
			if(foreignKeys.get(i).contains("[" + tableName +"]")){
				return foreignKeys.get(i);
			}
		}
		return null;
	}
		
	/* newly updated 
	 * ForeignKeySets의 자료구조 (arrayList)
	 * [referringTableName]this.col1/this.col2/this.col3 --> [referringTableName]this.col1/this.col2/this.col3 --> ...
	 * mappedFKeySets의 자료구조
	 * [referringTableName]that.col1/that.col2/that.col3
	 * */
	
	public void setForeignKeySets(ArrayList<String> temp, String referringTableName) {
		// temp의 길이가 2이상이면 "/"로 연결한 String을 ForeignKeys에 저장.
		String tempSet = "[" + referringTableName + "]";
		if(temp.size() >= 2) {
			for(int i=0; i<temp.size(); i++) {
				tempSet = tempSet + temp.get(i) + "/";
			}
			// 마지막 slash 없애고, 저장.
			tempSet = tempSet.substring(0, tempSet.length()-1);			
		} else {
			tempSet = tempSet + temp.get(0);
		}
		
		foreignKeys.add(tempSet);		
	}
	
	public ArrayList<String> getForeignKeySets() {
		return foreignKeys;
	}
	
	public void setMappedKeySets(ArrayList<String> temp, String referringTableName) {
		// temp의 길이가 2이상이면 "/"로 연결한 String을 ForeignKeys에 저장.
		String tempSet = "[" + referringTableName + "]";
		if(temp.size() >= 2) {
			for(int i=0; i<temp.size(); i++) {
				tempSet = tempSet + temp.get(i) + "/";
			}
			// 마지막 slash 없애고, 저장.
			tempSet = tempSet.substring(0, tempSet.length()-1);			
		} else {
			tempSet = tempSet + temp.get(0);
		}		
		mappedKeys.add(tempSet);		
	}
	
	public ArrayList<String> getMappedKeySets() {
		return mappedKeys;
	}

	public String getForeignKeySet(String tableName) {
		for(int i=0; i<foreignKeys.size(); i++){
			if(foreignKeys.get(i).contains("[" + tableName +"]")){
				String mappedKey = foreignKeys.get(i);
				for(int j=0; j<mappedKey.length(); j++) {
					if(mappedKey.charAt(j) == ']'){
						mappedKey = mappedKey.substring(j+1, mappedKey.length());
						return mappedKey;
					}
				}
				
			}
		}
		
		return null; // 불리는 상황이 null이 호출될 수 없는 상황임. 
	}
		
	/* 처리하기 편하게 가공된 형태로, colName1/colName2/colName3/... 이런 식의 String을 return한다.*/
	public String getMappedKeySet(String tableName) {
		for(int i=0; i<mappedKeys.size(); i++){
			if(mappedKeys.get(i).contains("[" + tableName +"]")){
				String mappedKey = mappedKeys.get(i);
				for(int j=0; j<mappedKey.length(); j++) {
					if(mappedKey.charAt(j) == ']'){
						mappedKey = mappedKey.substring(j+1, mappedKey.length());
						return mappedKey;
					}
				}
				
			}
		}
		
		return null; // 불리는 상황이 null이 호출될 수 없는 상황임. 
	}
	/* "id/int/salary" ---> "1/4/2" 하는 함수. */
	public String getMappedIndex(String mappedKeySet){
		
		String[] mappedKey = mappedKeySet.split("/");
		String encodedString = "";
		for(int i=0; i<mappedKey.length; i++){
			encodedString = encodedString + Integer.toString(getColumnIndex(mappedKey[i])) + "/";
		}
		encodedString = encodedString.substring(0, encodedString.length()-1);		
		
		return encodedString;
	}

	public void setAnotherFSets(ArrayList<String> temp) {
		// temp의 길이가 2이상이면 "/"로 연결한 String을 ForeignKeys에 저장.
		String tempString = "";
		for(int i=0; i<temp.size(); i++) {
			tempString = tempString + temp.get(i) + "/";
		}
		tempString = tempString.substring(0, tempString.length()-1);
		
		anotherForeignKeys.add(tempString);
	}

	public ArrayList<String> getAnotherFSets() {
		return anotherForeignKeys;
	}
	
	
	/* column을 순회하면서, 모두 nullable인지 확인한다. 모두 nullable일 경우, return true, otherwise, return false. */
	public boolean isAllNullable(String foreignKeys) {

		// 앞의 tableName은 날려줘야 함.
		for(int j=0; j<foreignKeys.length(); j++) {
			if(foreignKeys.charAt(j) == ']') {
				foreignKeys = foreignKeys.substring(j+1, foreignKeys.length());
				break;
			}
		}
		
		String[] foreignKey = foreignKeys.split("/");
				
		for(int i=0; i<foreignKey.length; i++){
			if(getColumnByName(foreignKey[i]).getNotNull()) {
				return false; // 여기에 들어오면 무조건 하나는 not null constraint가 있는 것임. 
			}
		}
		return true;
	}
	
	public ArrayList<Integer> getColumnIndexList(ArrayList<String> columnList){
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		
		for(int i=0; i<columnList.size(); i++) {
			indexList.add(getColumnIndex(columnList.get(i)));
		}
		return indexList;
	}
	
	public ArrayList<String> getColumnNamesOnly() {
		ArrayList<String> tempArray = new ArrayList<String>();
		
		for(int i=0; i<columnNT.size(); i++){
			String cName = columnNT.get(i).getColumnName();
			for(int j=0; j<cName.length(); j++) {
				if(cName.charAt(j) == '.'){
					tempArray.add(cName.substring(j+1, cName.length()));
					break;
				}				
			}			
		}		
		return tempArray;
	}

	public ArrayList<String> getColumnNamesFully() {
		ArrayList<String> tempArray = new ArrayList<String>();
		
		for(int i=0; i<columnNT.size(); i++){
			String cName = columnNT.get(i).getColumnName();
			tempArray.add(cName);
		}				
	
		return tempArray;
	}
	
	
	public String getColumnNameFully(String cName) {
				
		for(int i=0; i<columnNT.size(); i++){
			String originalColumnName = columnNT.get(i).getColumnName();
			String tempName;
			
			for(int j=0; j<originalColumnName.length(); j++) {
				if(originalColumnName.charAt(j) == '.') {
					tempName = originalColumnName.substring(j+1, originalColumnName.length());
					if(tempName.equals(cName)){
						return originalColumnName;
					}
				}
			}
						
		}		
		return null; // unreachable. 
	}
	
	
	/* This method initializes all member variables' values. 
	 * This method is needed since only one table object is used and recycled in the program */	
	public void recycle() {
		name = null;
		record = new Record();
		columnNT = new ArrayList<columnInfo>();
		referencedBy = new ArrayList<String>();
		referringTo = new ArrayList<String>();
		primaryKeys = new ArrayList<String>();
		foreignKeys = new ArrayList<String>();
		anotherForeignKeys = new ArrayList<String>();
		mappedKeys = new ArrayList<String>();
		preemptDoublePrimaryKey = 0;
	}
		
}

/*
 * The 'columnInfo' object is designed to contain necessary information about columns.
 * The data it contains are the information shown when "DESC ['#table name']" operation is performed.
 * All the methods below are simple getters and setters.
 * */

class columnInfo implements Serializable{
	String columnName;
	String columnType;
	boolean notNull;
	boolean PrimaryKey;
	boolean ForeignKey; 
	
	private static final long serialVersionUID = 2L;

	public columnInfo() {
		notNull = false;
		PrimaryKey = false;
		ForeignKey = false;
	}
	
	public columnInfo(String cName, String cType) {
		columnName = cName;
		columnType = cType;
		PrimaryKey = false;
		ForeignKey = false;
		notNull = false;
	}
	
	public int getCharColumnLength() { // 이렇게 하면 안되지 븅신아.
		int startIndex = 0;
		int endIndex = 0;
						
		if(columnType.contains("char")) {
			
			for(int i=0; i<columnType.length(); i++) {
				if(columnType.charAt(i) == '('){
					startIndex = i+1;
				}
				if(columnType.charAt(i) == ')'){
					endIndex = i;
				}
			}			
			return Integer.parseInt(columnType.substring(startIndex, endIndex));
		}		
			return -1;
	}
		
	public String getColumnName() {	
		return columnName;
	}
	
	public void setColumnName(String newName) {	
		this.columnName = newName;
	}
	
	public String getColumnType() {	
		return columnType;
	}
	
	public void setPrimaryKey() {
		PrimaryKey = true;
		notNull = true; // automatically following condition.
	}
	
	public boolean getPrimaryKey() {
		return PrimaryKey;
	}

	public void setForeignKey() {
		ForeignKey = true;
	}
	
	public boolean getForeignKey() {
		return ForeignKey;
	}
	
	public void setNotNull() {
		notNull = true;
	}
	
	public boolean getNotNull() {
		return notNull;
	}
	
	public void setColumnInfo(String cName, String cType) {
		columnName = cName;
		columnType = cType;
	}
	
}