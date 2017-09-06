package crawler;

public class Url {
   private String url;
   private int depth;
   
  Url(String url,int depth)
  {
      this.url = url;
      this.depth = depth;
  }
  
  Url(Url url)
  {
      this.url = url.url;
      this.depth = url.depth;
  }
  
  public int getDepth()
  {
      return depth;
  }
  
  public String getUrl()
  {
      return url;
  }
  
  
    
}
