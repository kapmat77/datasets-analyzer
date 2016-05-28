package mk.datasets.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Kapmat on 2016-05-28.
 */

public class FileOperator {

	private List<Record> input = new ArrayList<>();

	public List<Record> readDataFromFile(String file) {
		Path path = Paths.get(file);
		try {
			Scanner in = new Scanner(path);
			String line;
			String[] parts;
			while (in.hasNextLine()) {
				Record Record = new Record();
				line = in.nextLine();
				parts = line.split(" ");
				for (String word : parts) {
					word = word.toUpperCase();
					if (word.endsWith(".")) {
						word = word.replace(".", "");
//						Record.addWord(word);
					} else {
//						Record.addWord(word);
					}
				}
				input.add(Record);
			}
		} catch (IOException e) {
			System.out.println("Plik nie zostal wczytany poprawnie!");
			e.printStackTrace();
			System.exit(-1);
		}
		return input;
	}

}