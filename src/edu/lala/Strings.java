package edu.lala;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Strings {
	
	public static void StringCompare(String texto, int cursor, File arquivo) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(arquivo));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		try {
			r.skip(cursor);
			int caractere = '\0';
			StringBuilder sb = new StringBuilder();
			final int max_length = 1;
			int i = 0;
			while((caractere = r.read()) != '\0') {
				if(i > max_length) {
					break;
				}
				sb.append(caractere);
				if(/*validFormat(sb)*/true) {
					break;
				}
				i++;
			}
		} catch(IOException e) {
			
		}
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
}
