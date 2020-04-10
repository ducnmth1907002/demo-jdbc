package demo.entity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CrawlerThread extends Thread{
    private String url;

    public CrawlerThread(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            System.out.println("Bắt đầu lấy tin từ url: " + this.url);
            Document document = Jsoup.connect(url).get();
            String title = document.select("h1.title-detail").text();
            String description = document.select("p.description").text();
            String content = document.select("article.fck_detail").text();
            Article article = new Article();
            article.setUrl(url);
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            article.setSource("Vnexpress");
            article.setStatus(1);
            insertArticle(article);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void insertArticle(Article article) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/t1908_jdbc?useUnicode=true&serverTimezone=Asia/Bangkok&characterEncoding=utf-8", "root", "");
            String queryString = "INSERT INTO article" + "(id, url, title, description, content, source, status)" + "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, article.getId());
            preparedStatement.setString(2, article.getUrl());
            preparedStatement.setString(3, article.getTitle());
            preparedStatement.setString(4, article.getDescription());
            preparedStatement.setString(5, article.getContent());
            preparedStatement.setString(6, article.getSource());
            preparedStatement.setInt(7, article.getStatus());
            preparedStatement.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}