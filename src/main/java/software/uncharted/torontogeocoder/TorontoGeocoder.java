package software.uncharted.torontogeocoder;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import software.uncharted.torontogeocoder.model.Intersection;
import software.uncharted.torontogeocoder.reader.DBFReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cdickson on 03/09/16.
 */
public class TorontoGeocoder {
    public static void main(String[] args) {
        String dbfFile = "/home/cdickson/Desktop/centreline_intersection_mtm3/CENTRELINE_INTERSECTION.dbf";

        // Build an intersection list where we have two streets
        List<Intersection> intersectionList = DBFReader.read(dbfFile);

        // Build a map for quick street lookups
        Map<String, List<Intersection>> intersectionMap = new HashMap<>();
        intersectionList.forEach(intersection -> {
            intersection.getStreets().forEach(street -> {
                List<Intersection> partialList = intersectionMap.get(street);
                if (partialList == null) {
                    partialList = Lists.newArrayList();
                }
                partialList.add(intersection);
                intersectionMap.put(street,partialList);
            });
        });

        intersectionList.forEach(intersection -> System.out.println(intersection));

    }
}
