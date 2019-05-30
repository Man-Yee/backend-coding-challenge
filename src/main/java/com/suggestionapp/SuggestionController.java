package com.suggestionapp;

import com.suggestionapp.entities.City;
import com.suggestionapp.utilities.SearchHelper;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuggestionController{

    @RequestMapping("/suggestions")
    public ResponseEntity suggestions(@RequestParam String searchString,
                                  @RequestParam(value="latitude", required=false) Double latitude,
                                  @RequestParam(value="longitude", required=false) Double longitude) {
        Map<City, Double> result = null;
        try{
            result = SearchHelper.search(searchString, latitude, longitude);
        }
        catch(Exception ex){
            System.out.print("Exception occured: " + ex.getStackTrace());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
