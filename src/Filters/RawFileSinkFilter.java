package Filters;

import Framework.ExpandedFilterFramework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Outputs a byte stream to a file.
 */

public class RawFileSinkFilter extends ExpandedFilterFramework {
    private String dataPath;
    
    public RawFileSinkFilter(String dataPath) { 
        this.dataPath = dataPath;
        try {
            FileWriter fw = new FileWriter(this.dataPath);
            fw.close();
        } catch (IOException e) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem truncating " + dataPath + " >>" + e);
        }
    }

    public void run() {
        try {
            FileOutputStream output = new FileOutputStream(new File(dataPath), true);
            while (true) {
                output.write(ReadFilterInputPort());
            }
        } catch (EndOfStreamException e) {
            ClosePorts();
        } catch (IOException e) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem writing to " + dataPath + " >>" + e);
        }
    }
}
