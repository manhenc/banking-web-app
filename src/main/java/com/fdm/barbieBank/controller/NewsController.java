package com.fdm.barbieBank.controller;

import com.fdm.barbieBank.service.NewsService;
import com.fdm.barbieBank.utils.Article;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String getNews(Model model) {
        String json = newsService.getTopHeadlines();
        List<Article> articleList = parseArticles(json);

        model.addAttribute("articles", articleList);
        return "news";
    }

    @GetMapping("/news/search")
    public ResponseEntity<List<Article>> searchNews(Model model,
                             @RequestParam(required = false) String q,
                             @RequestParam(required = false) String searchIn,
                             @RequestParam(required = false) String sources,
                             @RequestParam(required = false) String domains,
                             @RequestParam(required = false) String excludeDomains,
                             @RequestParam(required = false) String from,
                             @RequestParam(required = false) String to,
                             @RequestParam(required = false) String language,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(defaultValue = "100") int pageSize,
                             @RequestParam(defaultValue = "1") int page) {

        NewsService.SearchParameters parameters = new NewsService.SearchParameters(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, page);
        String json = newsService.searchArticles(parameters);
        List<Article> articleList = parseArticles(json);

        return ResponseEntity.ok(articleList);
    }
    
    private List<Article> parseArticles(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray articles = jsonObject.getJSONArray("articles");

        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < articles.length(); i++) {
            JSONObject articleJSON = articles.getJSONObject(i);

            Article article = new Article();
            article.setAuthor(extractString(articleJSON, "author"));
            article.setContent(extractString(articleJSON, "content"));
            article.setTitle(articleJSON.getString("title"));
            article.setDescription(extractString(articleJSON, "description"));
            article.setUrl(articleJSON.getString("url"));
            article.setUrlToImage(extractString(articleJSON, "urlToImage"));
            String publishedAt = articleJSON.getString("publishedAt");
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = fromFormat.parse(publishedAt);
                article.setPublishedAt(toFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
                article.setPublishedAt(publishedAt);
            }

            articleList.add(article);
        }

        return articleList;
    }

    private String extractString(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        if (value != null && value instanceof String) {
            return (String) value;
        } else {
            return "";
        }
    }
}






