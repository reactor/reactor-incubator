package reactor.groovy.support;

import java.util.function.Consumer;

import groovy.lang.Closure;
import reactor.core.tuple.Tuple;

/**
 * Invokes a {@link groovy.lang.Closure} using the contents of the incoming {@link reactor.core.tuple.Tuple} as the
 * arguments.
 *
 * @author Jon Brisbin
 */
public class ClosureTupleConsumer implements Consumer<Tuple> {

	private final Closure cl;

	public ClosureTupleConsumer(Closure cl) {
		this.cl = cl;
	}

	@Override
	public void accept(Tuple tup) {
		cl.call(tup.toArray());
	}

}
