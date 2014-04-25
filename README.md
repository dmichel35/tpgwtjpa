# TP SIR - GWT

L'architecture de l'application est composée de trois packages :

* Client
* Shared
* Server


### Package Shared

On retrouve dans ce package les objets métiers manipulés par l'application, à la fois côté serveur et côté client : Person et Article.

* La classe Person dispose d'une relation OneToMany vers une collection d'objets Article

 
 ```java
@Entity
public class Person implements Serializable {
	
	private String id;
	private String name;
	private String firstName;
	private String address;
	private String city;
	private String CP;
	private ArrayList<Article> articles = new ArrayList<Article>();
	
	public Person() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	public String getId() {
		return id;
	}
	
    ...
    
	@OneToMany(mappedBy="author", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	public ArrayList<Article> getArticles() {
		return articles;
	}
    
    ...

}
```

 * La Article contient différents attributs dont un objet de type Date et une relation de type ManyToOne vers un objet Person


 ```java
@Entity
public class Article implements Serializable {
	
	private String id;
	private String title;
	private String content;
	private Date date;
	private Person author;

	public Article() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	public String getId() {
		return id;
	}

	...

	@Basic
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	...

	@ManyToOne
	public Person getAuthor() {
		return author;
	}

}

```

### Package Client

Ce package contient :

* une classe implémentant l'interface EntryPoint de GWT permettant de mettre en place l'interface graphique qui sera présentée à l'utilisateur
* différents widgets graphiques
* des interfaces décrivant les services disponibles sur le serveur

Ainsi, il existe une interface GreetingService, héritant de RemoteService, décrivant les différents méthodes d'un service disponible sur le serveur :

 ```java
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	public Person savePerson(Person p);
	
	public List<Person> listAllPerson();
	
	public Article saveArticle(String title, String content, String idAuthor) throws Exception;

	public List<Article> listAllArticles();
	
}

```

Une version asynchrone de cette interface est également disponible :

 ```java
public interface GreetingServiceAsync {
	
	public void savePerson(Person p, AsyncCallback<Person> callback);
	
	public void listAllPerson(AsyncCallback<List<Person>> callback);
	
	public void saveArticle(String title, String content, String idAuthor, AsyncCallback<Article> callback);
	
	public void listAllArticles(AsyncCallback<List<Article>> callback);
	
}
```

Voici un exemple d'utilisation de cette interface afin de créer un nouvel article :

 ```java
b2.addClickHandler(new ClickHandler() {
			
	public void onClick(ClickEvent event) {
							  
		greetingService.saveArticle(txtTitle.getText(), txtContent.getText(),
           authorList.getValue(authorList.getSelectedIndex()), 
           new AsyncCallback<Article>() {
				public void onFailure(Throwable caught) {
					// Affichage de l'erreur
					Window.alert("Request Failed: "+caught.getMessage());
				}
				@Override
				public void onSuccess(Article result) {
					Window.alert("Article created!");
					// Ajout de l'article dans le tableau
					articlesGrid.addArticle(result);
				}
			});
				
	}
			
});
```
Ainsi, un clic sur le bouton "b2" déclenche un appel asynchrone à la fonction saveArticle() définie dans l'interface GreetingService. Un objet Article est alors créé côté serveur et renvoyé au client où il sera affiché dans un tableau.

### Package Server

Le package Server contient une classe GreetingServiceImpl implémentant l'interface GreetingService définie dans le package Client et héritant de la classe RemoteServiceServlet de GWT.

Cette classe utilise une instance de EntityManager afin d'effectuer des requêtes sur la base de données et de persister en base les objets issus des classes annotées par des annotations JPA.

 ```java
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private EntityManager manager;
	
	@Override
	public void init() throws ServletException {
		super.init();
		manager = EMF.get().createEntityManager();
	}
	
	public Person savePerson(Person p) {
		EntityTransaction t =  manager.getTransaction();
		if (!t.isActive()) t.begin();
		manager.persist(p);
		t.commit();
		manager.detach(p);
		return p;
	}
	
	public List<Person> listAllPerson(){
		List<Person> res = new ArrayList<>();
		res.addAll(manager.createQuery("select p from Person as p").getResultList());
		for (Person p : res) manager.detach(p);
		return res;
	}
	
	public Article saveArticle(String title, String content, String idAuthor) throws Exception {
		if (title == null || title.equals("")) {
			throw new Exception("An article needs a title!");
		}
		if (idAuthor.equals("")) {
			throw new Exception("An article needs an author");
		}
		List<Person> result =  manager.createQuery(
        	"select a from Person as a where a.id = '"+idAuthor+"'"
        ).getResultList();
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

	public List<Article> listAllArticles() {
		List<Article> res = new ArrayList<>();
		res.addAll(manager.createQuery("select a from Article as a").getResultList());
		for (Article a : res) manager.detach(a);
		return res;
	}

}

```

### Déploiement

L'application est actuellement déployée sur Google App Engine et accessible à l'adresse suivante :

http://1-dot-tpgwtjpa.appspot.com/
