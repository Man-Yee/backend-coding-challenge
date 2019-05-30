package com.suggestionapp.utilities;

import com.suggestionapp.entities.City;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to read the TSV file and extract the city information to store in an array.
 * It then use the array to build a search index map.
 * Assumption: TSV contains name of cities in alphabetical order.
 */
public class TsvReader {
    private static final String FILENAME = "src/main/java/com/suggestionapp/data/cities_canada-usa.tsv";

    /**
     * List of known cities in North America
     */
    public static City[] cities;

    /**
     * Map containing the first or the first two letters of a city name and the start index where we can find the city
     * with such prefix.  The searchIndex is sorted alphabetically.
     */
    public static Map<String, Integer> searchIndex;

    public TsvReader(){
        cities = parseTSV(FILENAME);

        if(null != cities){
            Arrays.sort(cities); // Important in order to have an ordered array!
            searchIndex = buildSearchIndex(cities);
        }
    }

    /**
     * Parsing TSV file: Reading the file and build an array with the extracted cities information.
     * @param fileName - Path and filename
     * @return an array containing city objects.
     */
    private static City[] parseTSV(String fileName) {
        City[] cities = null;

        try {
            Path path = Paths.get(fileName);
            System.out.print("\nPath to file:" + path.toAbsolutePath());

            cities = new City[(int)Files.lines(path).count()-1];

            // create an instance of BufferedReader and manage resource with try
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

                // read the first line from the text file
                br.readLine();

                // Skip first line which contains column names
                String line = br.readLine();

                // Define array index counter
                int i = 0;

                // loop until all lines are read
                while (line != null) {
                    // use string.split to load a string array with the values from
                    // each line of the file, using a tab as the delimiter
                    String[] attributes = line.split("\t", -1);

                    City city = createCity(attributes);

                    // adding city into Map
                    cities[i] = city;

                    // read next line before looping
                    // if end of file reached, line would be null
                    line = br.readLine();

                    // increment array index counter
                    i++;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        return cities;
    }

    /**
     * Create a city object using the metadata found in the TSV file
     * @param metadata - metadata found in TSV
     * @return city object
     */
    private static City createCity(String[] metadata) {
        String name = StringHelper.normalize(metadata[1]);
        String country = StringHelper.normalize(metadata[8]);
        Double latitude = Double.parseDouble(metadata[4]);
        Double longitude = Double.parseDouble(metadata[5]);


        // create and return city of this metadata
        return new City(name, country, latitude, longitude);
    }

    /**
     * Build the search index: the search index is a map containing:
     *      - The first character of the city name and the first two characters of the city name as key
     *      - The index in the sorted array where it first occurred as value.
     * @param sortedCities sorted array of city objects
     * @return search index map
     */
    private static Map<String, Integer> buildSearchIndex(City[] sortedCities){
        HashMap<String, Integer> searchIndex = new HashMap<>();

        System.out.print("\nThere are " + sortedCities.length + " Cities.");

        for(int i = 0; i < sortedCities.length; i++){
            String firstChar = Character.toString(sortedCities[i].getName().charAt(0));
            if(!searchIndex.containsKey(firstChar)){
                // Save the index
                searchIndex.put(firstChar, i);
            }

            String twoChars = sortedCities[i].getName().substring(0,2);
            if(!searchIndex.containsKey(twoChars)){
                searchIndex.put(twoChars, i);
            }
        }

        return searchIndex;
    }
}
