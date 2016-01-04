public class CircularSuffixArray {
	private static final int CUTOFF = 15; // cutoff to insertion sort
	private String s;
	private int[] index;

	// do not instantiate
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new java.lang.NullPointerException();
		this.s = s;
		index = new int[length()];
		for (int i = 0; i < length(); i++) {
			index[i] = i;
		}
		sort(this.s, 0, this.s.length() - 1, 0);
	}

	//
	public int length() {
		return s.length();
	}

	public int index(int i) {
		if (i < 0 || i > length() - 1)
			throw new java.lang.IndexOutOfBoundsException();
		return index[i];
	}

	// return the dth character of nth string
	private int charAt(String s, int d, int n) {
		return s.charAt((n + d) % length());
	}

	// 3-way string quicksort a[lo..hi] starting at dth character
	private void sort(String s, int lo, int hi, int d) {
		// cutoff to insertion sort for small subarrays
		if (hi <= lo + CUTOFF) {
			insertion(s, lo, hi, d);
			return;
		}

		int lt = lo, gt = hi;
		// partition item
		char v = (char) charAt(s, d, index[lo]);
		int i = lo + 1;
		while (i <= gt) {
			// compare item
			char t = (char) charAt(s, d, index[i]);
			if (t < v)
				exch(lt++, i++);
			else if (t > v)
				exch(i, gt--);
			else
				i++;
		}

		// a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
		sort(s, lo, lt - 1, d);
		sort(s, lt, gt, d + 1);
		sort(s, gt + 1, hi, d);
	}

	// sort from a[lo] to a[hi], starting at the dth character
	private void insertion(String a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(a, j, j - 1, d); j--)
				exch(j, j - 1);
	}

	// exchange a[i] and a[j]
	private void exch(int i, int j) {
		int temp = index[i];
		index[i] = index[j];
		index[j] = temp;
	}

	// is j less than k, starting at character d
	private boolean less(String s, int j, int k, int d) {
		for (int i = d; i < length(); i++) {
			char jc = (char) charAt(s, i, index[j]), kc = (char) charAt(s, i, index[k]);
			if (jc < kc)
				return true;
			if (jc > kc)
				return false;
		}
		return false;
	}

	public static void main(String[] args) {
		String s = "ABRACADABRA!";
		// String s = "ABA";
		// ABA BAA AAB
		CircularSuffixArray test = new CircularSuffixArray(s);
		// print the results
		for (int i = 0; i < s.length(); i++)
			System.out.println(test.index(i));
	}
}