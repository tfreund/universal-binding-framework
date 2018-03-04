package de.freund.syncer;

public interface BOCapable {
	public boolean loadUiFromBO();
	public boolean writeUiToBO();
	void restoreBOValue();
}
