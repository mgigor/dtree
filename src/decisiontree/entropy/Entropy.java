package decisiontree.entropy;

public class Entropy<T> {
	  private final CounterMap<T> counts = new CounterMap<T>();
	  private int total = 0;

	  private static double Log2(double n) {
	    return Math.log(n) / Math.log(2);
	  }

	  public Entropy(Iterable<T> elements) {
	    for (T element : elements) {
	      counts.incrementAndGet(element);
	      total++;
	    }
	  }

	  public double entropy() {
	    double entropy = 0;
	    for (int count: counts.values()) {
	      double prob = (double) count / total;
	      entropy -= prob * Log2(prob);
	    }
	    return entropy;
	  }

	  public double perplexity() {
	    return Math.pow(2, entropy());
	  }
	}