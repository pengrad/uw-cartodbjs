package com.github.pengrad.uw_cartodbjs;

import java.util.List;

/**
 * Stas Parshin
 * 11 November 2015
 */
public class Model {

    List<Place> rows;

    public class Place {
        String address, name;
        Double longitude, latitude;

        public boolean isCorrect() {
            return longitude != null && latitude != null;
        }
    }
}
