package fr.istic.tpgwt.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;

import fr.istic.tpgwt.client.GreetingService;
import fr.istic.tpgwt.shared.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private EntityManager manager;
	
	@Override
	public void init() throws ServletException {
		super.init();
		manager = EMF.get().createEntityManager();
	}
	
	public Person savePerson(Person p) {
		EntityTransaction t =  manager.getTransaction();
		if (!t.isActive())
			t.begin();
		manager.persist(p);
		t.commit();
		manager.detach(p);
		return p;
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> listAllPerson(){
		List<Person> res = new ArrayList<>();
		res.addAll(manager.createQuery("select p from Person as p").getResultList());
		for (Person p : res) manager.detach(p);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public Article saveArticle(String title, String content, String idAuthor) throws Exception {
		if (title == null || title.equals("")) {
			throw new Exception("An article needs a title!");
		}
		if (idAuthor.equals("")) {
			throw new Exception("An article needs an author");
		}
		List<Person> result =  manager.createQuery("select a from Person as a where a.id = '"+idAuthor+"'").getResultList();
		if (result.isEmpty()) {
			throw new Exception("This is not an author!");
		}
		Article a = new Article();
		a.setAuthor(result.get(0));
		a.setTitle(title);
		a.setContent(content);
		a.setDate(new Date());
		EntityTransaction t =  manager.getTransaction();
		if (!t.isActive()) t.begin();
		manager.persist(a);
		t.commit();
		manager.detach(a);
		return a;
	}

	@SuppressWarnings("unchecked")
	public List<Article> listAllArticles() {
		List<Article> res = new ArrayList<>();
		res.addAll(manager.createQuery("select a from Article as a").getResultList());
		for (Article a : res) manager.detach(a);
		return res;
	}

}
