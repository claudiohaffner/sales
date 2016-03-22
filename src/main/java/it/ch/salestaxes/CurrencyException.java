package it.ch.salestaxes;

public class CurrencyException extends Exception {
	
	private static final long serialVersionUID = -5956859788887992184L;
	private String errorMessage;

	public CurrencyException(String arg0) {
		super(arg0);
		errorMessage = "CurrencyException: '"+arg0+"'";
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return errorMessage;
	}
	
}
