import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

public class StatusTableModel implements TableModel {

	List<String> columnNames = new ArrayList<String>();
	List<String> data = new ArrayList<String>();
	private BackEnd be;
	private String status;
	
	public StatusTableModel(BackEnd be, String status){
		this.be = be;
		this.status = status;
		columnNames.add("Pallet number");
		columnNames.add("Batch number");
		columnNames.add("Cookie");
		columnNames.add("Status");
		data.add("hej");
		data.add("hej");
		data.add("hej");
		data.add("hej");
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

}
