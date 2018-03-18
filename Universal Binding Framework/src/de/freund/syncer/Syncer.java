package de.freund.syncer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Syncer<E> implements BOCapable<E> {
	private List<SyncTransaction<E, ?, ?>> syncTransactions = new Vector<>();

	public void setObject(E object) {
		this.setObject(() -> object);
	}

	public void setObject(Supplier<E> objectSupplier) {
		this.syncTransactions.stream().forEach((t) -> t.setObject(objectSupplier));
		this.loadUiFromBO();
	}

	public boolean loadUiFromBO() {
		syncTransactions.forEach((st) -> st.loadUiFromBO());
		return true;
	}

	public boolean writeUiToBO() {
		boolean success = false;
		for (SyncTransaction<E, ?, ?> syncTransaction : syncTransactions) {
			success |= syncTransaction.writeUiToBO();
		}
		if (success == false) {
			restoreBOValue();
		}
		return true;
	}

	public void addSyncTransaction(SyncTransaction<E, ?, ?> syncTransaction) {
		this.syncTransactions.add(syncTransaction);
	}

	@Override
	public void restoreBOValue() {
		syncTransactions.forEach((st) -> st.restoreBOValue());
	}
}
