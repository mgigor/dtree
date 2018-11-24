package decisiontree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	
    public static Data extractData() {

        String csvFile = "data.csv";
        String line = "";
        String cvsSplitBy = ";";
        
        try (BufferedReader br = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(csvFile), "UTF8"))) {
        	boolean firstline = true;
        	List<Atribute> atributes = new ArrayList<>();
        	Atribute clazz = null;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                

                for (int i = 0; i < country.length-1; i++) {
					 if(firstline) {
						 atributes.add(new Atribute(country[i], new ArrayList<>()));
					 }else {
						 atributes.get(i).getValues().add(country[i]);
					 }	
					 if(i == country.length-2) {
						 if(firstline) {
							 clazz = new Atribute(country[i+1], new ArrayList<>());
						 }else {
							 clazz.getValues().add(country[i+1]);
						 }					 
					 }
				}
                firstline=false;
            }
            
            return new Data(atributes, clazz);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;

    }

}