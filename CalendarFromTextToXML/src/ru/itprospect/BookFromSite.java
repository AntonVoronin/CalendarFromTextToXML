package ru.itprospect;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;



public class BookFromSite {
	GregorianCalendar date;
	String dateStr;
	int num;
	String fullText;
	String type;
	String link;
	String book;
	Boolean isOrdinary=false;
	int alt;
	ReadStartEnd [] otr = new ReadStartEnd[20];
	
	public void DetectChapter() {
		//TODO «десь пишем замену не читаемых символов
		fullText = fullText.replaceAll("\ufffd", "-").replace("--", "-");
		
		String TextBezKnigi = poluchitTextBezKnigi(fullText);
		//System.out.println(TextBezKnigi);
		
		ArrayList<String> TextPoSlovam = TextPoSlovam(TextBezKnigi);
		
		//for (String word : TextPoSlovam) {
			//System.out.println(word);
		//};
		
		ReadStartEnd otrivok = new ReadStartEnd();
		Boolean OtrivokIzDvuhChastey = false;
		int TekGlava = 0;
		int IndexOtr = 0;
				
		for (int i=1; i<TextPoSlovam.size(); i++) {
			String word = TextPoSlovam.get(i);
			
			if (word.equals(":")) { //ѕрошлое слово - глава
				String textGlava = TextPoSlovam.get(i-1);
				if (textGlava.matches("c|C")) {
					TekGlava =  1003; //—пециально дл€ книги Esth C делаем номер главы -100
				}
				else {
					TekGlava =  Integer.parseInt(TextPoSlovam.get(i-1));
				}
				
			};
			
			if (word.equals(",") || word.equals(";")) { //Ёто конец отрывка
				if (!OtrivokIzDvuhChastey) { //придетс€ заполнить и начало отрывка
					otrivok.chapterStart = TekGlava;
					otrivok.stihStart = Integer.parseInt(TextPoSlovam.get(i-1));
				};
				otrivok.chapterEnd = TekGlava;
				otrivok.stihEnd = Integer.parseInt(TextPoSlovam.get(i-1));
				otr[IndexOtr] = otrivok;
				
				otrivok = new ReadStartEnd();
				OtrivokIzDvuhChastey = false;
				IndexOtr++;
			};
			
			if (word.equals("-")) { //ќтрывок состоит из двух частей, и прошлое слово - начало отрывка
				OtrivokIzDvuhChastey = true;
				
				//«аполн€ем начало отрывка
				otrivok.chapterStart = TekGlava;
				otrivok.stihStart = Integer.parseInt(TextPoSlovam.get(i-1).replaceAll("\\D", ""));
			};
			
		};
			
	}
	
	private String poluchitTextBezKnigi(String str) {
		//1 Pet 4:14
		int indexColon = str.indexOf(":");
		int indexBook = str.lastIndexOf(" ", indexColon);
		
		String TextBezKnigi = str.substring(indexBook+1);
		//TODO ѕроверка странных символов
//		for (int i = 0; i < TextBezKnigi.length(); i++) {
//			char ch = TextBezKnigi.charAt(i);
//			int chCode = (int)ch;
//			System.out.println(ch + " - " + String.valueOf(chCode));
//		}
		TextBezKnigi = TextBezKnigi.replace("c:", "1003:").replaceAll("\ufffd", "-").replaceAll(" and ", ", ").replaceAll("a|b|c|d|e|f|g|h", "").replace("--", "-");

		
		
		TextBezKnigi = TextBezKnigi.trim();
		
		return TextBezKnigi;
	}

	public void detectTextKnigi(String textPredBook) {
		int indexColon = fullText.indexOf(":");
		if (indexColon == -1) {
			indexColon = fullText.indexOf("-");
		}
		int indexBook = fullText.lastIndexOf(" ", indexColon);
		//System.out.println("\'" + indexBook + "\'");
		if (indexBook==-1) {
			book = textPredBook;
		}
		else {
			String TextKnigi = fullText.substring(0, indexBook);
			book = TextKnigi.trim();
		}
		//System.out.println("“екст книги \'" + book + "\'");
		
	}
	
