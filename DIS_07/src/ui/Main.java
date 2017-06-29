package ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dnl.utils.text.table.TextTable;
import etl.DB2ConnectionManager;

public class Main {
	private static void load() throws SQLException{
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement selectStmt = null;
		try {
			String selectSQL = "select * from sales join product_dim "
					+ "on sales.article_id=product_dim.id "
					+ "join shop_dim on sales.shop_id = shop_dim.id";
			selectStmt = con.prepareStatement(selectSQL);
			ResultSet rs = selectStmt.executeQuery();
			int i = 0;
			//String[][] data = new String[sells.size()][columnNames.length];
			while (rs.next()) {
				
			}
		} finally {
			
		}
		
		TextTable table = new TextTable(new String[] { "col1", "col2" }, new String[][] {new String[]{"","a2"}, new String[]{"b1","b2"}});
		table.printTable();

	}

	public static void main(String[] args) {
		// Menüoptionen
		final int MENU_AGENT = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		
		// Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Manage estate agents", MENU_AGENT);
		mainMenu.addEntry("Manage estates", MENU_ESTATE);
		mainMenu.addEntry("Manage contracts", MENU_CONTRACT);
		mainMenu.addEntry("Quit", QUIT);

		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
			case MENU_AGENT:

				break;
			case MENU_ESTATE:

				break;
			case MENU_CONTRACT:

				break;
			case QUIT:
				return;
			}
		}
	}

}
