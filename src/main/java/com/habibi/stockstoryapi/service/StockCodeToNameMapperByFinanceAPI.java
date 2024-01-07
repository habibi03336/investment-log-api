package com.habibi.stockstoryapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.HttpsURLConnectionWrapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class StockCodeToNameMapperByFinanceAPI implements StockCodeToNameMapper {

    private String url;

    public StockCodeToNameMapperByFinanceAPI(){
        this.url= "https://d1cz2i2cbiio5l.cloudfront.net/companyByStockCode?stockCode=";
    }

    @Override
    public String getStockName(String stockCode) {
        try {
            HttpsURLConnection con = makeConnection(this.url + stockCode);
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if(status != 200){
                throw new Exception("finance api return status is not 200");
            }
            JsonNode resBody = new ObjectMapper().readTree(getInputFrom(con));
            return resBody.get("name").asText();
        } catch (Exception e) {
            return "undefined";
        }
    }

    public HttpsURLConnection makeConnection(String url) throws IOException {
        try (
                HttpsURLConnectionWrapper connectionWrapper =
                        new HttpsURLConnectionWrapper(new URL(url))
        ) {
            HttpsURLConnection con = connectionWrapper.getConnection();
            return con;
        }
    }

    public String getInputFrom(HttpsURLConnection con) throws IOException {
        String inputLine;
        StringBuffer content = new StringBuffer();
        try (
                BufferedReader in
                        = new BufferedReader(new InputStreamReader(con.getInputStream()))
        ){
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        return content.toString();
    }
}
