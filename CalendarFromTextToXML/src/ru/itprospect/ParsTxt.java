package ru.itprospect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ParsTxt {
	
	private String FileNameCatholic;
	private String FileNameCatholicXML;
	private ArrayList<BookFromSite> AllBook;

	public ParsTxt(String FileNameCatholic) {
		this.FileNameCatholic = FileNameCatholic;
		FileNameCatholicXML = FileNameCatholic + ".xml";
	}
	
	public void ParsIt() {
		AllBook = new ArrayList<BookFromSite>();
		
		//Чтение текстового файла
		try{
			FileInputStream fstream = new FileInputStream("FileNameCatholic");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null){
				System.out.println(strLine);
				
				
				
			}
		}catch (IOException e){
			System.out.println("Ошибка");
		}

		
//		ArrayList<BookFromSite> thisBook = ReadingsToBooks.StringToBooks(linkText, curDate, type, alt);
//		AllBook.addAll(thisBook);
		
		
		
		XMLWriter xmlr = new XMLWriter(AllBook);
		try {
			xmlr.write(FileNameCatholicXML);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
