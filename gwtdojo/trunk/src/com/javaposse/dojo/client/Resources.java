package com.javaposse.dojo.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	
	@Source("dojowriter.css")
	Css css();
	
	
	@Source("background.jpg")
	ImageResource backgroundImage();

}
