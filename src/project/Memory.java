package project;


public class Memory {
    public static final int DATA_SIZE = 2048;
    private int[] data = new int[DATA_SIZE];

    /***
     * Put Part 1 fields here for ordering, before the following fields
     ***/
    private int changedIndex = -1;


    public int[] getDataArray() {
        return data;
    }

    public int getData(int index) {
        return data[index];
    }

    public void setData(int index, int value) {
        data[index] = value;
        changedIndex = index;
    }

    public int getChangedIndex() {
        return this.changedIndex;
    }

    public void clearData(int start, int end) {
        for (int i = start; i < end; i++) {
            data[i] = 0;
        }
        changedIndex = -1;
    }


}
