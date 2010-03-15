package com.javaposse.dojo.client;

import java.util.List;

import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class Dojowriter implements EntryPoint {
	
	final DocumentDataService dataService = GWT.create(DocumentDataService.class);
	
	

	@Override
	public void onModuleLoad() {
		 
		 dataService.init(new VoidCallback(){
			@Override
			public void onSuccess() {
				afterInit();
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Didn't Work");
			}
		 });
		
	}
	
	
	final SimplePanel mainPanel = new SimplePanel();
	final FlexTable documentsList = new FlexTable();
	
	private void afterInit(){
		dataService.getDocuments(new ListCallback<Document>(){
			@Override
			public void onSuccess(List<Document> result) {
				documentsList.clear();
				for(final Document doc : result){
					int row = documentsList.getRowCount();
					documentsList.insertRow(row);
					documentsList.addCell(row);
					Label l = new Label(doc.getTitle());
					documentsList.setWidget(row, 0, l);
					l.addClickHandler( new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							showDocument(doc.getId());	
						}
						
					});
				}
				mainPanel.setWidget(documentsList);
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Failed to get list of documents");
			}
		});
	}
	
	

}
