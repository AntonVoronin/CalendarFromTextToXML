package ru.itprospect;


public class CalendarFromTextToXML {
	
	private static final String FileNameCatholic = "�������� ����.txt";

	public static void main(String[] args) {
		ParsTxt parser = new ParsTxt(FileNameCatholic);
		parser.ParsIt();
		
		System.out.println("done");
	}

}
