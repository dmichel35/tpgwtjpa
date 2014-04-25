package fr.istic.tpgwt.client;

import java.util.ArrayList;
import java.util.List;

import fr.istic.tpgwt.shared.Article;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;

public class ArticlesGrid extends Grid {

	private List<Article> articles = new ArrayList<Article>();
	
	public ArticlesGrid() {
		super();
		this.setStyleName("bordered");
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles.clear();
		this.articles.addAll(articles);
		this.build();
	}
	
	public void addArticle(Article a) {
		this.articles.add(a);
		this.build();
	}
	
	public void build() {
		this.resize(articles.size()+1, 3);
		this.setText(0, 0, "Title");
		this.setText(0, 1, "Date");
		this.setText(0, 2, "Author");
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd");
		for(int i=0; i < articles.size(); i++) {
			Article a = articles.get(i);
			this.setText(i+1, 0, a.getTitle());
			this.setText(i+1, 1, fmt.format(a.getDate()));
			this.setText(i+1, 2, a.getAuthor().getFirstName()+" "+a.getAuthor().getName());
		}
	}
	
}
