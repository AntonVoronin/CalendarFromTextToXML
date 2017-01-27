package ru.itprospect;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


public class XMLWriter {
	private ArrayList<BookFromSite> AllBook;
	private DocumentBuilder builder;
	
	public XMLWriter(ArrayList<BookFromSite> AllBook) {
		this.AllBook = AllBook;
	}
	
	public void write(String FileName) throws TransformerException, IOException, ParserConfigurationException {
		if (AllBook.size()==0) return;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element ElBibleCalendar = doc.createElement("BibleCalendar");
		doc.appendChild(ElBibleCalendar);
		ElBibleCalendar.setAttribute("conf", "catholic");
		
		GregorianCalendar PrevDate = AllBook.get(0).date;
		int PrevYear = PrevDate.get(Calendar.YEAR);
		
		Element ElYear = doc.createElement("Year");
		ElYear.setAttribute("year", String.valueOf(PrevYear));
		ElBibleCalendar.appendChild(ElYear);

		Element ElDate = doc.createElement("Date");
		ElDate.setAttribute("date", String.format("%tF", PrevDate));
		ElYear.appendChild(ElDate);
		
		for (BookFromSite b : AllBook) {
			
			//System.out.println(b);
			
			GregorianCalendar CurDate = b.date;
			int CurYear = CurDate.get(Calendar.YEAR);
			
			if (CurYear!=PrevYear) {
				PrevYear = CurYear;
				ElYear = doc.createElement("Year");
				ElYear.setAttribute("year", String.valueOf(CurYear));
				ElBibleCalendar.appendChild(ElYear);
			}
						
			//System.out.println(String.format("%tF", PrevDate) + " = " + String.format("%tF", CurDate));
			if (! String.format("%tF", PrevDate).equals(String.format("%tF", CurDate))) {
				PrevDate = CurDate;
				ElDate = doc.createElement("Date");
				ElDate.setAttribute("date", String.format("%tF", CurDate));
				ElYear.appendChild(ElDate);
			}
			
			if (b.alt == 0) {
				Element ElBook = doc.createElement("Book");
				ElBook.setAttribute("text", b.fullText);
				ElBook.setAttribute("type", b.type);
				ElBook.setAttribute("book", b.book);
				if (b.alt>0) ElBook.setAttribute("alt", String.valueOf(b.alt));
				//ElBook.setAttribute("isOrdinary", b.isOrdinary.toString());
				ElDate.appendChild(ElBook);


				for (ReadStartEnd otrivok : b.otr) {
					if (otrivok!=null) {
						Element ElFragment = doc.createElement("Fragment");
						String chStartS = String.valueOf(otrivok.chapterStart);
						if (otrivok.chapterStart == 1003) {
							chStartS = "C";
						};
						String chEndS = String.valueOf(otrivok.chapterEnd);
						if (otrivok.chapterEnd == 1003) {
							chEndS = "C";
						};

						ElFragment.setAttribute("chapterStart", chStartS);
						ElFragment.setAttribute("stihStart", String.valueOf(otrivok.stihStart));
						ElFragment.setAttribute("chapterEnd", chEndS);
						ElFragment.setAttribute("stihEnd", String.valueOf(otrivok.stihEnd));
						if (b.book.equalsIgnoreCase("PS") || b.book.equalsIgnoreCase("ПС")) {
							ElFragment.setAttribute("chapterStartMasoretic", String.valueOf(otrivok.chapterStartMasoretic));
							ElFragment.setAttribute("stihStartMasoretic", String.valueOf(otrivok.stihStartMasoretic));
							ElFragment.setAttribute("chapterEndMasoretic", String.valueOf(otrivok.chapterEndMasoretic));
							ElFragment.setAttribute("stihEndMasoretic", String.valueOf(otrivok.stihEndMasoretic));
						}
						ElBook.appendChild(ElFragment);
					}
				}
			}
			
		}
		
		//Записываем файл на диск
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(FileName)));
		
	}
	
	
}
