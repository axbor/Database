import java.util.ArrayList;

import javax.swing.AbstractListModel;

	class BlockingListModel extends AbstractListModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> blocking = new ArrayList<String>();

		public BlockingListModel(BackEnd be){
			blocking = be.getBatches();
		}

		@Override
		public int getSize() {
			return blocking.size();
		}

		@Override
		public String getElementAt(int index) {
			return blocking.get(index);
		}
	}