package myapp.concrete;

/* Для сериализации в XML и JSON используется библиотека XStream
 * https://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.4.19/xstream-1.4.19.jar
 * https://repo1.maven.org/maven2/org/codehaus/jettison/jettison/1.2/jettison-1.2.jar
 * 
 * (https://x-stream.github.io)
*/
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import myapp.entities.Worker;

class JsonDocument extends WorkerDocument {
	public JsonDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		try {
			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			xstream.setMode(XStream.NO_REFERENCES);
			xstream.alias(Worker.class.getSimpleName().toLowerCase(), Worker.class);
			String jsonStr = xstream.toXML(worker);
			System.out.println(jsonStr);
			System.out.println();

		} catch (XStreamException e) {
			System.err.println("Ошибка: что-то пошло не так в процессе формирования JSON.");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		} catch (NoClassDefFoundError e) {
			System.err.println("Ошибка: вероятно не установлены требуемые библиотеки XStream.");
		}
	}
}
