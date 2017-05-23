import java.util.ArrayList;
import java.io.Serializable;

/* dataList를 쓸 때, row 단위로 해야 될 것 같다. 지금은 그냥 이어 붙이다보니 문제가 발생한다. */

public class Record implements Serializable {

	ArrayList<columnInfo> columnConstraint;
	ArrayList<Data> dataList;
	Table targetTable;
	// data를 어떻게 hold할 것인지.
	// 어차피 토큰임.. 토큰 -> 스트링 
	private static final long serialVersionUID = 3L;
	
	public Record() {
		columnConstraint = new ArrayList<columnInfo>();
		dataList = new ArrayList<Data>();
	}
	
	public Table getTable(){
		return targetTable;
	}

	/* insert referential constraint check에만 필요한 함수 */
	
	public String makeForeignKeySet() {
		String tempString = "";
		
		for(int i=0; i<dataList.size(); i++) {
			tempString = tempString + columnConstraint.get(i).getColumnName() + "/";
		}
		
		tempString = tempString.substring(0, tempString.length()-1);
		return tempString;		
	}
	
	public void initialize() {
		columnConstraint = new ArrayList<columnInfo>();
		dataList = new ArrayList<Data>();
		//targetTable = null;
	}
	
	public ArrayList<Data> getDataList(){
		return dataList;
	}
		
	public int getDataListSize() {
		return dataList.size();
	}
	
	public void setDataList(ArrayList<Data> dataList){
		this.dataList = dataList;
	}
	
	public void addDataList(ArrayList<Data> dataList){
		for(int i=0; i<dataList.size(); i++) {
			this.dataList.add(dataList.get(i));
		}		
	}
	
	public void setColumnConstraint(ArrayList<columnInfo> columnNT) {
		columnConstraint = columnNT;
	}
	
	public void strArrayToDataArray(ArrayList<String> strArray, Table targetTable) {		

		this.targetTable = targetTable;
		
		for(int i=0; i<strArray.size(); i++) {			
			Data tempData = new Data(strArray.get(i), i, targetTable.getColumnInfo(i).getColumnName(), targetTable.getColumnInfo(i).getColumnType()); // !@!@! i, targetTable.getColumnInfo(i).getColumnName())
			dataList.add(tempData);			
		}
	}
	
	/* Record 객체를 받아서, legality를 체크하는 함수 필요함. 
	 * 현재, checkLegality는 type을 확인하고 있음.
	 * 메소드 하나가 하나의 error에 대해 처리하게 만들어야 함.
	 * */
	public boolean isTypeLegal(Record record) {

		/* 이 단계에서는 */
		
		/* 여기서 Reference Constraint, nullable constraint 등의 조건을 따져줘야 함. */
		for(int i=0; i<columnConstraint.size(); i++) {
			String tempType1 = columnConstraint.get(i).getColumnType();
			String tempType2 = record.getDataList().get(i).getDataType();
			

			// type constraint check에서는 인풋으로 null이 들어오면, 무조건 OK임.
			if(tempType2.equals("null")) {
				continue;
			}
								
			if(tempType1.contains("char") && tempType2.contains("char")) {
				continue;
			}				
			
			if(tempType1.equals(tempType2)) {				
				continue;
			} else {
				return false;
			}
			  // string과 string 비교. record의 dataList.			
		}		
			return true;
	}
	
	public void printDataList() {
		for(int i=0; i<dataList.size(); i++) {
					
			if(i % columnConstraint.size() == 0){
				for(int j=i; j<i+columnConstraint.size(); j++) {
					System.out.print(dataList.get(j).getData() + "/");
				}
				System.out.println("");
			}
		}		
	}
	
	// select에서 column 뽑아낼 때 편하게 할 수 있는 방법 생각하기
	// select a from c, d 할 때, c, d Cartesian product 편리하게 할 수 있는 방법 생각하기.
}

