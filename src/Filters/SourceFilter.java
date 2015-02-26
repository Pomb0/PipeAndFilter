package Filters;

import Framework.ExpandedFilterFramework;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class SourceFilter extends ExpandedFilterFramework{
    private String dataPath;
    public SourceFilter(String dataPath) {
        this.dataPath = dataPath;
    }

    public void run() {
        DataInputStream dis = null;
        byte dataByte;

        try {
            dis = new DataInputStream(new FileInputStream(this.dataPath));
            System.out.println(this.getClass().getCanonicalName() + "::Reading source file...");
            while (true) {
                dataByte = dis.readByte();
                WriteFilterOutputPort(dataByte);
            }
            
        } catch (EOFException e) {
            System.out.println(this.getClass().getCanonicalName() + "::End of the source file reached...");
            try {
                ClosePorts();
                dis.close();
            } catch (IOException e1) {
                System.out.println(this.getClass().getCanonicalName() + "::Problem closing source file...");
            }

        } catch (IOException iox) {
            System.out.println(this.getClass().getCanonicalName() + "::Problem reading source file...");
        }
    }
}
