package utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 02.11.22
 **/
public class CSVFileWriter {

    private final List<String[]> data = new ArrayList<>();

    private final File csvOutFile;

    private final int numberOfRows;

    private String[] header = null;

    private String separator = ",";

    public CSVFileWriter(File csvOutFile, int numberOfRows) {
        this.csvOutFile = csvOutFile;
        assert numberOfRows > 0 : "Am I a Joke to You?";
        this.numberOfRows = numberOfRows;
    }

    public CSVFileWriter(File csvOutFile, String[] header) {
        this(csvOutFile, header.length);
        this.header = header;
    }

    public void addLine(@Nonnull String[] newData) {
        if (newData.length == numberOfRows) {
            data.add(newData);
        } else {
            data.add(new String[]{"INCONSISTENT LENGTH"});
        }
    }

    public void addLine(@Nonnull List<String[]> newData) {
        for (String[] string : newData) {
            addLine(string);
        }
    }

    /**
     * Write CSV
     *
     * @param separator if null uses , as separator by default
     * @param clearData if true data buffer is cleared after write
     * @return CSV File
     * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist
     *                               but cannot be created, or cannot be opened for any other reason
     */
    public File writeCSV(@Nullable String separator, boolean clearData) throws FileNotFoundException {
        if (separator != null) {
            this.separator = separator;
        }
        if (header != null && !csvOutFile.exists()) {
            data.add(0, header);
        }
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutFile, true), true,
                StandardCharsets.UTF_8)) {
            data.stream() //
                    .map(this::convertToCSV) //
                    .forEach(pw::println);
            pw.flush();
        }
        if (clearData) {
            data.clear();
        }
        return csvOutFile;
    }

    /**
     * Write CSV
     *
     * @param separator if null uses , as separator by default
     * @return CSV File
     * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist
     *                               but cannot be created, or cannot be opened for any other reason
     */
    public File writeCSV(@Nullable String separator) throws FileNotFoundException {
        return writeCSV(separator, false);
    }

    /**
     * Write CSV
     *
     * @param clearData if true data buffer is cleared after write
     * @return CSV File
     * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist
     *                               but cannot be created, or cannot be opened for any other reason
     */
    public File writeCSV(boolean clearData) throws FileNotFoundException {
        return writeCSV(null, clearData);
    }

    public File getCsvOutFile() {
        return csvOutFile;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data) //
                .map(this::escapeSpecialCharacters) //
                .collect(Collectors.joining(separator));
    }

    private String escapeSpecialCharacters(String data) {
        return StringEscapeUtils.escapeCsv(data);
    }
}
