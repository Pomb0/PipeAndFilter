package Systems;

import Filters.FileReaderFilter;

public class SystemA {
    public static void main(String args []){
        FileReaderFilter dataReader = new FileReaderFilter("data/FlightData.dat");
        dataReader.start();
    }
}
