package etl;

import java.sql.SQLException;

public class WarehouseLoader {

	public static void main(String... args) {
		System.out.println("Start loading");
		DB2Loader db2Loader = new DB2Loader();
		CSVTransformer csvTransformer = new CSVTransformer();
		
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
		
		System.out.println("Transforming sales.csv data for foreign key support..");
		try {
			csvTransformer.transformSales();
		} catch (Exception e) {
			System.out.println("Can't transform sales data");
			e.printStackTrace();
		}
		System.out.println("Transformation completed");
		
		System.out.println("Loading sales data..");
		try {
			db2Loader.loadSales();
		} catch (SQLException e) {
			System.out.println("Can't load sales data");
			e.printStackTrace();
		}
		System.out.println("Loading completed");
	}
}
