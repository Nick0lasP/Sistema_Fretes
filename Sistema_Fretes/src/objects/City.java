package objects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

import exceptions.CityNotFoundException;

public class City {
	
	// This class represents a city, containing a list of cities and the distance between them.
	private String[] cities;
	private int[][] distances;
	
	// Constructor that initializes the cities and distances arrays by reading from a given archive.
	public City(String archive) {
		initialazible(archive);
	}

	// this method reads the cities and their distances from the csv archive and initializes the cities and distances arrays.
	public void initialazible(String archive) {
		try (BufferedReader reader = new BufferedReader(new FileReader(archive))){
			String line = reader.readLine();
			this.cities = line.split(";");
			this.distances = new int[this.cities.length][this.cities.length];
			int i = 0;

			while ((line = reader.readLine()) != null) {
				String[] values = line.split(";");
				for (int j = 0; j < values.length; j++) {
					distances[i][j] = Integer.parseInt(values[j]);
				}
				i++;
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("File \""+archive+"\" not found."); 
		} catch (IOException e) {
			System.out.println("Unable to read the file " + archive+".");
		} 

	}

	// Returns an array containing all the cities stored
	public String[] getCities() {
		return cities;
	}

	/*
	 Given two cities, returns the distance between them as an integer based on the origin city name and destination city name
	 If either the origin or destination city is not found in the object's list of cities throws an exception
	 */
	public int getDistance(String origin, String destination) throws CityNotFoundException {
		int i = Arrays.asList(cities).indexOf(removeAccents(origin).toUpperCase());
		int j = Arrays.asList(cities).indexOf(removeAccents(destination).toUpperCase());
		if (i == -1) {
			throw new CityNotFoundException(origin);
		}
		if (j == -1) {
			throw new CityNotFoundException(destination);
		}
		return distances[i][j];
	}

	//This method normalize a string, removing the accents
	public String removeAccents(String value) {
		String normalizer = Normalizer.normalize(value, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(normalizer).replaceAll("");
	}
}
