package fr.istic.tpgwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Grid;

import fr.istic.tpgwt.shared.Person;

public class PersonsGrid extends Grid {

	private List<Person> persons = new ArrayList<Person>();

	public PersonsGrid() {
		super();
		this.setStyleName("bordered");
	}	

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons.clear();
		this.persons.addAll(persons);
		this.build();
	}
	
	public void addPerson(Person person) {
		this.persons.add(person);
		this.build();
	}
	
	public void build() {
		this.resize(persons.size()+1, 5);
		this.setText(0, 0, "Name");
		this.setText(0, 1, "First Name");
		this.setText(0, 2, "Address");
		this.setText(0, 3, "CP");
		this.setText(0, 4, "City");
		for(int i=0; i < persons.size(); i++) {
			Person p = persons.get(i);
			this.setText(i+1, 0, p.getName());
			this.setText(i+1, 1, p.getFirstName());
			this.setText(i+1, 2, p.getAddress());
			this.setText(i+1, 3, p.getCP());
			this.setText(i+1, 4, p.getCity());
		}
		this.getCellFormatter().setWidth(0, 2, "256px");
	}

}
