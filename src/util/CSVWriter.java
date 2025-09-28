package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter implements AutoCloseable {
    private final PrintWriter writer;
    private boolean headerWritten = false;

    public CSVWriter(String filePath) throws IOException {
        this.writer = new PrintWriter(new FileWriter(filePath));
    }

    public void writeHeader(String... headers) {
        if (!headerWritten) {
            writer.println(String.join(",", headers));
            headerWritten = true;
        }
    }

    public void writeRow(Object... values) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            row.append(values[i]);
            if (i < values.length - 1) row.append(",");
        }
        writer.println(row);
    }

    @Override
    public void close() {
        writer.flush();
        writer.close();
    }
}
