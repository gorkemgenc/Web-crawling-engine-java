package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Bu class verilen url adresini crawl eder.

public class Spider {
    private static final String USER_AGENT =
	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<Url> links = new LinkedList<Url>();
    private Document htmlDocument;
    private String urlNews;
    private String titleNews;
    private StringBuilder textNews;
    private Url url;
    
    public boolean crawl(Url url)
    {
        try
        {
            
            Connection connection = Jsoup.connect(url.getUrl()).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            
            this.htmlDocument = htmlDocument;
            this.url = url;
            
            titleNews = htmlDocument.title();
            textNews = new StringBuilder();
            
            // aljazeera
            if(url.getUrl().contains("aljazeera"))
            {
                urlNews = url.getUrl();
                titleNews = htmlDocument.title();
                // Üstteki büyük yazı
                Elements mainSentence = htmlDocument.select("article#main-story").select("h2");
                mainSentence.stream().forEach((e) -> {
                    if(!e.text().contains("facebook.com") && !e.text().contains("src"))
                        textNews.append(e.text().trim()).append("\n");
                });
                
                // Resmin altındaki yazı
                Elements pictureSentence = htmlDocument.select("figure.article-main-image").select("figcaption");
                pictureSentence.stream().forEach((e) -> {
                    if(!e.text().contains("facebook.com") && !e.text().contains("src"))
                        textNews.append(e.text().trim()).append("\n");
                });
                
                // Alttaki yazılar
                Elements paragraphs = htmlDocument.select("div#article-body").select("p");
                paragraphs.stream().forEach((paragraph) -> {
                    if(!paragraph.toString().contains("iframe") && !paragraph.toString().contains("<span"))
                        textNews.append(paragraph.text()).append("\n");
                });
            }
            
            // reuters
            if(url.getUrl().contains("reuters"))
            {
                urlNews = url.getUrl();
                titleNews = htmlDocument.title();
                // Üstteki büyük yazı
                Elements mainSentence = htmlDocument.select("div.related-photo-caption");
                mainSentence.stream().forEach((e) -> {
                    if(!e.text().contains("facebook.com") && !e.text().contains("src"))
                        textNews.append(e.text().trim()).append("\n");
                });
                
                // Alttaki yazılar
                Elements paragraphs = htmlDocument.select("span#articleText").select("p");
                paragraphs.stream().forEach((paragraph) -> {
                    if(!paragraph.text().contains("facebook.com") && !paragraph.text().contains("src"))
                        textNews.append(paragraph.text()).append("\n");
                });
            }
            
            // nytimes
            if(url.getUrl().contains("nytimes"))
            {
                urlNews = url.getUrl();
                titleNews = htmlDocument.title();
                // Üstteki büyük yazı
                Elements mainSentence = htmlDocument.select("span.caption-text");
                mainSentence.stream().forEach((e) -> {
                    if(!e.text().contains("facebook.com") && !e.text().contains("src"))
                        textNews.append(e.text().trim()).append("\n");
                });
                
                // Alttaki yazılar
                Elements paragraphs = htmlDocument.select("div#story-body").select("p");
                paragraphs.stream().forEach((paragraph) -> {
                    if(!paragraph.text().contains("facebook.com") && !paragraph.text().contains("src"))
                        textNews.append(paragraph.text()).append("\n");
                });
            }
            
            // bbc
            if(url.getUrl().contains("bbc"))
            {
                urlNews = url.getUrl();
                titleNews = htmlDocument.title();
                // Üstteki büyük yazı
                Elements mainSentence = htmlDocument.select("div.story-body").select("p.story-body__introduction");
                mainSentence.stream().forEach((e) -> {
                    if(!e.text().contains("facebook.com") && !e.text().contains("src"))
                        textNews.append(e.text().trim()).append("\n");
                });
                
                // Alttaki yazılar
                Elements paragraphs = htmlDocument.select("div#story-body").select("p");
                paragraphs.stream().forEach((paragraph) -> {
                    if(!paragraph.text().contains("facebook.com") && !paragraph.text().contains("src"))
                        textNews.append(paragraph.text()).append("\n");
                });
            }
            
            Elements linksOnPage = htmlDocument.select("a[href]");

            for (Element link : linksOnPage)
            {
                if (link.attr("href").compareTo("") != 0 && link.attr("href").charAt(0) != '#' && link.attr("href").compareTo("") != 0)
                {
                    if(link.absUrl("href").contains("www.aljazeera.com") || link.absUrl("href").contains("www.reuters.com") || link.absUrl("href").contains("www.nytimes.com") || link.absUrl("href").contains("www.bbc"))
                    {
                        /*if(!link.absUrl("href").contains("video") && !link.absUrl("href").contains("contact") && !link.absUrl("href").contains("help") && !link.absUrl("href").contains("watch_now"))*/
                        //{
                            if(!link.absUrl("href").contains(".tr"))
                            {
                                this.links.add(new Url(link.absUrl("href"), url.getDepth()+1));
                            }
                        //}
                    }
                }
            }
            
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
    
    public void saveHtml(int x)
    {
        if(this.htmlDocument != null && this.htmlDocument.body() != null)
        {       
            StringBuilder finalNews = new StringBuilder();
            if(!urlNews.trim().equals(""))
                finalNews.append(urlNews).append("\n");
            if(!titleNews.trim().equals(""))
                finalNews.append(titleNews).append("\n");
            if(!textNews.toString().trim().equals(""))
                finalNews.append(textNews.toString()).append("\n");

            if(this.url.getDepth() != 0)
            {
                String filePath = "";
                switch (x) {
                    case 1:
                        filePath = "logs1";
                        break;
                    case 2:
                        filePath = "logs2";
                        break;
                    case 3:
                        filePath = "logs3";
                        break;
                    case 4:
                        filePath = "logs4";
                        break;
                    default:
                        break;
                }

                Path path = Paths.get(System.getProperty("user.dir"), "data", filePath, titleNews + ".txt");
                String pathStr = path.toString();

                File file = new File(pathStr);

                try 
                {
                    if(!file.exists())
                    {
                        file.createNewFile();

                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    //bw.write(textNews.toString());
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        //bw.write(textNews.toString());
                        bw.write(finalNews.toString());
                    }
                } 
                catch (IOException ioe) 
                {
                }
            }
        }
    }
    
    public List<Url> getLinks()
    {
        return this.links;
    }
}
