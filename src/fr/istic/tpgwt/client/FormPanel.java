package fr.istic.tpgwt.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormPanel extends VerticalPanel {
	
	private String labelWidth;
	
	public FormPanel(int labelWidth) {
		super();
		this.labelWidth = Integer.toString(labelWidth);
	}
	
	public void addField(String label, Widget w) {
		HorizontalPanel p = new HorizontalPanel();
		Label l = new Label(label);
		p.add(l);
		p.add(w);
		p.setCellWidth(l, labelWidth);
		this.add(p);
	}
	
}
