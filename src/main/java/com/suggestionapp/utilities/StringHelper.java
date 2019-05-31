package com.suggestionapp.utilities;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Helper class containing useful methods related to strings.
 */
public class StringHelper {

    /**
     * Helper method to normalize a string.
     * @param inputString
     * @return
     */
    public static String normalize(String inputString){
        String result = inputString.toLowerCase();
        result = removeDiacritics(result);

        return result;
    }

    /**
     * Helper method to remove diacritics in a string
     * @param inputString
     * @return
     */
    public static String removeDiacritics(String inputString){
        String result = Normalizer
                .normalize(inputString, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");

        return result;
    }
}
