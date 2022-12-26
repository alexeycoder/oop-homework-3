package myapp.enums;

import java.util.Arrays;

/** Поддерживаемые форматы преобразования документа. */
public enum DocType {
	XML,
	MD,
	JSON;

	public static boolean isEligibleString(String str) {
		return Arrays.stream(DocType.values()).map(DocType::name).anyMatch(n -> n.equals(str));
	}
}
