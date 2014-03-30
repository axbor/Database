import java.util.ArrayList;

import javax.swing.AbstractListModel;

	class StatusListModel extends AbstractListModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> statuses = new ArrayList<String>();

		public StatusListModel(BackEnd be){
			statuses.add("Blocked");
			statuses.add("Delivered");
			statuses.add("In production");
			statuses.add("In storage");
		}

		@Override
		public int getSize() {
			return statuses.size();
		}

		@Override
		public String getElementAt(int index) {
			return statuses.get(index);
		}
	}