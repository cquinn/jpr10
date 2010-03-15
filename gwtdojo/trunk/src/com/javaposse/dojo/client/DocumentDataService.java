package com.javaposse.dojo.client;

import com.google.code.gwt.database.client.service.Connection;
import com.google.code.gwt.database.client.service.DataService;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.Select;
import com.google.code.gwt.database.client.service.Update;
import com.google.code.gwt.database.client.service.VoidCallback;


@Connection(name="Document", version="1.0	",
	    description="DocumentsDatabase", maxsize=10000)
public interface DocumentDataService extends DataService {

  @Update("CREATE TABLE IF NOT EXISTS documents ("
		      + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		      + "title TEXT, content TEXT)")
  void init(VoidCallback callback);
  
  @Select("SELECT * FROM documents ORDER BY id")
  void getDocuments(ListCallback<Document> callback);
  
 
  @Update("INSERT INTO documents (title, content) VALUES ( {document.getTitle()}, {document.getText()}  )")
  void createDocument(Document document, VoidCallback callback );
  
  @Update("UPDATE documents SET title = {document.getTitle()}, content = {document.getText()} WHERE id = {document.getId()}")
  void updateDocument(Document document, VoidCallback callback);
	
}
