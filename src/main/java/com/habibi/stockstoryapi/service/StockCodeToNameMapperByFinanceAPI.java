package com.habibi.stockstoryapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockCodeToNameMapperByFinanceAPI implements StockCodeToNameMapper {

    private String url;

    public StockCodeToNameMapperByFinanceAPI(){
        this.url= "https://d1cz2i2cbiio5l.cloudfront.net/companyByStockCode?stockCode=";
    }

    @Override
    public String getStockName(String stockCode) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(this.url + stockCode);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if(status != 200){
                throw new Exception("finance api return status is not 200");
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode resBody = mapper.readTree(content.toString());
            return resBody.get("name").asText();
        } catch (Exception e) {
            return "undefined";
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
    }
}
