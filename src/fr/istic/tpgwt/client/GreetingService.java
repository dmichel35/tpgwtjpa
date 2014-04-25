package fr.istic.tpgwt.client;

import java.util.List;

import fr.istic.tpgwt.shared.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	public Person savePerson(Person p);
	
	public List<Person> listAllPerson();
	
	public Article saveArticle(String title, String content, String idAuthor) throws Exception;

	public List<Article> listAllArticles();
	
}
