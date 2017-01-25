package ru.itprospect;


public class CalendarFromTextToXML {
	
	private static final String FileNameCatholic = "read_cal_ru_cat_2017.xml";

	public static void main(String[] args) {
		ParsTxt parser = new ParsTxt(FileNameCatholic);
		parser.ParsIt();
		
		System.out.println("done");
	}

}
