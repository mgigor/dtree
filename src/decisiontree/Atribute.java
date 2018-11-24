package decisiontree;

import java.util.List;

public class Atribute {

	private String name;
	private List<String> values;
	
	public Atribute(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public List<String> getValues() {
		return values;
	}
	
	
}