	private ArrayList<String> TextPoSlovam(String str) {
		ArrayList<String> al = new ArrayList<String>();
		int EndNum = 0;
		int StartNum = 0;
		
		//str = str.replaceAll("[.]", "");
		
		for (int i=0; i<=str.length()-1; i++) {
			String CurCh = str.substring(i, i+1);
			if (CurCh.equals(",") || CurCh.equals("-") || CurCh.equals(":")) {
				
				EndNum = i;
				String StrWord = str.substring(StartNum, EndNum);
				StrWord = StrWord.trim();
				
				al.add(StrWord);
				al.add(CurCh);
				
				//System.out.println(StrWord);
				
				StartNum = i+1;
			};
			
			if (i==(str.length()-1)) {
				
				EndNum = i+1;
				String StrWord = str.substring(StartNum, EndNum);
				StrWord = StrWord.trim();
				
				al.add(StrWord);
				
				//System.out.println(StrWord);
			};
		};
		
		al.add(",");
		
		return al;
		
	}

	public void convertPsalms() {
		if (book.equalsIgnoreCase("PS")) {
			for (ReadStartEnd otrivok : otr) {
				if (otrivok!=null) {
					int[] start = ConvertNumberPsalms.convertToSeptuagint(otrivok.chapterStart, otrivok.stihStart);
					int[] end = ConvertNumberPsalms.convertToSeptuagint(otrivok.chapterEnd, otrivok.stihEnd);
					
					otrivok.chapterStartMasoretic = otrivok.chapterStart;
					otrivok.stihStartMasoretic = otrivok.stihStart;
					otrivok.chapterEndMasoretic = otrivok.chapterEnd;
					otrivok.stihEndMasoretic = otrivok.stihEnd;
					
					otrivok.chapterStart = start[0];
					otrivok.stihStart = start[1];
					otrivok.chapterEnd = end[0];
					otrivok.stihEnd = end[1];
					}
				}
			
		}
	}
	
	public void sortFragments() {
		//ќпредел€ем количество не пустых элементов, сортируем
		int i = 0;
		while(otr[i] != null) {
			i++;
		}
		Arrays.sort(otr, 0, i);

		//”дал€ем отрывки, которые повтор€ют уже имеющийс€ стих
		i = 1;
		while(otr[i] != null) {
			if (otr[i].chapterStart == otr[i-1].chapterEnd && otr[i].stihStart < otr[i-1].stihEnd) {
				if (otr[i].stihEnd <= otr[i-1].stihEnd) {
					//удал€ем (сдвигаем все элементы влево)
					for (int d=i; d<otr.length-1; d++) {
						otr[d] = otr[d+1];
					}
					i--;
				}
				else {
					otr[i].stihStart = otr[i-1].stihEnd;
				}
			}
			i++;
		}
	}
	
	@Override
	public String toString() {
		String StrOtr = "# ";
		for (ReadStartEnd otrivok : otr) {
			if (otrivok!=null) {
				StrOtr = StrOtr + otrivok.toString() + " # ";
			};
		};
		
		return "BookFromSite [fullText=" + fullText + ", type=" + type + ", book="
				+ book  + ", date="	+ dateStr + ", otr=" + StrOtr + "]";
	}
	
	
	
}	
	


class ReadStartEnd implements Comparable {
	int chapterStart;
	int stihStart;
	int chapterEnd;
	int stihEnd;
	int chapterStartMasoretic;
	int stihStartMasoretic;
	int chapterEndMasoretic;
	int stihEndMasoretic;
	
	@Override
	public String toString() {
		return "ReadStartEnd [chapterStart=" + chapterStart + ", StihStart="
				+ stihStart + ", chapterEnd=" + chapterEnd + ", StihEnd="
				+ stihEnd + "]";
	}

	@Override
	public int compareTo(Object obj) {
		ReadStartEnd tmp = (ReadStartEnd)obj;
		if (this.chapterStart < tmp.chapterStart) {
			/* текущее меньше полученного */
			return -1;
		}   
		else if (this.chapterStart > tmp.chapterStart) {
			/* текущее больше полученного */
			return 1;
		}
		else if (this.chapterStart == tmp.chapterStart) {
			if (this.stihStart < tmp.stihStart) {
				/* текущее больше полученного */
				return -1;
			}
			else if (this.stihStart > tmp.stihStart) {
				/* текущее больше полученного */
				return 1;
			}
		}
		/* текущее равно полученному */
		return 0;  

	}
	
}