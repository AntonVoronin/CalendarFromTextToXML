package ru.itprospect;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ReadingsToBooks {

	public static ArrayList<BookFromSite> StringToBooks(String readingText, GregorianCalendar startDate) {
		return StringToBooks(readingText, startDate, "", 0, "");
	}
	
	public static ArrayList<BookFromSite> StringToBooks(String readingText, GregorianCalendar startDate, String type, int alt, String dateType) {
		
		readingText = readingText.replaceAll("Phlm ", "Phlm 1:").replaceAll("phlm ", "Phlm 1:").replaceAll("2 John ", "2 John 1:").replaceAll("3 John ", "3 John 1:")
				.replaceAll("2 jn ", "2 jn 1:").replaceAll("3 jn ", "3 jn 1:").replaceAll("1 Sm 3:9; Jn 6:68c", "1 Sm 3:9")
				.replaceAll(";", ",").replaceAll("cf Jn", "Jn").replaceAll("\\(cited in Lk 4:18\\)", "").replaceAll(", 32C4:4", "").replaceAll("2 Cor1", "2 Cor 1")
				.replaceAll("See ", "").replaceAll("SEE ", "").replaceAll("see ", "")
				.replaceAll("4befghn", "4").replaceAll("Jn 1:7, Lk 1:17", "Jn 1:7").replaceAll("Jn 1:14a, 12a", "Jn 1:12, 14").replaceAll("cf Mk", "Mk")
				.replaceAll("cf Lk", "Lk").replaceAll("cf 2 Thes", "2 Thes").replaceAll("cf 1 Cor", "1 Cor").replaceAll("Cf Luke", "Luke").replaceAll("Cf Eph", "Eph")
				.replaceAll("Cf 2 Tm", "2 Tm").replaceAll("Jude ", "Jude 1:")
				.replaceAll("Cf ", "").replaceAll("cf ", "").replaceAll("cF ", "")
				.replaceAll("Heb5", "Heb 5")
				.replaceAll("Heb13", "Heb 13").replaceAll("Gn1", "Gn 1").replaceAll("Jgs2", "Jgs 2")
				.replaceAll("Col1", "Col 1")
				.replaceAll("Sgs", "Songs");
		
		if (readingText.equals("16:13-19") && startDate.equals(new GregorianCalendar(2016, 1, 22))) {
			readingText = "Mt " + readingText;
		}
		if (readingText.equals("1:46-56") && startDate.equals(new GregorianCalendar(2017, 11, 22))) {
			readingText = "Lk " + readingText;
		}

		
		ArrayList<BookFromSite> books = new ArrayList<BookFromSite>();
		String DateStr = String.format("%tF", startDate);

			String textBook = readingText.trim();
			//System.out.println("\'" + textBook + "\'");
			String textPredBook = "";
			//обрабатывать строки типа Song 2:8-14 or Zeph 3:14-18   (2013-12-21) (2014-11-02 - очень много or)
			String[] varText = textBook.split(" or ");
			for (int x=0; x<varText.length; x++) {
		        //System.out.println(varText[x]);
				BookFromSite book = new BookFromSite();
				book.date = (GregorianCalendar) startDate.clone();
				book.dateStr = DateStr;
				book.fullText = varText[x];
				book.detectTextKnigi(textPredBook);
				if (type.contains("[это рядовое чтение]")) {
					book.isOrdinary = true;
					type = type.replace("[это рядовое чтение]", "").trim();
				}
				book.type = type;
				book.dateType = dateType;
				book.alt = alt;
				
				book.DetectChapter();
				book.convertPsalms();
				//отрывки по-порядку
				book.sortFragments();
				System.out.println(book);
				books.add(book);

				textPredBook = book.book; 
			}


		
		
		return books;

	}
	

}
