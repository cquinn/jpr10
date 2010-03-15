package com.javaposse.dojo.client;

import com.google.gwt.core.client.JavaScriptObject;

public class Document extends JavaScriptObject {

	protected Document(){
		
	}
	
	public final native String getText()/*-{
		return this.content;
	}-*/;
	
	public final native void setText(String text)/*-{
		this.content = text;
	}-*/;
	
	public final native int getId() /*-{
		return this.id === undefined ? -1 : this.id;
	}-*/;
	
	public final boolean isNew(){
		return this.getId() == -1;
	}
	
	public final native String getTitle()/*-{
		return this.title;
	}-*/;

	public final native void setTitle(String title)/*-{
		this.title = tile;
	}-*/;
	
	
}
