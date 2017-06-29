package etl;

import java.sql.SQLException;

public class WarehouseLoader {

	public static void main(String... args) {
		System.out.println("Start loading");
		DB2Loader db2Loader = new DB2Loader();
		
		System.out.println("Loading shop data..");
		try {
			db2Loader.loadShops();
		} catch (SQLException e) {
			System.out.println("Can't load shop data");
			e.printStackTrace();
		}
		
		System.out.println("Loading product data..");
		try {
			db2Loader.loadProducts();
		} catch (SQLException e) {
			System.out.println("Can't load product data");
			e.printStackTrace();
		}
		System.out.println("Loading completed");
	}
}
