package de.freund.syncer;

import java.util.function.Supplier;

public interface BOCapable<E> {
	public void setObject(Supplier<E> object);
	public boolean loadUiFromBO();
	public boolean writeUiToBO();
	void restoreBOValue();
}
