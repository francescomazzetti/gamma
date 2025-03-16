package it.gamma.service.signer.account;

public class UserAccount {

	private String taxcode;
	private String signInstrument;

	public UserAccount(String taxcode, String signInstrument) {
		this.setTaxcode(taxcode);
		this.setSignInstrument(signInstrument);
	}

	public String getSignInstrument() {
		return signInstrument;
	}

	public void setSignInstrument(String signInstrument) {
		this.signInstrument = signInstrument;
	}

	public String getTaxcode() {
		return taxcode;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

}
