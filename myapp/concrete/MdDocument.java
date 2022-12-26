package myapp.concrete;

import java.lang.reflect.Method;
import java.util.Arrays;

import myapp.entities.Worker;

class MdDocument extends WorkerDocument {
	private static final String GETTER_TOKEN = "get";

	public MdDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		var getters = Arrays.stream(worker.getClass().getDeclaredMethods()).filter(MdDocument::isGetter).toList();
		StringBuilder sb = new StringBuilder(worker.getClass().getSimpleName()).append(":\n");
		for (var getter : getters) {
			var propName = getter.getName().substring(GETTER_TOKEN.length());
			Object propValue = null;
			try {
				propValue = getter.invoke(worker);
			} catch (Exception e) {
				propValue = "не удалось прочитать значение свойства";
			}
			sb.append("* _").append(propName).append("_: ")
					.append(propValue.toString()).append("\n");
		}
		System.out.println(sb.toString());
	}

	private static boolean isGetter(Method method) {
		return method.getName().startsWith(GETTER_TOKEN)
				&& method.getName().length() > GETTER_TOKEN.length()
				&& method.getParameterTypes().length == 0
				&& !Void.class.equals(method.getReturnType());
	}
}
