package de.freund.syncer;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncTransaction<BO, BOValue, UIValue> implements BOCapable<BO> {
	private Function<BO, BOValue> boGetter;
	private BiConsumer<BO, BOValue> boSetter;
	private Supplier<UIValue> uiGetter;
	private Consumer<UIValue> uiSetter;
	private Function<BOValue, UIValue> abTransformer;
	private Function<UIValue, BOValue> baTransformer;
	private BOValue oldBoValue;
	private Consumer<Exception> errorHandler;
	private Supplier<BO> objectStrategy;

	public SyncTransaction(Function<BO, BOValue> boGetter, BiConsumer<BO, BOValue> boSetter, Supplier<UIValue> uiGetter, Consumer<UIValue> uiSetter,
			Function<BOValue, UIValue> boUiTransformer, Function<UIValue, BOValue> uiBoTransformer, Consumer<Exception> errorHandler) {
		this.errorHandler = errorHandler;
		this.abTransformer = boUiTransformer;
		this.baTransformer = uiBoTransformer;
		connectBO(boGetter, boSetter);
		connectUI(uiGetter, uiSetter);
	}

	public void setHinweg(Function<BO, BOValue> boGetter, Consumer<UIValue> uiSetter, Function<BOValue, UIValue> boUiTransformer) {
		this.boGetter = Objects.requireNonNull(boGetter);
		this.uiSetter = Objects.requireNonNull(uiSetter);
		abTransformer = Objects.requireNonNull(boUiTransformer);
	}

	public void setRueckweg(Supplier<UIValue> uiGetter, BiConsumer<BO, BOValue> boSetter, Function<UIValue, BOValue> uiBoTransformer) {
		this.uiGetter = Objects.requireNonNull(uiGetter);
		this.boSetter = Objects.requireNonNull(boSetter);
		baTransformer = Objects.requireNonNull(uiBoTransformer);
	}

	private void connectUI(Supplier<UIValue> bGetter, Consumer<UIValue> bSetter) {
		this.uiGetter = Objects.requireNonNull(bGetter);
		this.uiSetter = Objects.requireNonNull(bSetter);
	}

	private void connectBO(Function<BO, BOValue> boGetter, BiConsumer<BO, BOValue> boSetter) {
		this.boGetter = Objects.requireNonNull(boGetter);
		this.boSetter = Objects.requireNonNull(boSetter);
	}

	@Override
	public boolean loadUiFromBO() {
		BOValue boValue = boGetter.apply(objectStrategy.get());
		UIValue newBValue = abTransformer.apply(boValue);
		uiSetter.accept(newBValue);
		return true;
	}

	@Override
	public boolean writeUiToBO() {
		try {
			oldBoValue = boGetter.apply(objectStrategy.get());
			UIValue uiValue = uiGetter.get();
			BOValue newAValue = baTransformer.apply(uiValue);
			boSetter.accept(objectStrategy.get(), newAValue);
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
		boSetter.accept(objectStrategy.get(), oldBoValue);
	}

	@Override
	public void setObject(Supplier<BO> objectStrategy) {
		this.objectStrategy = objectStrategy;
	}
}
