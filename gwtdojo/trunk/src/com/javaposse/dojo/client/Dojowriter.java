package com.javaposse.dojo.client;

import java.util.List;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class Dojowriter implements EntryPoint {
	
	final DocumentDataService dataService = GWT.create(DocumentDataService.class);
	final Resources resources = GWT.create(Resources.class);
	

	@Override
	public void onModuleLoad() {
		 StyleInjector.inject(resources.css().getText());
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
		 
		 RootPanel.get().add(mainPanel);
		 RootPanel.getBodyElement().setClassName(resources.css().outer());
		 mainPanel.setStyleName(resources.css().mainPanel());
		 SimplePanel glassPane = new SimplePanel();
		 glassPane.setStyleName(resources.css().glassPane());
		 RootPanel.get().add(glassPane);
	}
	
	
	final FocusPanel mainPanel = new FocusPanel();
	final FlexTable documentsList = new FlexTable();
	
	private void showList(){
		
		documentsList.setWidth( "100%");
		
		dataService.getDocuments(new ListCallback<Document>(){
			@Override
			public void onSuccess(List<Document> result) {
				documentsList.clear();
				documentsList.removeAllRows();
				for(final Document doc : result){
					int row = documentsList.getRowCount();
					documentsList.insertRow(row);
					documentsList.addCell(row);
					Label l = new Label(doc.getTitle());
					documentsList.setWidget(row, 0, l);
					documentsList.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
					l.addClickHandler( new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							showDocument(doc);	
						}
						
					});
				}
				
				int row = documentsList.getRowCount();
				Button newButton = new Button("New");
				newButton.addClickHandler( new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						showDocument(createDocument()); 
					}
					
				});
				
				
				
				documentsList.insertRow(row);
				documentsList.setWidget(row, 0, newButton);
				documentsList.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
				
				
				mainPanel.setWidget(documentsList);
			}

			@Override
			public void onFailure(DataServiceException error) {
				Window.alert("Failed to get list of documents");
			}
		});
	}
	
	
	private final native Document createDocument()/*-{
		return { title:"New Document", content:"<html><head><style> * { font-family: Verdana; }</style></head></body></html>"};
	}-*/;
	
	
	private void showDocument(final Document doc){
		
		final FlowPanel vp = new FlowPanel();
		final TextBox title = new TextBox();
		title.setStyleName(resources.css().title());
		
		final RichTextArea text = new RichTextArea();
		text.setStyleName(resources.css().richText());
		final Button save = new Button("Save");
		save.setStyleName( resources.css().button());
		vp.add(title);
		vp.add(text);
		vp.add(save);
		vp.setWidth("100%");
		vp.setHeight("100%");
		
		title.setValue(doc.getTitle());
		text.setHTML(doc.getText());
		
		
		final Timer t = new Timer(){

			@Override
			public void run() {
				save.getElement().getStyle().setDisplay(Display.NONE);
				title.setStyleName(resources.css().titleNoBorder());
				text.setStyleName(resources.css().richTextNoBorder());
			}
			
		};
		
		final HandlerRegistration moveReg = mainPanel.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				t.cancel();
				t.schedule(2000);
			}
			
		});
		
		
		final HandlerRegistration overReg = mainPanel.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				save.getElement().getStyle().setDisplay(Display.BLOCK);
				title.setStyleName(resources.css().title());
				text.setStyleName(resources.css().richText());
			}
			
		});
		
		final HandlerRegistration overReg2 = mainPanel.addMouseOutHandler(new MouseOutHandler(){

			@Override
			public void onMouseOut(MouseOutEvent event) {
				save.getElement().getStyle().setDisplay(Display.NONE);
				title.setStyleName(resources.css().titleNoBorder());
				text.setStyleName(resources.css().richTextNoBorder());
			}

			
		});
		
		SoundController controller = new SoundController();
		controller.setPrioritizeFlashSound(true);
		final Sound clickSound = controller.createSound(Sound.MIME_TYPE_AUDIO_MPEG, GWT.getModuleBaseURL()+"/Pop.mp3");
		
		
		text.addKeyDownHandler(new KeyDownHandler(){

			@Override
			public void onKeyDown(KeyDownEvent event) {
				clickSound.play();
				save.getElement().getStyle().setDisplay(Display.NONE);
				title.setStyleName(resources.css().titleNoBorder());
				text.setStyleName(resources.css().richTextNoBorder());
			}
			
		});
		
		save.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				doc.setText(text.getHTML());
				doc.setTitle(title.getValue());
				overReg.removeHandler();
				overReg2.removeHandler();
				moveReg.removeHandler();
				if(doc.isNew()){
					createDocument(doc);
				} else {
					updateDocument(doc.getId(), title.getValue(), text.getHTML());
				}
			}
		});
		
		mainPanel.setWidget(vp);
	}
	
	
	private void createDocument(Document doc){
		dataService.createDocument(doc, new VoidCallback(){

			@Override
			public void onSuccess() {
				showList();	
			}

			@Override
			public void onFailure(DataServiceException error) {
				error.printStackTrace();
				Window.alert("Failed to insert new document");
				GWT.log(null, error);
			}
			
		});
	}
	
	private void updateDocument(int id, String title, String text){
		dataService.updateDocument(id, title, text, new VoidCallback(){

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
