package com.isispl.customlistviewfromjson;

import com.isispl.customlistviewfromjson.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaymin581 on 22/06/15.
 */
public class FlowerJSONParser  {
    public static List<Flower> parseFeed(String content){
        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i ++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Flower flower = new Flower();
                flower.setProductId(jsonObject.getInt("productId"));
                flower.setPrice(jsonObject.getDouble("price"));
                flower.setCategory(jsonObject.getString("category"));
                flower.setName(jsonObject.getString("name"));
                flower.setInstructions(jsonObject.getString("instructions"));
                flower.setPhoto(jsonObject.getString("photo"));

                flowerList.add(flower);
            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

