package com.suggestionapp;

import junit.framework.Assert;
import org.junit.Test;
import com.suggestionapp.entities.City;
import com.suggestionapp.utilities.SearchHelper;

import java.util.Map;

public class SearchTest {

    @Test
    public void TestSearchHelper_MixedCaseSearchString(){
        Map<City, Double> result = SearchHelper.search("LonDo", 43.70011, -79.4163);

        Assert.assertEquals(result.size(), 5);

        boolean found = false;
        int i = 0;
        for (Map.Entry<City, Double> entry : result.entrySet()) {
            System.out.println("\nResult: " + entry.getKey() + "/" + entry.getValue());

            if(!found && entry.getKey().getName().equalsIgnoreCase("london")) {
                Assert.assertEquals(entry.getKey().getCountry(), "ca");
                Assert.assertEquals(entry.getKey().getLatitude(), 42.98339);
                Assert.assertEquals(entry.getKey().getLongitude(), -81.23304);
                Assert.assertEquals("London should be the first hit.", i, 0);
                found = true;
            }

            i++;
        }

        Assert.assertTrue("Did not find London", found);
    }

    @Test
    public void TestSearchHelper_SearchStringInCaps(){
        Map<City, Double> result = SearchHelper.search("LONDO", 43.70011, -79.4163);

        Assert.assertEquals(result.size(), 5);

        boolean found = false;
        int i = 0;
        for (Map.Entry<City, Double> entry : result.entrySet()) {
            System.out.println("\nResult: " + entry.getKey() + "/" + entry.getValue());

            if(!found && entry.getKey().getName().equalsIgnoreCase("london")) {
                Assert.assertEquals(entry.getKey().getCountry(), "ca");
                Assert.assertEquals(entry.getKey().getLatitude(), 42.98339);
                Assert.assertEquals(entry.getKey().getLongitude(), -81.23304);
                Assert.assertEquals("London should be the first hit.", i, 0);
                found = true;
            }

            i++;
        }

        Assert.assertTrue("Did not find London", found);
    }

    @Test
    public void TestSearchHelper_ExactNameNoCoordinates(){
        Map<City, Double> result = SearchHelper.search("London", null, null);

        Assert.assertEquals(result.size(), 5);

        boolean found = false;
        int i = 0;
        for (Map.Entry<City, Double> entry : result.entrySet()) {
            System.out.println("\nResult: " + entry.getKey() + "/" + entry.getValue());

            if(!found && entry.getKey().getName().equalsIgnoreCase("london")) {
                Assert.assertEquals("London should be the first hit.", i, 0);
                found = true;
            }

            i++;
        }

        Assert.assertTrue("Did not find London", found);
    }

    @Test
    public void TestSearchHelper_NonExistentCityName(){
        Map<City, Double> result = SearchHelper.search("SomeNonExistentCityName", null, null);

        Assert.assertEquals("Should not find any hit for city name \"SomeNonExistentCityName\"", result.size(), 0);
    }

}
