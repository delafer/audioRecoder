package org.delafer.recoder.model.enums.factory;

import java.util.HashMap;
import java.util.Map;

public final class Register {

	private static final transient Map<String, Enum<?>> map = new HashMap<>(32);

	private static final String getKey(Enum<?> en, Object key) {
		StringBuilder sb = new StringBuilder();
		sb.append(en.getClass().getSimpleName()).append('.').append(key != null ? key.toString() : '!');
		return sb.toString();
	}

	public static final void register(final Enum<?> en, final Object key) {
		map.put(getKey(en, key), en);
	}

	public static final Enum<?> getByKey(final Enum<?> en, final Object key) {
		return map.get(getKey(en, key));
	}

}
