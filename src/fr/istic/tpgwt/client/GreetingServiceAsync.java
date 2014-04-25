package fr.istic.tpgwt.client;

import java.util.List;

import fr.istic.tpgwt.shared.*;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	public void savePerson(Person p, AsyncCallback<Person> callback);
	
	public void listAllPerson(AsyncCallback<List<Person>> callback);
	
	public void saveArticle(String title, String content, String idAuthor, AsyncCallback<Article> callback);
	
	public void listAllArticles(AsyncCallback<List<Article>> callback);
	
}
