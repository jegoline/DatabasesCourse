package ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dnl.utils.text.table.TextTable;
import etl.DB2ConnectionManager;

public class Main {

	private static List<String> shopDims = Arrays.asList("shop_dim.land_name", "shop_dim.region_name",
			"shop_dim.stadt_name", "shop_dim.shop_name");
	private static String currentShopDim = shopDims.get(0);
	
	private static List<String> productDims = Arrays.asList("product_dim.category_name", "product_dim.family_name",
			"product_dim.group_name", "product_dim.article_name");
	private static String currentProductDim = productDims.get(0);

	private static void load(String currentShopDim, String currentProductDim){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement selectStmt = null;
		try {
			String selectSQL = "select sum(sales.amount), sum(sales.items_sold), %s, "
					+ "%s, time_dim.year "
					+ "from sales join product_dim  on sales.fk_article_id=product_dim.id "
					+ "join shop_dim on sales.fk_shop_id = shop_dim.id "
					+ "join time_dim on sales.fk_time_id = time_dim.id "
					+ "GROUP BY rollup(time_dim.year, %s, %s) "
					+ "order by %s, time_dim.year, %s";
			selectSQL = String.format(selectSQL, currentShopDim, currentProductDim, currentShopDim, currentProductDim,
					currentShopDim, currentProductDim);
			selectStmt = con.prepareStatement(selectSQL);
			System.out.println(selectSQL);

			ResultSet rs = selectStmt.executeQuery();
			List<List<String>> tableData = new ArrayList<>();

			while (rs.next()) {
				List<String> raw = new ArrayList<>();
				raw.add(rs.getString(3) == null ? "" : rs.getString(3));
				raw.add(rs.getString(4) == null ? "" : rs.getString(4));
				raw.add(rs.getString(5) == null ? "" : rs.getString(5));
				raw.add(rs.getString(1) == null ? "" : rs.getString(1));
				raw.add(rs.getString(2) == null ? "" : rs.getString(2));
				tableData.add(raw);
			}

			String[][] tableDataStr = new String[tableData.size()][5];
			int j = 0;
			for (List<String> l : tableData) {
				tableDataStr[j] = l.toArray(new String[l.size()]);
				j++;
			}

			TextTable table = new TextTable(new String[] { "Place", "Products", "Time", "Sales", "Items sold" }, tableDataStr);
			table.printTable();
		} catch(SQLException ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		final int DRILL_DOWN = 0;
		final int ROLL_UP = 1;
		final int QUIT = 2;

		load(currentShopDim, currentProductDim);

		// Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Drill down", DRILL_DOWN);
		mainMenu.addEntry("Roll up", ROLL_UP);
		mainMenu.addEntry("Quit", QUIT);

		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
			case DRILL_DOWN:
				showDimMenu(true);
				break;
			case ROLL_UP:
				showDimMenu(false);
				break;
			case QUIT:
				return;
			}
		}
	}

	private static void showDimMenu(boolean drillDown) {
		// Menüoptionen
		final int PLACE = 0;
		final int TIME = 1;
		final int PRODUCT = 2;
		final int BACK = 4;

		// Maklerverwaltungsmenü
		Menu menu = new Menu("Manage Estate Agents");
		menu.addEntry("Place", PLACE);
		menu.addEntry("Time", TIME);
		menu.addEntry("Product", PRODUCT);
		menu.addEntry("Back", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = menu.show();
			switch (response) {
			case PLACE:
				currentShopDim = nextDim(shopDims, currentShopDim, drillDown);
				load(currentShopDim, currentProductDim);
				break;
			case TIME:

				break;
			case PRODUCT:
				currentProductDim = nextDim(productDims, currentProductDim, drillDown);
				load(currentShopDim, currentProductDim);
				break;
			case BACK:
				return;
			}
		}
	}

	private static String nextDim(List<String> dim, String current, boolean drillDown) {
		int ind = dim.indexOf(current);
		if (drillDown && ind == dim.size() - 1) {
			return current;
		}

		if (!drillDown && ind == 0) {
			return current;
		}

		return drillDown ? dim.get(ind + 1) : dim.get(ind - 1);
	}
}
