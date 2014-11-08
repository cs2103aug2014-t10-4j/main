//@author A0110937J

//The following class is used to keep track of the position in string arrays

public class Index {
	private int value;

	public Index() {
		value = 0;
	}

	public Index(int number) {
		this.value = number;
	}

	public void decrement() {
		this.value = value - 1;
	}

	public void incrementByTwo() {
		this.value = value + 2;

	}

	public int getValue() {
		return value;
	}

	public void setValue(int index) {
		this.value = index;
	}

	public void increment() {
		value++;
	}

	public void reset() {
		this.value = 0;
	}

	public void resetToOne() {
		this.value = 1;
	}

}
