package crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.Enums;

public class Crawler {

    private Url initialUrl;
    private int maxDepth;
    private int maxPages;
    private int collectionId = 0;
    private int x;
    
    private int docId = 0;

    private Set<Url> pagesVisited = new HashSet<Url>();
    private List<Url> pagesToVisit = new LinkedList<Url>();
    
    
    public Crawler(String url, int depth,int pages,int x)
    {
        this.initialUrl = new Url(url,0);
        this.maxDepth = depth;
        this.maxPages = pages;
        this.x = x;
    }
    
    public void collect(int whichNews)
    {   	
        while (this.pagesVisited.size() < this.maxPages) 
        {
            Url currentUrl;
            Spider spider = new Spider();

            if (this.pagesToVisit.isEmpty()) {
                    currentUrl = this.initialUrl;
                    this.pagesVisited.add(currentUrl);
            } else {
                    currentUrl = this.nextUrl();
            }

            // Maximum depth control
            if (currentUrl.getDepth() > this.maxDepth) {
                    return;
            }
                
            spider.crawl(currentUrl);
            spider.saveHtml(whichNews);
            this.pagesVisited.add(currentUrl);
            this.pagesToVisit.addAll(spider.getLinks());  
        }
    }
    
    private Url nextUrl() 
    {
        Url nextUrl;

        do {
                nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));

        return nextUrl;
    }
    
    	
    public static void main(String[] args) {
        
        String url1 = "http://www.aljazeera.com";
        String url2 = "http://www.reuters.com";
        String url3 = "http://www.nytimes.com";
        String url4 = "http://www.bbc.com";
        
        int maxDepth = 3;
        int maxPages = 2000;

        // Logları aldım artık
        Crawler crawler1 = new Crawler(url1,maxDepth,maxPages,1);
        crawler1.collect(1);
        Crawler crawler2 = new Crawler(url2,maxDepth,maxPages,2);
        crawler2.collect(2);
        Crawler crawler3 = new Crawler(url3,maxDepth,maxPages,3);
        crawler3.collect(3);
        Crawler crawler4 = new Crawler(url4, maxDepth, maxPages,4);
        crawler3.collect(4);
    }
    
   
}
