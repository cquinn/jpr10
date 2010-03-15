package com.javaposse.dojo.client;

import java.util.List;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Dojowriter implements EntryPoint {
	
	final DocumentDataService dataService = GWT.create(DocumentDataService.class);
	
	

	@Override
	public void onModuleLoad() {
		 
		 dataService.init(new VoidCallback(){
			@Override
			public void onSuccess() {
				showList();
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Didn't Work");
			}
		 });
		
	}
	
	
	final SimplePanel mainPanel = new SimplePanel();
	final FlexTable documentsList = new FlexTable();
	
	private void showList(){
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
							showDocument(doc);	
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
	
	
	private void showDocument(final Document doc){
		final VerticalPanel vp = new VerticalPanel();
		final TextBox title = new TextBox();
		final RichTextArea text = new RichTextArea();
		final Button save = new Button("Save");
		vp.add(title);
		vp.add(text);
		vp.add(save);
		
		title.setValue(doc.getTitle());
		text.setText(doc.getText());
		
		
		SoundController controller = new SoundController();
		
		final Sound clickSound = controller.createSound(Sound.MIME_TYPE_AUDIO_MPEG, GWT.getModuleBaseURL()+"/Pop.mp3");
		
		text.addKeyDownHandler(new KeyDownHandler(){

			@Override
			public void onKeyDown(KeyDownEvent event) {
				clickSound.play();
			}
			
		});
		
		save.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				doc.setText(text.getText());
				doc.setTitle(title.getValue());
				
				if(doc.isNew()){
					createDocument(doc);
				} else {
					updateDocument(doc);
				}
			}
		});
		
		mainPanel.setWidget(vp);
	}
	
	
	private void createDocument(Document doc){
		
		dataService.createDocument(doc.getTitle(), doc.getText(), new RowIdListCallback(){

			@Override
			public void onSuccess(List<Integer> rowIds) {
				showList();	
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Failed to insert new document");
			}
			
		});
	}
	
	private void updateDocument(Document doc){
		dataService.updateDocument(doc.getId(), doc.getTitle(), doc.getText(), new VoidCallback(){

			@Override
			public void onSuccess() {
				showList();
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Failed to save document");
			}
			
		});
	}
	
	
	
	
	
	

}
