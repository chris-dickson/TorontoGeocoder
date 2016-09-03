package software.uncharted.torontogeocoder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Intersection implements Serializable {
    protected static String[] STOP_WORDS_ARR = {"N","S","E","W","NW","NE","SW","SE","N W","N E","S W","S E",
            "EXPRESS","RD","BLVD","AVE", "AV", "ST","DR","CRT","HWY","AVE", "CRES", "LN", "LANE", "TRL", "GRV", "WAY", "RAMP", "PL","C", "SQ"};
    public static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(STOP_WORDS_ARR));

    protected String description;
    protected List<String> streets;
    protected LatLng latLng;

    protected String getStreetName(String str) {
        String[] pieces = str.split(" ");
        String clean = "";
        for (int i = 0; i < pieces.length; i++) {
            String s = pieces[i].toUpperCase();
            if (STOP_WORDS.contains(s)) {
                continue;
            }
            clean += pieces[i] + " ";
        }

        String cleanName = clean.trim();

        // Make sure we didn't remove "St" when it mains "Saint"
        int stIdx = str.toUpperCase().indexOf("ST");
        if ( stIdx != -1 && stIdx < str.toUpperCase().indexOf(clean)) {
            cleanName = "ST " + cleanName;
        }
        return cleanName;
    }

    public Intersection(String description) {
        this.description = description;

        String desc = description.toUpperCase();
        if (desc == "") {
            return;
        }

        String[] streets = desc.split("/");
        String[] cleaned = new String[streets.length];
        for (int i = 0; i < streets.length; i++) {
            cleaned[i] = getStreetName(streets[i]);
        }
        this.streets = Arrays.asList(cleaned);

        if (hasMultiWordStreet()) {
            // Remove occurrences of single word street names in the others
            List<String> singleWordStreets = this.streets.stream().filter(s -> s.split(" ").length == 1).collect(Collectors.toList());

            for (int i = 0; i < this.streets.size(); i++) {
                if (this.streets.get(i).split(" ").length > 2) {
                    for (int j = 0; j < singleWordStreets.size(); j++) {
                        if (this.streets.get(i).contains(singleWordStreets.get(j))) {
                            String trimmed = this.streets.get(i).replace(singleWordStreets.get(j), "").trim();
                            this.streets.set(i, trimmed);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "\"" + this.description + "\"," +
               "\"" + this.streets.get(0) + "\"," +
               "\"" + this.streets.get(1) + "\"," +
               "\"" + this.latLng.getLat() + "\"," +
               "\"" + this.latLng.getLng() + "\"";
    }

    public boolean contains(String street) {
        return streets.stream().anyMatch((s -> s.equalsIgnoreCase(street)));
    }
    public boolean hasMultiWordStreet() {
        return streets.stream().anyMatch(s -> s.split(" ").length > 1);
    }
}
