package ru.itprospect;


public class CalendarFromTextToXML {
	
	private static final String FileNameCatholic = "католики июнь.txt";

	public static void main(String[] args) {
		ParsTxt parser = new ParsTxt(FileNameCatholic);
		parser.ParsIt();
		
		System.out.println("done");
	}

}
