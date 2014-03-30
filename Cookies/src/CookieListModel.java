import java.util.ArrayList;

import javax.swing.AbstractListModel;

	class CookieListModel extends AbstractListModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> cookies = new ArrayList<String>();

		public CookieListModel(BackEnd be){
			cookies = be.getCookies();		
		}

		public int getSize() {
			return cookies.size();
		}

		public String getElementAt(int index) {
			return cookies.get(index);
		}
	}
