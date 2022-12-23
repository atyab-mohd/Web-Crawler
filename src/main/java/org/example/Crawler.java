package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;

public class Crawler {
    private HashSet<String> urlLink;
    private int max_depth = 2;
    public Connection connection;
    public Crawler(){
        //Set up the connection to MySQL
        connection = (Connection) DatabaseConnection.getConnection();
        urlLink = new HashSet<String>();
    }
    public void getPagesTextsAndLinks(String url, int depth){
        if(!urlLink.contains(url)){
            if(urlLink.add(url)){
                System.out.println(url);
            }
            try {
                Document document = Jsoup.connect(url).timeout(5000).get();
                String text = document.text().length()<500? document.text(): document.text().substring(0,499);
                System.out.println(text);
                //insert data into pages table
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages Values(?,?,?)");
                preparedStatement.setString(1, document.title());
                preparedStatement.setString(2, url);
                preparedStatement.setString(3, text);
                preparedStatement.executeUpdate();


                depth++;
                if(depth > max_depth){
                    return;
                }
                Elements availableLinksOnPage = document.select("a[href]");
                for(Element currentLink : availableLinksOnPage){
                    getPagesTextsAndLinks(currentLink.attr("abs:href"), depth);
                }
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.getPagesTextsAndLinks("https://www.javatpoint.com/", 0);
    }
}