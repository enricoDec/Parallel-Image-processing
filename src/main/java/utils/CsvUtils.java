package utils;

import de.siegmar.fastcsv.reader.NamedCsvReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 05.12.22
 **/
public class CsvUtils {

    public static double getAverageOfCsvColumn(File csvFile, String columnName, int numberOfRows) throws IOException {
        ArrayList<String> values;
        try (NamedCsvReader csv = NamedCsvReader.builder().fieldSeparator(';').build(csvFile.toPath())) {
            if (numberOfRows != 0) {
                values = new ArrayList<>(numberOfRows);
            } else {
                values = new ArrayList<>();
            }
            csv.forEach(row -> values.add(row.getField(columnName).trim()));
        }
        if (values.isEmpty()) {
            throw new RuntimeException("No data found");
        } else {
            return values.stream().mapToLong(Long::parseLong).summaryStatistics().getAverage();
        }
    }

    public static long getNumberOrRows(File csvFile) throws IOException {
        try (NamedCsvReader csv = NamedCsvReader.builder().fieldSeparator(';').build(csvFile.toPath())) {
            return csv.stream().count();
        }
    }
}
