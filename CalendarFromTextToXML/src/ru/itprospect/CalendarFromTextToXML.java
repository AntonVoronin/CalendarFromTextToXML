package ru.itprospect;


public class CalendarFromTextToXML {
	
	private static final String FileNameCatholic = "read_cal_ru_orth_2020 февраль март.txt";

	public static void main(String[] args) {
		ParsTxt parser = new ParsTxt(FileNameCatholic);
		parser.ParsIt();
		
		System.out.println("done");
	}
}
