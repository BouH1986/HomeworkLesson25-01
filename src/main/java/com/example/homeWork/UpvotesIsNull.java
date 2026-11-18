package com.example.homeWork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class UpvotesIsNull {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                            .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                            .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                            .build())
                    .build();

            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                System.out.println("HTTP статус код: " + response.getStatusLine().getStatusCode());
                System.out.println();

                String jsonResponse = EntityUtils.toString(response.getEntity());

                //json -> Object

                List<Post> postsAll = mapper.readValue(jsonResponse, new TypeReference<>() {
                });

                List<Post> posts = postsAll.stream().filter(value -> value.getUpvotes() == null).toList();

                for (int i = 0; i < Math.min(2, posts.size()); i++) {
                    Post post = posts.get(i);
                    System.out.println("Результат №" + (i + 1) + ": ");
                    System.out.println("ID: " + post.getId());
                    System.out.println("Text: " + post.getText());
                    System.out.println("Type: " + post.getType());
                    System.out.println("User: " + post.getUser());
                    System.out.println("Upvotes: " + post.getUpvotes());
                    System.out.println();
                    if (posts.size() > 2) {
                        System.out.println("...");
                    }
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}


