package de.freund.syncer;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UISyncTransaction<BOValue, UIValue> implements BOCapable {
	private Supplier<BOValue> boGetter;
	private Consumer<BOValue> boSetter;
	private Supplier<UIValue> uiGetter;
	private Consumer<UIValue> uiSetter;
	private Function<BOValue, UIValue> abTransformer;
	private Function<UIValue, BOValue> baTransformer;
	private BOValue oldBoValue;
	private Consumer<Exception> errorHandler;

	public UISyncTransaction(Supplier<BOValue> boGetter, Consumer<BOValue> boSetter, Supplier<UIValue> uiGetter, Consumer<UIValue> uiSetter,
			Function<BOValue, UIValue> boUiTransformer, Function<UIValue, BOValue> uiBoTransformer, Consumer<Exception> errorHandler) {
		this.errorHandler = errorHandler;
		this.abTransformer = boUiTransformer;
		this.baTransformer = uiBoTransformer;
		connectBO(boGetter, boSetter);
		connectUI(uiGetter, uiSetter);
	}

	private void connectUI(Supplier<UIValue> bGetter, Consumer<UIValue> bSetter) {
		this.uiGetter = Objects.requireNonNull(bGetter);
		this.uiSetter = Objects.requireNonNull(bSetter);
	}

	private void connectBO(Supplier<BOValue> boGetter, Consumer<BOValue> boSetter) {
		this.boGetter = Objects.requireNonNull(boGetter);
		this.boSetter = Objects.requireNonNull(boSetter);
	}

	@Override
	public boolean loadUiFromBO() {
		BOValue boValue = boGetter.get();
		UIValue newBValue = abTransformer.apply(boValue);
		uiSetter.accept(newBValue);
		return true;
	}

	@Override
	public boolean writeUiToBO() {
		try {
			oldBoValue = boGetter.get();
			UIValue uiValue = uiGetter.get();
			BOValue newAValue = baTransformer.apply(uiValue);
			boSetter.accept(newAValue);
			errorHandler.accept(null);
			return true;
		} catch (Exception e) {
			if (errorHandler != null) {
				errorHandler.accept(e);
			}
			return false;
		}
	}

	@Override
	public void restoreBOValue() {
		boSetter.accept(oldBoValue);
	}
}
