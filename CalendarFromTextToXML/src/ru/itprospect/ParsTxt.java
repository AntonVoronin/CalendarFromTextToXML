package ru.itprospect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParsTxt {
	
	private String FileNameCatholic;
	private String FileNameCatholicXML;
	private ArrayList<BookFromSite> AllBook;
	
	private int bookQty;
	private HashMap<String, BookBQ> bookMap;
	private ArrayList<BookBQ> arrayListBook;
	
	private final static String DIR_BQ_RST = "RST/";
	private final static String INI_BQ_RST = "bibleqt.ini";

	public ParsTxt(String FileNameCatholic) {
		this.FileNameCatholic = FileNameCatholic;
		FileNameCatholicXML = FileNameCatholic.substring(0, FileNameCatholic.length()-4) + ".xml";
		InitializationBQ();
	}
	
	public void ParsIt() {
		AllBook = new ArrayList<BookFromSite>();
		Date currDate=null;
		String currDateType = "";
		int a = 0;
		
		//Чтение текстового файла
		try{
			FileInputStream fstream = new FileInputStream(FileNameCatholic);
			//TODO здесь нужно указать кодировку. По умолчанию win1251   
			//"utf8"
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream ));
			String strLine;
			while ((strLine = br.readLine()) != null){
				a++;
				//System.out.println(strLine);
				//TODO Анализ текстового файла
				if (strLine.startsWith("Дата: ")) {
					String strDate = strLine.substring(6, 16);
					//System.out.println(strDate);
					
					SimpleDateFormat format = new SimpleDateFormat();
					format.applyPattern("yyyy-MM-dd");
					currDate = format.parse(strDate);
					
					currDateType = "";
					int indexType = strLine.lastIndexOf("   ");
					if (indexType>0) {
						currDateType = strLine.substring(indexType+3).trim();
					}
					else if (strLine.length()>16) {
						String analizType = strLine.substring(17).trim();
						//System.out.println(analizType);
						if (analizType.length()>0) {
							currDateType = analizType;
						}
					}
				}
				else if (a<2) {
					continue;
				}
				else if(strLine.length()>0){
					String textType = "";
					String textRead = strLine;
					int indexType = strLine.indexOf("   ");
					
					if (indexType==-1){
						indexType = strLine.lastIndexOf("[это рядовое чтение]");
					}
					
					if (indexType>0) {
						textType = strLine.substring(indexType).trim();
						textRead = strLine.substring(0, indexType).trim();
					} 
					
					//Добавляем книгу
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(currDate);
					ArrayList<BookFromSite> thisBook = ReadingsToBooks.StringToBooks(textRead, cal, textType, 0, currDateType);
					AllBook.addAll(thisBook);
					for (BookFromSite b : thisBook) {
						CheckBook(b);
					}
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
	
	private void CheckBook(BookFromSite b) {
		String strDate = b.dateStr;
		//TODO Ищем здесь книгу по краткому имени
		//Убираем из краткого названия книги пробелы
		String bookNameBezProbelov = b.book.replaceAll(" ", "").toUpperCase(Locale.getDefault());
		BookBQ bookBQ = bookMap.get(bookNameBezProbelov);
		if (bookBQ != null) {
			//System.out.println("название книги из xml : " + shotNameBook + " - полное имя : " + bookBQ.fullName);

			//TODO Нужно проверить количество глав в книге 
			//и то, что в следующем отрывке номер главы и стиха не меньше, чем в предыдущем отрывке
			int predChapter = 0;
			int predStih = 0;
			for (ReadStartEnd otrivok : b.otr) {
				if (otrivok!=null) {
						if (otrivok.chapterEnd>bookBQ.chapterQty) {
							System.out.println(strDate + "!!!" + b.book + " Глава больше, чем есть в книге : " + otrivok.chapterEnd);
						}

						if (otrivok.chapterStart<predChapter) {
							System.out.println(strDate + "!!!" + b.book + " в предыдущем отрывке глава меньше : " + otrivok.chapterStart);
						}

						if (otrivok.chapterStart==predChapter && otrivok.stihStart<predStih) {
							System.out.println(strDate + "!!!" + b.book + " в предыдущем отрывке стих меньше : " + otrivok.chapterStart);
						}
						predChapter = otrivok.chapterEnd;
						predStih = otrivok.stihEnd;
				}				

			}
		}
		else {
			System.out.println(strDate + "Не найдена книга : " + b.book);
		}
	}
	
	private void InitializationBQ() {


		try {
			//InputStreamReader isr = new InputStreamReader(is, "windows-1251");



			FileInputStream is = new FileInputStream ( DIR_BQ_RST + INI_BQ_RST );
			InputStreamReader isr = new InputStreamReader ( is, "windows-1251");
			BufferedReader in = new BufferedReader(isr);

			String line;
			Boolean isDone = false;
			while (!isDone && (line=in.readLine()) != null) {
				if (!(line.length()==0 || line.startsWith("//"))) {

					if (line.startsWith("ChapterSign =")) {
//						chapterSign = line.substring(13).trim();
					}
					else if (line.startsWith("VerseSign =")) {
//						verseSign = line.substring(11).trim();
					}
					else if (line.startsWith("BookQty =")) {
						bookQty = Integer.parseInt(line.substring(9).trim());
						isDone = true;
					}
				}
			}

			if (!isDone) {
				is.close();
				return; //Ошибка, дальше не идем
			}

			bookMap = new HashMap<String, BookBQ>();
			arrayListBook = new ArrayList<BookBQ>();
			//arrayListKey = new ArrayList<String>();

			//Начинаем цикл по книгам
			for (int a = 0; a<bookQty; a++) {
				BookBQ bookBQ = new BookBQ();
				arrayListBook.add(bookBQ);

				for (int b = 0; b<5; b++) {
					line=in.readLine();

					if (line.startsWith("PathName =")) {
						bookBQ.pathName = line.substring(10).trim();
					}
					else if (line.startsWith("FullName =")) {
						bookBQ.fullName = line.substring(10).trim();
						//System.out.println(bookBQ.fullName);
					}
					else if (line.startsWith("ChapterQty =")) {
						bookBQ.chapterQty = Integer.parseInt(line.substring(12).trim());
					}
					else if (line.startsWith("ShortName =")) {
						//здесь нужно выделить все варианты краткого названия книги и добавить книгу в Map столько раз, сколько сокращений найдено
						String strShortName = line.substring(11).trim();
						StringTokenizer t = new StringTokenizer(strShortName);
						boolean first = true;
						while (t.hasMoreTokens()) {
							String token = t.nextToken().toUpperCase(Locale.getDefault());
							bookMap.put(token, bookBQ);

							if (first) {
								bookBQ.key = token; //добавляем первое сокращение названия
								first = false;
							}
						}

					}
				}
			}


			is.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}


	
	class BookBQ {
		public String pathName;
		public String fullName;
		public String key;
		public int chapterQty;

		@Override
		public String toString() {
			return "BookBQ [pathName=" + pathName + ", fullName=" + fullName
					+ ", chapterQty=" + chapterQty + "]";
		}
	
	}
}
