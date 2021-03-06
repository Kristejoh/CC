package timetable;

import dataTypes.AClass;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseIcal {
	//
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	String[] subjectCode = new String[1000];
	String[] className = new String[1000];
	String[] attendingGroups = new String[1000];
	String[] classTypes = new String[1000];
	String[] academicians = new String[1000];
	boolean userAttends = true;
	String[] startTimestamp = new String[1000];
	String[] endTimestamp = new String[1000];
	String[] startingTime = new String[1000];
	String[] endingTime = new String[1000];
	String[] classRooms = new String[1000];

	boolean Startread = false;
	boolean Endread = false;
	int counter = 0;

	public void ParseIc(String Surl) throws IOException {
		ParseIcal parser = new ParseIcal(Surl);
		parser.processLineByLine();
		toAclass();
	}

	/**
	 * Constructor.
	 * 
	 * @param aFileName
	 * full name of an existing, readable file.
	 */
	public ParseIcal(String aFileName) {
		fFilePath = Paths.get(aFileName);
	}

	/**
	 * Template method that calls .
	 */
	@SuppressWarnings("unused")
	public final void processLineByLine() throws IOException {
		String foundStart = "BEGIN:VEVENT";
		String foundEnd = "END:VEVENT";
		String descR = "DESCRIPTION:";
		String timeStart = "DTSTART:";
		String timeEnd = "DTEND:";
		String classRoom = "LOCATION:";
		String name = "";

		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			while (scanner.hasNext()) {
				if (name.contains(foundStart)) {
					counter++;
					Startread = true;
				}
				if (name.contains(foundEnd)) {
					Endread = true;
				}
				if (Startread & Endread) {
					Startread = false;
					Endread = false;
				}
				if (Startread & !Endread) {
					// Timestart
					if (name.contains(timeStart)) {
						name = name.replace(timeStart, "");
						startTimestamp[counter - 1] = name;
					}
					// Timeend
					if (name.contains(timeEnd)) {
						name = name.replace(timeEnd, "");
						endTimestamp[counter - 1] = name;
					}
					// classroom
					if (name.contains(classRoom)) {
						name = name.replace(classRoom, "");
						name = name.replace(",", "/");
						classRooms[counter - 1] = name;
					}
					if (name.contains(descR)) {
						processLine(name);
					}
				}
				name = scanner.nextLine();
			}
		}
		CalendarWindow calen = new CalendarWindow();
		calen.calenda();
		toAclass();
		Writer writer = null;
		String fileLoc = "output.txt";
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(fileLoc));
			String classiks = String.valueOf(toAclass());
			
				out.write(classiks);
			
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * Overridable method for processing lines in different ways.
	 */

	@SuppressWarnings({ "unused", "resource" })
	protected void processLine(String aLine) {
		String descR = "DESCRIPTION:";
		String temp = aLine;
		temp = temp.replace(descR, "");
		temp = temp.replace("\\n\\n", "_");
		temp = temp.replace("\\n", "_");
		String[] parts = temp.split("_");
		String part1 = parts[0];
		String part2 = parts[1];
		String part3 = parts[2];
		String part4 = parts[3];
		String part5 = parts[4];
		String part6 = parts[5];
		String part7 = parts[6];
		String part8 = parts[7];
		String part9 = parts[8];
		// Ainekood
		part1 = part1.replace("Ainekood: ", "");
		subjectCode[counter - 1] = part1;
		// Rühmad
		part2 = part2.replace("R�hmad: ", "");
		part2 = part2.replace("\\", "");
		part2 = part2.replace(",", "/");
		attendingGroups[counter - 1] = part2;
		// Õppejõud
		part5 = part5.replace("�ppej�ud: ", "");
		part5 = part5.replace("kasutaja:", "");
		part5 = part5.replace(",", "/");
		if (part5.equals("")) {
			part5 = "empty";
		}
		academicians[counter - 1] = part5;

		// AineTüüp
		part6 = part6.replace("T��p: ", "");
		part6 = part6.replace(",", "/");
		classTypes[counter - 1] = part6;

		// Aine Algusaeg ja Lõppaeg
		part3 = part3.replace("Aeg: ", "");
		part3 = part3.replace(",", "/");
		Scanner useDel = new Scanner(part3).useDelimiter("\\s* - \\s*");
		startingTime[counter - 1] = useDel.next();
		endingTime[counter - 1] = useDel.next();

		// Ainenimi
		part9 = part9.replace("Voor: ", "");
		part9 = part9.replaceAll("(\\d+)", "");
		part9 = part9.replaceAll("[A-Z] ", "");
		part9 = part9.replaceAll(" [A-Z]", "");
		part9 = part9.replace(",", "/");
		className[counter - 1] = part9;
		// log(endingTime[counter]);

	}

	@SuppressWarnings({ "rawtypes" })
	public ArrayList toAclass() throws IOException {
		ArrayList<AClass> classes = new ArrayList<>();
		for (int i = 0; i < counter - 1; i++) {
			classes.add(new AClass(subjectCode[i], className[i] ,
					attendingGroups[i], classTypes[i], startTimestamp[i],
					endTimestamp[i], classRooms[i], academicians[i]));

		}
		// System.out.println(classes);

		
		return classes;

	}
}
