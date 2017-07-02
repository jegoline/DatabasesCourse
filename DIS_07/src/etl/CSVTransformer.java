package etl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dao.ProductJDBC;
import dao.SalesJDBC;
import dao.ShopJDBC;
import dao.TimeJDBC;

import data.Product;
import data.Sales;
import data.Shop;
import data.Time;

public class CSVTransformer {

	public void transformSales() throws Exception{

    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        String csvFile = "csv/sales.csv";
        String csvTransformedFile = "csv/sales_transformed.csv";
        String line = "";
        String cvsSplitDelimeter = ";";
        int headerLines = 1;
        int lineCounter = 0;
        
        ShopJDBC shopJDBC = new ShopJDBC();
        ProductJDBC productJDBC = new ProductJDBC();
        TimeJDBC timeJDBC = new TimeJDBC();
        
        // load objects from db
        List<Shop> shops = shopJDBC.loadAll();
        List<Product> products = productJDBC.loadAll();
        List<Time> times = timeJDBC.loadAll();
        
        
        BufferedWriter bw = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	// preparing file writer for transformed csv
        	FileWriter fw = new FileWriter(new File(csvTransformedFile));
        	bw = new BufferedWriter(fw);
            
            // remove header
            for(int i=0; i < headerLines; i++){
            	br.readLine();
            }
            
            while ((line = br.readLine()) != null) {

                // use semicolon as separator
                String[] column = line.split(cvsSplitDelimeter);
                lineCounter += 1;
                boolean pursueWrite = true;
                
                int shopId = -1;
                int productId = -1;
                int timeId = -1;
                
                // get matches
                for(Shop shop : shops) {
                    if(shop.getShopName().equals(column[1])) {
                        shopId = shop.getId();
                    }
                }
                
                for(Product product : products) {
                    if(product.getArticleName().equals(column[2])) {
                        productId = product.getId();
                    }
                }
                
                for(Time time : times) {
                    if(df.format(time.getDate()).equals(column[0])) {
                        timeId = time.getId();
                    }
                }
                
                Time receivedTime = null;
                
                if (timeId == -1){
                	Time parsedTime = createTime(column[0], lineCounter);
                	if (parsedTime == null){
                		System.out.println("\"" + column[0] + "\""  + " is not of a correct format. " +
                    			" Change to dd.MM.yyyy . This item will be skipped.");
                    	pursueWrite = false;
                	}
                	receivedTime = timeJDBC.insert(parsedTime);
                	times.add(receivedTime);
                	timeId = receivedTime.getId();
                }
                
                if (shopId == -1){
                	System.out.println("\"" + column[1] + "\""  + " on line " + lineCounter +
                			" was not found in the product table. This item will be skipped.");
                	pursueWrite = false;
                }
                
                if (productId == -1){
                	System.out.println("\"" + column[2] + "\"" + " on line " + lineCounter +
                			" was not found in the product table. This item will be skipped.");
                	pursueWrite = false;
                }
                
                if (pursueWrite) {
                	bw.write(timeId + ";" + shopId + ";" + productId + ";" 
        					+ column[3] + ";" + column[4].replace(",", ".") + ";\n");
                }
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally { 
    	   try{
    	      if(bw!=null)
    		 bw.close();
    	   }
    	   catch(Exception ex){
    		 ex.printStackTrace();
    	    }
    	}

    }
    
    private Time createTime(String dateString, int lineCounter) {
    	Time time = new Time();
    	
    	Date dateItem = null;
    	String monthItem = null;
    	String yearItem = null;
    	String quarterItem = null;
    	
        try {
        	dateItem = new SimpleDateFormat("dd.MM.yyyy").parse(dateString);
        	monthItem = new SimpleDateFormat("MMMM").format(dateItem) + "";
        	yearItem = new SimpleDateFormat("yyyy").format(dateItem) + "";
        	
        	int quarter = Integer.parseInt(new SimpleDateFormat("MM").format(dateItem) );
        	if (quarter < 4)
        		quarterItem = "1st Quarter";
        	else if (quarter < 7)
        		quarterItem = "2nd Quarter";
        	else if (quarter < 10)
        		quarterItem = "3rd Quarter";
        	else if (quarter < 13)
            	quarterItem = "4th Quarter";
        	
		} catch (ParseException e) {
			System.out.println("Incorrect Date format on line " + lineCounter);
			return null;
		}
        
        time.setId(-1);
        time.setDate(dateItem);
        time.setMonth(monthItem);
        time.setYear(yearItem);
        time.setQuarter(quarterItem);
        
        
    	return time;
    }

}
