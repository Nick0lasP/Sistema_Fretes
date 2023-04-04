package exceptions;

@SuppressWarnings("serial")
public class CityNotFoundException extends Exception {
	
	/*	 
      The initial idea of ​​this project was to create exceptions to handle errors, during development I saw that it would be better to make them visible on screen. 
      And I ended up leaving just the one that says that a city is not in the csv list, as I myself inserted the options in the choiceBox, it never happens.
	 */
	
	private String invalidCity;

    public CityNotFoundException(String invalidCity) {
        super("The city " + invalidCity + " is invalid because it's not in the csv list.");
        this.invalidCity = invalidCity;
    }

    public String getCidadeInvalida() {
    	return invalidCity;
    }
        

}
