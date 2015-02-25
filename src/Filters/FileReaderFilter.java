package Filters;

import Framework.FilterFramework;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderFilter extends FilterFramework{
    private String dataPath;

    public FileReaderFilter(String dataPath){
        this.dataPath = dataPath;
    }

    public void run()
    {

        String fileName = "FlightData.dat";	// Input data file.
        int bytesread = 0;					// Number of bytes read from the input file.
        int byteswritten = 0;				// Number of bytes written to the stream.
        DataInputStream in = null;			// File stream reference.
        byte databyte = 0;					// The byte of data read from the file

        try{

            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file..." );

            while(true){
                databyte = in.readByte();
                bytesread++;
                WriteFilterOutputPort(databyte);
                byteswritten++;

            }

        }catch ( EOFException eoferr ){
            System.out.println("\n" + this.getName() + "::End of file reached..." );
            try{
                in.close();
                ClosePorts();
                System.out.println( "\n" + this.getName() + "::Read file complete, bytes read::" + bytesread + " bytes written: " + byteswritten );

            }catch (Exception closeerr){
                System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeerr);

            }
        }catch ( IOException iox ){
            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox );

        }

    }

}

