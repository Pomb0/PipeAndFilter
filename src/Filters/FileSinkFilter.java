package Filters;

import Framework.FilterFramework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSinkFilter extends FilterFramework {
    private String dataPath;

    public FileSinkFilter(String dataPath) {
        this.dataPath = dataPath;
    }

    public void run() {

        int bytesRead = 0;
        int bytesWritten = 0;
        byte dataByte = 0;
        File file;
        FileOutputStream out;

        try {
            file = new File(this.dataPath);
            out = new FileOutputStream(file);

            if (!file.exists()) {
                if(!file.createNewFile()) {
                    System.out.println(this.getName() + "::File Sink couldn't create the new file.");
                    return;
                }
            }

            while (true) {
                try {
                    dataByte = ReadFilterInputPort();
                    bytesRead++;
                    out.write(dataByte);
                    bytesWritten++;
                } catch (EndOfStreamException e) {
                    ClosePorts();
                    out.flush();
                    out.close();
                    System.out.println(this.getName() + "::File Sink closing; bytes read: " + bytesRead + " bytes written: " + bytesWritten);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(this.getName() + "::File Sink IOException;\n" + e);
        }
    }
}