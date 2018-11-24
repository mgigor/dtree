package decisiontree;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import decisiontree.entropy.Entropy;
import decisiontree.entropy.EntropyAtributeResponse;

public class App {
	
	public static void main(String[] args) {
		System.out.println("start");
		Data data = CSVReader.extractData();
		TreeNode tree = calcula(data,null,null,0);
		
		tree.print();

	}
	
	private static Data filtraData(Data data, Atribute atribute, String valueSelected) {
		
		List<Integer> indexList = new ArrayList<>();
		int x = 0;
		for (String value : atribute.getValues()) {
			if(value.equals(valueSelected)) {
				indexList.add(x);
			}
			x++;
		}
		List<Atribute> atributeList = new ArrayList<>();
		for (Atribute atributeData : data.atributes) {
			if(atributeData.getName() != atribute.getName()) {
				List<String> values = new ArrayList<>();
				for (Integer integer : indexList) {
					values.add(atributeData.getValues().get(integer));
				}
				atributeList.add(new Atribute(atributeData.getName(), values));			
			}

		}
		
		List<String> values = new ArrayList<>();
		for (Integer integer : indexList) {
			values.add(data.clazz.getValues().get(integer));
		}	
		Atribute clazz = new Atribute(data.clazz.getName(), values);
		
		return new Data(atributeList, clazz);
		
	}
	
	
	private static TreeNode calcula(Data dataArg, Atribute selected, String value, int level) {
		List<TreeNode> treechild = new ArrayList<>();
		TreeNode tree;
		Data data;
		if(selected == null) {
			data = dataArg;
			tree = new TreeNode("", treechild);
		}else {
			tree = new TreeNode(selected.getName() + " ("+value +")", treechild);
			data = filtraData(dataArg, selected, value);
		}
		
		for (Atribute atribute : data.atributes) {
			System.out.print(atribute.getName()+"\t");
		}
		System.out.println(data.clazz.getName());
		
		for (int i = 0; i < data.atributes.get(0).getValues().size(); i++) {
			for (int i2 = 0; i2 < data.atributes.size(); i2++) {
				System.out.print(data.atributes.get(i2).getValues().get(i)+"\t");
			}
			System.out.println(data.clazz.getValues().get(i));
		}

		
		
		//if(data.clazz.getValues().stream().distinct().count() == 1)
			System.out.println(data.clazz.getValues().stream().distinct().collect(Collectors.toList()));
		BigDecimal entropyFromClass = calculateEntropy(data.clazz.getValues());
		
		Map<Atribute,BigDecimal> igs = new HashMap<>();
		EntropyAtributeResponse max = null;
		BigDecimal lastIG = new BigDecimal(-1);
		Atribute high = null;
		
		System.out.println("CONJUNTO Entropia ("+ data.clazz.getName() +"): " + entropyFromClass);
		System.out.println("level:" + level);
		for (Atribute atribute : data.atributes) {
			EntropyAtributeResponse resp = calculateEntropyFromAtributes(atribute,data.clazz.getValues());
			BigDecimal entropyFromAtribute = resp.getEntropy();
			BigDecimal ig = entropyFromClass.subtract(entropyFromAtribute);
			
			if(ig.compareTo(lastIG) > 0) {
				lastIG = ig;
				max = resp;
				high = atribute;
			}
			
			System.out.println("\tIG: "+ig);
			BigDecimal gr;
			try {
				gr = ig.divide(calculateEntropy(atribute.getValues()), RoundingMode.HALF_DOWN);
			}catch (Exception e) {
				gr = new BigDecimal(0);
			}
			 
			System.out.println("\tGR: "+gr);
			igs.put(atribute, ig);
		}
		System.out.println("______________________________");
		for (Entry<String, BigDecimal> entry : max.getEntropys().entrySet()) {
			//if(entry.getValue().doubleValue() != 0d && data.atributes.size() != 1) {
			if( data.atributes.size() != 1) {
			//if( data.atributes.size() != 1) {
				Atribute selectedAtribute = high;	
				System.out.println(">>" + selectedAtribute.getName());
				System.out.println(">>" + entry.getKey());
				treechild.add(calcula(data,selectedAtribute,entry.getKey(),level+1));
			}
		}
		
		return tree;
		
	}	
	
		
	private static EntropyAtributeResponse calculateEntropyFromAtributes(Atribute atribute, List<String> clazz) {
		List<String> elements = atribute.getValues();
		List<String> elementsDistinctValues = elements.stream().distinct().collect(Collectors.toList());
		BigDecimal entropy = new BigDecimal(0);
		System.out.println(atribute.getName().toUpperCase());
		Map<String,BigDecimal> entrops = new HashMap<>();
		for (String value : elementsDistinctValues) {
			List<String> clazzElements = new ArrayList<>();
			System.out.print("\tEntropia ("+value+") ");
			for (int i = 0; i < elements.size(); i++) {
				if(elements.get(i).equals(value)) {
					clazzElements.add(clazz.get(i));
				}
			}
			
	        Map<String, Long> counted = clazzElements.stream()
	                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	        
			System.out.print(formatCollectionToDivision(counted.values(),clazzElements.size()) + " : ");
			
			double totalValue = elements.stream().filter(x -> x.equals(value)).count();
			double totalClass = clazz.size();
			BigDecimal entropyAtribute = calculateEntropy(clazzElements);
			System.out.println(entropyAtribute);
			entropy = entropy.add(entropyAtribute.multiply(new BigDecimal( totalValue / totalClass) ).setScale(6, RoundingMode.HALF_DOWN));	
			entrops.put(value, entropyAtribute);
			
		}
		
		System.out.println("\tEntropia ("+atribute.getName()+"): "+entropy);
		return new EntropyAtributeResponse(entropy,entrops);
	}
	
	private static BigDecimal calculateEntropy(List<String> elements) {
		return new BigDecimal( new Entropy<>(elements).entropy() ).setScale(6, RoundingMode.HALF_DOWN);
	}
	
	private static List<String> formatCollectionToDivision(Collection<Long> list, int total) {
		return list.stream().map(x -> x+"/"+total).collect(Collectors.toList());
	}
	
}
