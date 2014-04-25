package fr.istic.tpgwt.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.istic.tpgwt.shared.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tpgwtjpa implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private PersonsGrid personsGrid;
	private ArticlesGrid articlesGrid;
	
	private TextBox txtName = new TextBox();
	private TextBox txtFirstName = new TextBox();
	private TextBox txtAddress = new TextBox();
	private TextBox txtCity = new TextBox();
	private TextBox txtCP = new TextBox();
	
	private TextBox txtTitle = new TextBox();
	private TextArea txtContent = new TextArea();
	private ListBox authorList = new ListBox();
	
	private VerticalPanel pg = new VerticalPanel();
	private VerticalPanel pl = new VerticalPanel();
	private VerticalPanel pr = new VerticalPanel();
	private HorizontalPanel mainPanel = new HorizontalPanel();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		// Création du tableau des personnes
		personsGrid = new PersonsGrid();
		pg.add(personsGrid);
		
		// Récupération des personnes sur le serveur
		greetingService.listAllPerson(new AsyncCallback<List<Person>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Request Failed: "+caught.getMessage());
			}
			@Override
			public void onSuccess(List<Person> result) {
				if (result != null) {
					// Affichage des personnes dans le tableau
					personsGrid.setPersons(result);
					// Affichage des personnes dans la liste des auteurs
					authorList.clear();
					for(Person p : result) {
						authorList.addItem(p.getName()+" "+p.getFirstName(), p.getId());
					}
				}
			}
		});
		
		// Création du formulaire d'ajout d'une personne
		FormPanel formPerson = new FormPanel(100);
		formPerson.addField("Name", txtName);
		formPerson.addField("First Name", txtFirstName);
		formPerson.addField("Address", txtAddress);
		formPerson.addField("CP", txtCP);
		formPerson.addField("City", txtCity);
		
		// Création du bouton d'enregristement de la personne
		HorizontalPanel p6 = new HorizontalPanel();
		Button b = new Button("create");
		p6.add(b);
		p6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	
		formPerson.add(p6);
		
		pl.add(pg);
		pl.add(formPerson);
		mainPanel.add(pl);	
		
		b.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				
				Person p = new Person();
				p.setName(txtName.getText());
				p.setFirstName(txtFirstName.getText());
				p.setCity(txtCity.getText());
				p.setCP(txtCP.getText());
				p.setAddress(txtAddress.getText());
			  
				greetingService.savePerson(p, new AsyncCallback<Person>() {
				    public void onFailure(Throwable caught) {
				    	// Affichage de l'erreur
				    	Window.alert("Request Failed: "+caught.getMessage());
				    }
					@Override
					public void onSuccess(Person result) {
						Window.alert("Person created!");
						// Ajout de la nouvelle personne au tableau
						personsGrid.addPerson(result);
						// Ajout de la nouvelle personne à la liste des auteurs
						authorList.addItem(result.getName()+" "+result.getFirstName(), result.getId());
					}
				});
				
			}
			
		});
		
		// Création du tableau des articles
		articlesGrid = new ArticlesGrid();
		pr.add(articlesGrid);
		
		greetingService.listAllArticles(new AsyncCallback<List<Article>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Request Failed: "+caught.getMessage());
				
			}
			@Override
			public void onSuccess(List<Article> result) {
				// Affichage des articles dans le tableau
				articlesGrid.setArticles(result);
			}
		});
		
		// Création du formulaire d'ajout d'un article
		FormPanel formArticle = new FormPanel(100);
		formArticle.addField("Title", txtTitle);
		formArticle.addField("Author", authorList);
		formArticle.add(txtContent);
		txtContent.setHeight("300px");
		
		// Création du bouton d'enregistrement de l'article
		HorizontalPanel p7 = new HorizontalPanel();
		Button b2 = new Button("create");
		p7.add(b2);
		p7.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		formArticle.add(p7);
		
		pr.add(formArticle);
		
		b2.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
							  
				greetingService.saveArticle(txtTitle.getText(), txtContent.getText(), authorList.getValue(authorList.getSelectedIndex()), new AsyncCallback<Article>() {
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
		
		mainPanel.add(pr);
		mainPanel.setStyleName("mainPanel");
		RootPanel.get().add(mainPanel);
		
	}

}
