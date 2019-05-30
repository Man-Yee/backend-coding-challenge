package com.suggestionapp.utilities;


import com.suggestionapp.entities.City;
import com.suggestionapp.utilities.Pair;

import java.util.*;
import java.util.Map.*;




/**
 * Helper class to search for possible city suggestions according to given criteria (name, latitude, longitude) and
 * to attribute a score to each of the suggested city.
 */
public final class SearchHelper {

    private static final TsvReader searcher = new TsvReader();

    private SearchHelper(){

    }

    /**
     * Helper method to search for the given searchString and match it to known cities.  It is also possible to provide
     * the latitude and longitude coordinates to narrow the search.  The resulting cities will be given a score between
     * 0 to 1 where 1 is the closest to the search criteria.
     * @param searchString The complete or partial name of the city that we want to look for
     * @param latitude The given latitude coordinate to disambiguate the search suggestions
     * @param longitude The given longitude coordinate to disambiguate the search suggestions
     * @return a map of suggested cities that fits the search criteria.  The key of the map is a city object and the
     * value is the score given.
     */
    public static Map<City, Double> search(String searchString, Double latitude, Double longitude){
        if(null == searchString){
            throw new NullPointerException("Search string cannot be null");
        }

        if(searchString.isEmpty()){
            throw new IllegalArgumentException("Search string cannot be empty.");
        }

        Integer startIndex = null, endIndex = null;
        String normalizedString = StringHelper.normalize(searchString);

        if(normalizedString.length() >= 2){
            // Look for two characters string match
            startIndex = searcher.searchIndex.getOrDefault(normalizedString.substring(0, 2), null);
        }

        // Look for one character string instead to broaden the search.
        if(null == startIndex){
            startIndex = searcher.searchIndex.getOrDefault(normalizedString.substring(0, 1), null);
        }

        if(null == startIndex){
            // Nothing found
            return Collections.emptyMap();
        }
        else{
            Set<String> keys = searcher.searchIndex.keySet();
            String[] keyArrays = new String[keys.size()];
            keyArrays = keys.toArray(keyArrays);

            // Ensure startIndex is not the last item, find endIndex
            if(startIndex + 1 < searcher.cities.length) {
                Integer nextKeyIndex = Arrays.asList(keyArrays).indexOf(normalizedString.substring(0, 2)) + 1;
                String nextKey = keyArrays[nextKeyIndex];
                System.out.print("\nnextKey: \"" + nextKey + "\"\n");

                endIndex = searcher.searchIndex.get(nextKey);
            }

            if(null == endIndex) {
                endIndex = searcher.cities.length - 1;
            }

            System.out.print("\nKeys size: " + keys.size() + ";StartIndex: " + startIndex + "; EndIndex: " + endIndex + "\n");
            System.out.print("\nCities[startIndex]: " + searcher.cities[startIndex] + "\n");
            System.out.print("\nCities[endIndex]: " + searcher.cities[endIndex] + "\n");

            HashMap<City, Double> resultCityMap = new HashMap<>();
            for(int i = startIndex; i < endIndex; i++){
                Pair<City, Double> aCity = matchCity(normalizedString, latitude, longitude, i);

                if(null != aCity) {
                    resultCityMap.put(aCity.getKey(), aCity.getValue());
                }
            }

            //Sort Map according to descending score
            return sortMapByValue(resultCityMap, false);
        }
    }

    /**
     * Helper method to match the given city name with the known cities.  A score is given to the closest match (name,
     * latitude, longitude).
     * @param normalizedSearchString The given city name (partial or complete)
     * @param latitude The given latitude coordinate to disambiguate similar locations
     * @param longitude The given longitude coordinate to disambiguate similar locations
     * @param index The index in which the city of interest is found in the known cities array
     * @return The City information and its score.  If null is return, it means that the given index is not a match.
     */
    private static Pair<City, Double> matchCity(String normalizedSearchString, Double latitude, Double longitude, Integer index){
        City city = searcher.cities[index];
        Double score = 0d;

        score = matchName(normalizedSearchString, city.getName(), score);

        // Match coordinates only if there's a name match
        if(score > 0d) {
            score = matchCoordinates(city.getLatitude(), latitude, score);
            score = matchCoordinates(city.getLongitude(), longitude, score);
            return new Pair(city, score);
        }
        else{
            return null;
        }
    }

    /**
     * Helper method to calculate whether the given latitude/longitude is closed to the suggested city.  A score will be
     * given accordingly.
     * @param inputLatitude The given latitude or longitude.
     * @param cityLatitude The suggested city latitude or longitude.
     * @param score The score that is given so far to the city.
     * @return The new calculated score after considering the coordinate.
     */
    private static Double matchCoordinates(Double inputLatitude, Double cityLatitude, Double score){
        if(null != cityLatitude) {
            if (Math.abs(cityLatitude - inputLatitude) < 3.0) {
                score += 0.3;
            } else if (Math.abs(cityLatitude - inputLatitude) < 5.0) {
                score += 0.1;
            }
        }

        return score;
    }

    /**
     * Helper method to calculate whether the given name match to the suggested city.  A score will be given accordingly.
     * @param inputName The given name for the match.
     * @param cityName The suggested city name.
     * @param score The score that is given so far to the city.
     * @return The new calculated score after considering name
     */
    private static Double matchName(String inputName, String cityName, Double score){
        if(cityName.equalsIgnoreCase(inputName)){
            score += 0.4;
        }
        else if(cityName.startsWith(inputName)){
            score += 0.3;
        }

        return score;
    }

    /**
     * A generic helper method to sort a map according to its value (in our case the value is the score).
     * @param map a map with keys and values.
     * @param ascending if true, ascending sort.  Otherwise if false, descending sort.
     * @param <K> Key object type
     * @param <V> Value object type
     * @return Sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map, boolean ascending) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        if(!ascending){
            Collections.reverse(list);
        }

        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
