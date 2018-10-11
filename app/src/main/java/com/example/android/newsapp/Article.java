package com.example.android.newsapp;

class Article {
    private final String articleTitle;
    private final String articlePublishDate;
    private final String articleTopic;
    private final String byLine;
    private final String trailText;
    private final String articleUrl;

    public Article(
            String articleTitle, String articlePublishDate,
            String articleTopic, String articleUrl,
            String byLine, String trailText) {

        this.articleTitle = articleTitle;
        this.articlePublishDate = articlePublishDate;
        this.articleTopic = articleTopic;
        this.articleUrl = articleUrl;
        this.byLine = byLine;
        this.trailText = trailText;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticlePublishDate() {
        return articlePublishDate;
    }

    public String getArticleTopic() {
        return articleTopic;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getByLine() {
        return byLine;
    }

    public String getTrailText() {
        return trailText;
    }
}
