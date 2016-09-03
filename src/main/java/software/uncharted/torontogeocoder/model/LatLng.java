package software.uncharted.torontogeocoder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Accessors(chain=true)
public class LatLng implements Serializable {
    private Double lat;
    private Double lng;

    public LatLng(String latLngString) {
        String[] latLng = latLngString.split(",");
        lat = Double.parseDouble(latLng[0]);
        lng = Double.parseDouble(latLng[1]);
    }

    public LatLng clamp() {
        return clamp(-90.0,-180.0,90.0,180.0);
    }

    public LatLng clamp(Double minLat, Double minLng, Double maxLat, Double maxLng) {
        lng = Math.max(minLng, Math.min(lng, maxLng));
        lat = Math.max(minLat, Math.min(lat, maxLat));
        return this;
    }
}

