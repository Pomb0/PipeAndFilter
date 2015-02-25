package Filters;

import Framework.ExpandedFilterFramework;
import Framework.FilterFramework;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSourceFilter extends ExpandedFilterFramework{
    private String dataPath;

    public FileSourceFilter(String dataPath) {
        this.dataPath = dataPath;
    }

    public void run() {
        int bytesRead = 0;
        int bytesWritten = 0;
        DataInputStream dis = null;
        byte dataByte = 0;

        try {
            dis = new DataInputStream(new FileInputStream(this.dataPath));
            System.out.println(this.getName() + "::Source Filter reading file...");

            while (true) {
                dataByte = dis.readByte();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            }
        } catch (EOFException eoferr) {
            System.out.println(this.getName() + "::End of file reached...");

            try {
                dis.close();
                ClosePorts();
                System.out.println(this.getName() + "::Read file complete, bytes read::" + bytesRead + " bytes written: " + bytesWritten);
            } catch (Exception closeerr) {
                System.out.println(this.getName() + "::Problem closing input data file::" + closeerr);
            }

        } catch (IOException iox) {
            System.out.println(this.getName() + "::Problem reading input data file::" + iox);
        }
    }
}
