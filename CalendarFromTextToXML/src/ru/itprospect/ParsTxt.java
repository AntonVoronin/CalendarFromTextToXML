package ru.itprospect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
		Date currDate=null;
		int a = 0;
		
		//Чтение текстового файла
		try{
			FileInputStream fstream = new FileInputStream(FileNameCatholic);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null){
				a++;
				System.out.println(strLine);
				//TODO Анализ текстового файла
				if (strLine.startsWith("Дата: ")) {
					String strDate = strLine.substring(6, 16);
					System.out.println(strDate);
					
					SimpleDateFormat format = new SimpleDateFormat();
					format.applyPattern("yyyy-MM-dd");
					currDate = format.parse(strDate);
				}
				else if (a<2) {
					continue;
				}
				else if(strLine.length()>0){
					String textType = "";
					String textRead = strLine;
					int indexType = strLine.lastIndexOf("   ");
					if (indexType>0) {
						textType = strLine.substring(indexType+3);
						textRead = strLine.substring(0, indexType);
					} 
					
					//Добавляем книгу
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(currDate);
					ArrayList<BookFromSite> thisBook = ReadingsToBooks.StringToBooks(textRead, cal, textType, 0);
					AllBook.addAll(thisBook);
				}
			}
			br.close();
		}catch (IOException | ParseException e){
			System.out.println("Ошибка");
		}

		
		
		
		
		XMLWriter xmlr = new XMLWriter(AllBook);
		try {
			xmlr.write(FileNameCatholicXML);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
}
