package decisiontree.entropy;

import java.math.BigDecimal;
import java.util.Map;

public class EntropyAtributeResponse {
	private BigDecimal entropy;
	private Map<String,BigDecimal> entropys;
	
	public EntropyAtributeResponse(BigDecimal entropy, Map<String, BigDecimal> entropys) {
		this.entropy = entropy;
		this.entropys = entropys;
	}
	
	public BigDecimal getEntropy() {
		return entropy;
	}
	
	public Map<String, BigDecimal> getEntropys() {
		return entropys;
	}
	
}
