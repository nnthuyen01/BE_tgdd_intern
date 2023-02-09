package com.group04.tgdd.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoneyConvert {
    public static BigDecimal calculaterPrice(BigDecimal price, double promotion){
        // Get discount rate
        BigDecimal sale = BigDecimal.valueOf((100 - promotion) / 100);
        // Get price after discount
        sale = (price.multiply(sale)).setScale(0, RoundingMode.UNNECESSARY);
        return sale;
    }

    public static double getConversionRate(String from, String to) throws IOException {
        String url_str = String.format("https://api.exchangerate.host/convert?from=%s&to=%s",from,to);

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        String req_result = jsonobj.get("result").getAsString();
        return Double.parseDouble(req_result);
    }
}
