import java.util.function.Consumer;
import java.util.function.Function;

import de.freund.syncer.BOCapable;

public class ST2<O, A, B> implements BOCapable {

	private O object;
	private Function<O, A> aGetter;
	private Consumer<B> bSetter;
	private Function<A, B> abTransformer;
	
	public ST2(Function<O, A> aGetter, Consumer<B> bSetter, Function<A, B> abTransformer) {
		this.aGetter = aGetter;
		this.bSetter = bSetter;
		this.abTransformer = abTransformer;
	}
	
	public void setObject(O object) {
		this.object = object;
	}
	
	@Override
	public boolean loadUiFromBO() {
		A aValue = aGetter.apply(object);
		B bValue = abTransformer.apply(aValue);
		bSetter.accept(bValue);
		return true;
	}

	@Override
	public boolean writeUiToBO() {
		return true;
	}

	@Override
	public void restoreBOValue() {
		// TODO Auto-generated method stub
		
	}
}
