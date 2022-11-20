import de.siegmar.fastcsv.reader.NamedCsvReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author : Enrico Gamil Toros
 * Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 20.11.22
 **/
public class TestUtils {

    public static double getAverageOfCsvColumn(File csvFile, String columnName) throws IOException {
        return getAverageOfCsvColumn(csvFile, columnName, 0);
    }

    public static double getAverageOfCsvColumn(File csvFile, String columnName, int numberOfRows) throws IOException {
        ArrayList<String> values;
        try (NamedCsvReader csv = NamedCsvReader.builder().fieldSeparator(';').build(csvFile.toPath())) {
            if (numberOfRows != 0) {
                values = new ArrayList<>(numberOfRows);
            } else {
                values = new ArrayList<>();
            }
            csv.forEach(row -> values.add(row.getField(columnName)));
        }
        if (values.isEmpty()) {
            throw new RuntimeException("No data found");
        } else {
            return values.stream().mapToLong(Long::parseLong).summaryStatistics().getAverage();
        }
    }
}
