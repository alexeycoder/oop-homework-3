package myapp.concrete;

/* Для сериализации в XML и JSON используется библиотека XStream
 * https://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.4.19/xstream-1.4.19.jar
 * https://repo1.maven.org/maven2/org/codehaus/jettison/jettison/1.2/jettison-1.2.jar
 * 
 * (https://x-stream.github.io)
 * 
 * Стандартный java.beans.XMLEncoder генерирует неудобочитаемый XML.
*/
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import myapp.entities.Worker;

class XmlDocument extends WorkerDocument {

	public XmlDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {

		try {
			XStream xstream = new XStream(new DomDriver());
			xstream.alias(Worker.class.getSimpleName().toLowerCase(), Worker.class);
			String xmlStr = xstream.toXML(worker);
			System.out.println(xmlStr);
			System.out.println();

		} catch (XStreamException e) {
			System.err.println("Ошибка: что-то пошло не так в процессе формирования XML.");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		} catch (NoClassDefFoundError e) {
			System.err.println("Ошибка: вероятно не установлены требуемые библиотеки XStream.");
		}

		// // XMLEncoder выдаёт не очень human-readable XML
		// try (ByteArrayOutputStream os = new ByteArrayOutputStream();
		// XMLEncoder xmlEncoder = new XMLEncoder(os)) {
		// xmlEncoder.writeObject(worker);
		// xmlEncoder.flush();
		// System.out.println(os.toString());
		// } catch (IOException ex) {
		// System.err.println("Ошибка: что-то пошло не так в процессе формирования
		// XML.");
		// System.err.println(ex.getMessage());
		// ex.printStackTrace(System.err);
		// }
	}
}
