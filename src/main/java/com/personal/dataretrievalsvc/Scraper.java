package com.personal.dataretrievalsvc;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class Scraper {

    public static void main(String[] args) {

        try{

            Document doc =  Jsoup.connect("https://www.codetriage.com/?language=Java")
                            .userAgent("Mozilla/5.0")
                            .timeout(10 * 1000)
                            .method(Method.POST)
                            .data("txtloginid", "YOUR_LOGINID")
                            .data("txtloginpassword", "YOUR_PASSWORD")
                            .data("random", "123342343")
                            .data("task", "login")
                            .data("destination", "/welcome")
                            .followRedirects(true)
                            .get();

            //parse the document from response

            System.out.println(doc);

            //get cookies


            /*
             * You may need to send all the cookies you received
             * from the post response to the subsequent requests.
             *
             * You can do that using cookies method of Connection
             */



        }catch(IOException ioe){
            System.out.println("Exception: " + ioe);
        }

    }
}
