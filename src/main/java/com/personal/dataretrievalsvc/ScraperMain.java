package com.personal.dataretrievalsvc;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ScraperMain {

    public static void main(String[] args) {
       try {

            Connection.Response response =  Jsoup.connect("https://portal.uspto.gov/pair/PublicPair")
                    .method(Connection.Method.GET)
                    .execute();

            //parse the document from response
           Map<String, String> headers = response.headers();

          Document document =  response.parse();
           Elements elements =  document.getAllElements();
         System.out.println(
                 "Elemens:"+ elements.size()

         );

           for(Element element: elements){
               System.out.println(element.tagName());
           }



            //get cookies


            /*
             * You may need to send all the cookies you received
             * from the post response to the subsequent requests.
             *
             * You can do that using cookies method of Connection
             */



        } catch (Exception e) {
           e.printStackTrace();
       }
    }
}
