package ru.itprospect;

public class ConvertNumberPsalms {
	
	public static int[] convertToSeptuagint(int chapterV, int stihV) {
		int chapterS = chapterV;
		int stihS = stihV;
		
		
		if (chapterV<=9) {
			//Совпадают
		}
		else if (chapterV==10) {
			chapterS = 9;
			stihS = stihV + 21;
		}
		else if (chapterV>=11 && chapterV<=114) {
			chapterS = chapterV - 1;
		}
		else if (chapterV==115) {
			chapterS = 113;
			//в пс 114 - 8 стихов, в 115 - 18 стихов
			stihS = stihV + 8;
		}
		else if (chapterV==116) {
			if (stihV<=9) {
				chapterS = 114;
			}
			else {
				chapterS = 115;
				stihS = stihV - 9;
			}
		}
		else if (chapterV>=117 && chapterV<=146) {
			chapterS = chapterV - 1;
		}		
		else if (chapterV==147) {
			if (stihV<=11) {
				chapterS = 146;
			}
			else {
				chapterS = 147;
				stihS = stihV - 11;
			}
		}
		else if (chapterV>=148) {
			//Совпадают
		}		
		
		int[] chapterStihS = new int[2];
		chapterStihS[0] = chapterS;
		chapterStihS[1] = stihS;
		return chapterStihS;
	}
	
}
