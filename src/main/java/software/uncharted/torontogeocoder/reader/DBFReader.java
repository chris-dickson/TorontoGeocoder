package software.uncharted.torontogeocoder.reader;

import org.jamel.dbf.processor.DbfProcessor;
import org.jamel.dbf.utils.DbfUtils;
import software.uncharted.torontogeocoder.model.Intersection;
import software.uncharted.torontogeocoder.model.LatLng;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by cdickson on 03/09/16.
 */
public class DBFReader {
    public static List<Intersection> read(String filePath) {
        File dbf = new File(filePath);
        return DbfProcessor.loadData(dbf, row -> {
            String description = new String(DbfUtils.trimLeftSpaces((byte[]) row[2]));
            LatLng latLng = new LatLng((Double) row[15],(Double) row[16]);


            return new Intersection(description)
                    .setLatLng(latLng);
        })
            .stream()
            .filter(i -> Objects.nonNull(i.getStreets()) && i.getStreets().size() == 2)
            .collect(Collectors.toList());
    }
}
