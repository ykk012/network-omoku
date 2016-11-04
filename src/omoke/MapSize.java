package omoke;

import java.io.Serializable;

public class MapSize implements Serializable {
	final int CELL=30;
	final int SIZE=20;
	public MapSize(){}
	public int getCell() {
		return CELL;
	}
	public int getSize() {
		return SIZE;
	}
}
