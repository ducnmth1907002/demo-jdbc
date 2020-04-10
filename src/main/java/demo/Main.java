package demo;

import demo.entity.CrawlerThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Bắt đầu lấy link");
            ArrayList<String> listLink = new ArrayList<String>();
            Document document = Jsoup.connect("https://vnexpress.net/").get();
            Elements aElements = document.select("a");
            if (aElements.size() == 0) {
                return;
            }
            for (int i = 0; i < aElements.size(); i++) {
                Element item = aElements.get(i);
                String link = item.attr("href");
                if (link.contains(".html") && !link.contains("#box_comment")) {
                    listLink.add(link);
                }
            }
            System.out.println("Tổng cộng có: " + listLink.size() + " links");
            ArrayList<CrawlerThread> listThread = new ArrayList<CrawlerThread>();
            for (int i = 0; i < listLink.size(); i++) {
                CrawlerThread crawler = new CrawlerThread(listLink.get(i));
                listThread.add(crawler);
                crawler.start();
            }
            for (CrawlerThread crawler : listThread) {
                crawler.join();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}


