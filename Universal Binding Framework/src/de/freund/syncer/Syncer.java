package de.freund.syncer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Syncer<E> {
	private List<BOCapable> syncTransactions = new Vector<>();
	private Supplier<E> objectSupplier;
	private Consumer<Syncer<E>> buildStrategy;
	private List<Consumer<Syncer<E>>> listeners = new ArrayList<Consumer<Syncer<E>>>();
	
	public E getObject() {
		return objectSupplier.get();
	}
	
	public void setObject(E object) {
		this.setObject(() -> object);
	}
	
	public void setObject(Supplier<E> objectSupplier) {
		this.objectSupplier = objectSupplier;
		syncTransactions.clear();
		this.buildStrategy.accept(this);
		this.loadUiFromBO();
	}
	
	public Syncer(Consumer<Syncer<E>> buildStrategy) {
		this.buildStrategy = buildStrategy;
	}

	public void loadUiFromBO() {
		syncTransactions.forEach((st) -> st.loadUiFromBO());
	}

	public void writeUiToBO() {
		boolean success = false;
		for (BOCapable syncTransaction : syncTransactions) {
			success |= syncTransaction.writeUiToBO();
		}
		if (success == false) {
			syncTransactions.forEach((st) -> st.restoreBOValue());
		}
		this.listeners.forEach((listener) -> listener.accept(this));
	}

	public void addSyncTransaction(BOCapable syncTransaction) {
		this.syncTransactions.add(syncTransaction);
	}

	
	public void addListener(Consumer<Syncer<E>> listener) {
		this.listeners.add(listener);
	}
}
