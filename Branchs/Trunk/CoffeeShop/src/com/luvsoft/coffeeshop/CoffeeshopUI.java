package com.luvsoft.coffeeshop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.luvsoft.entities.Floor;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("coffeeshop")
public class CoffeeshopUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CoffeeshopUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		layout.addComponent(button);
		Floor floor = new Floor();
		floor.setId("1");
		floor.setCode("code1");
		floor.setName("1st Floor");
		List<String> tableIdList = new ArrayList<String>();
		tableIdList.add("Table 1");
		tableIdList.add("Table 2");
		tableIdList.add("Table 3");
		floor.setTableIdList(tableIdList);
		System.out.println(floor.toHashMap().toString());
	}

}