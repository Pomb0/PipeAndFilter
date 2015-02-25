package Filters;

import Framework.FilterFramework;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSourceFilter extends FilterFramework{
    private String dataPath;

    public FileSourceFilter(String dataPath){
        this.dataPath = dataPath;
    }

    public void run(){
        int bytesRead = 0;					// Number of bytes read from the input file.
        int bytesWritten = 0;				// Number of bytes written to the stream.
        DataInputStream in = null;			// File stream reference.
        byte dataByte = 0;					// The byte of data read from the file.

        try{
            in = new DataInputStream(new FileInputStream(this.dataPath));
            System.out.println(this.getName() + "::File Reader reading file..." );

            while(true){
                dataByte = in.readByte();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            }

        }catch ( EOFException eoferr ){
            System.out.println(this.getName() + "::End of file reached..." );
            try{
                in.close();
                ClosePorts();
                System.out.println(this.getName() + "::Read file complete, bytes read::" + bytesRead + " bytes written: " + bytesWritten );
            }catch (Exception closeerr){
                System.out.println(this.getName() + "::Problem closing input data file::" + closeerr);
            }
        }catch ( IOException iox ){
            System.out.println(this.getName() + "::Problem reading input data file::" + iox );
        }
    }
}

