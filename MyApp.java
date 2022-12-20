package edu.alexey.seminar3;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import edu.alexey.seminar3.Main.Worker;

// АБСТРАКЦИИ:

/**
 * Абстракция некоего Документа, умеющего сохраняться.
 * Метод saveAs(), служащий для цели "как бы"-сохранения документа,
 * выводит его содержимое в консоль.
 * 
 * Примечание: Здесь и далее Абстракция -- в философском смысле, не тип Java.
 * 
 * Примечание 2: Возможно, было бы логично документу вместо функции сохранения
 * себя, иметь функцию получения своего строкового представления в своём
 * специфичном формате,
 * а функцию сохранения делегировать диалогу-сохранялке (см. далее).
 * Однако следуем тех.заданию с семинара, где у документа д.б. saveAs().
 */
interface Document {
	public void saveAs();
}

/**
 * Абстракция некоей кнопки, умеющей нажиматься.
 * То, как она информирует своего "клиента" о своём нажатии --
 * не специфицировано!
 * 
 * В качестве варианта реализации оповещения о нажатии, будем использовать
 * приём с созданием непосредственно в клиенте экземпляров анонимного типа,
 * производного от данного интерфейса, таким образом в нём же определяя
 * метод click(), в котором благодаря замыканию будем иметь доступ
 * к non-private членам класса-клиента (в нашем случае клиент -- Документ).
 * Это позволит в методе click() производной Кнопки вызвать нужный метод
 * конкретной реализации клиента, т.о. имитировав информирование клиента
 * о нажатии.
 */
interface Button {
	public void click();
}

/**
 * Абстракция некоего диалога с пользователем.
 * Типа как диалоговое окошко, с функцией сохранения чего-либо.
 * 
 * В нашем случае конкретная реализация будет:
 * 1) завязана на консоль, и
 * 2) отвечать за "сохранение" документа в формате на выбор пользователя.
 */
interface Dialog {
	/**
	 * Запускает диалог с целью спросить что-то у пользователя.
	 * (В конкретной реализации ниже будет выяснять формат сохранения.)
	 */
	void start();

	/**
	 * Позволяет снаружи "кликнуть" по кнопочке, размещённой в диалоге.
	 * (В конкретной реализации ниже будет по-смыслу кнопкой "Сохранить".)
	 */
	void clickButton();
}

// ТОЧКА ВХОДА:

public class MyApp {
	public static void main(String[] args) {
		// Экземпляр того, что будем "сохранять":
		Worker worker = new Worker("Johnny Five", 123, 100500);

		// Создаём экземпляр нашей "сохранялки" в формате на выбор,
		// и передаём ему то, что надо сохранить:
		Dialog dialog = new SaveAsDialog(worker);
		// Запускаем "сохранялку", на этом этапе её цель -- выяснить у пользователя
		// в каком же формате сохранить переданное ей чудо:
		dialog.start();

		// Имитируем нажатие кнопки "Сохранить" пользоваетелем:
		dialog.clickButton();
	}

	/**
	 * Тип сущности с которым работаем (будем "как бы"-сохранять в разных форматах).
	 * Работник.
	 */
	public static class Worker implements java.io.Serializable {
		private String name;
		private int age;
		private int salary;

		public Worker() {
		}

		public Worker(String name, int age, int salary) {
			this.name = name;
			this.age = age;
			this.salary = salary;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public int getSalary() {
			return salary;
		}

		public void setSalary(int salary) {
			this.salary = salary;
		}

		@Override
		public String toString() {
			return String.format("Имя: %s\nВозраст %d\nЗ/п %d", name, age, salary);
		}

		// @Override
		// public boolean equals(Object obj) {
		// if (this == obj) {
		// return true;
		// }
		// if (obj instanceof Worker that) {
		// boolean nameEquals = this.name != null ? this.name.equals(that.name) :
		// that.name == null;
		// return nameEquals && this.age == that.age && this.salary == that.salary;
		// }
		// return false;
		// }

		// @Override
		// public int hashCode() {
		// int result = name != null ? name.hashCode() : 0;
		// result = 31 * result + age;
		// result = 31 * result + salary;
		// return result;
		// }
	}
}

// КОНКРЕТНОЕ:

/** Поддерживаемые форматы преобразования документа. */
enum DocType {
	XML,
	MD,
	JSON;

	public static boolean isEligibleString(String str) {
		return Arrays.stream(DocType.values()).map(DocType::name).anyMatch(n -> n.equals(str));
	}
}

/** Конкретная реализация -- диалог-сохранялка. Консольная. */
class SaveAsDialog implements Dialog {

	/* С консолью работает только диалог. Здесь и оставим Scanner. */
	private static final Scanner scanner = new Scanner(System.in);

	private final Worker worker;
	private final Button button;

	protected Document document;

	/**
	 * Консольная диалог-сохранялка.
	 * 
	 * @param whatToSave Экземпляр сохраняемой сущности. Должен быть не null.
	 */
	public SaveAsDialog(Worker whatToSave) {
		this.worker = Objects.requireNonNull(whatToSave);
		this.button = new Button() {
			@Override
			public void click() {
				document.saveAs();
			}
		};
	}

	@Override
	public void start() {
		askFormat();
	}

	@Override
	public void clickButton() {
		if (document == null) {
			System.out.println("Ошибка: Сперва надо выбрать тип документа.");
			return;
		}

		button.click();
	}

	private void askFormat() {
		System.out.println("=".repeat(40));
		System.out.println("Сохраняемый Worker:");
		System.out.println(worker.toString());
		System.out.println("=".repeat(40));

		String allowedInputsStr = Arrays.stream(DocType.values()).map(DocType::name).collect(Collectors.joining(", "));
		String rawInput;
		while (true) {
			System.out.printf("Задайте тип документа (%s): ", allowedInputsStr);
			rawInput = scanner.nextLine().toUpperCase();
			if (DocType.isEligibleString(rawInput)) {
				break;
			} else {
				System.out.printf("Некорректный ввод! Допустимые варианты ввода: %s. Пожалуйста попробуйте ещё раз.\n",
						allowedInputsStr);
			}
		}
		DocType docType = DocType.valueOf(rawInput);
		document = selectDocument(docType);
	}

	private Document selectDocument(DocType docType) {
		return switch (docType) {
			case XML -> new XmlDocument(worker);
			case MD -> new MdDocument(worker);
			case JSON -> new JsonDocument(worker);
		};
	}
}

abstract class WorkerDocument implements Document {

	protected Worker worker;

	public WorkerDocument(Worker worker) {
		this.worker = Objects.requireNonNull(worker);
	}
}

class XmlDocument extends WorkerDocument {

	public XmlDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
				XMLEncoder xmlEncoder = new XMLEncoder(os)) {
			xmlEncoder.writeObject(worker);
			xmlEncoder.flush();
			System.out.println(os.toString());
		} catch (IOException ex) {
			System.err.println("Ошибка: что-то пошло не так в процессе формирования XML.");
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}

class MdDocument extends WorkerDocument {

	public MdDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		System.out.printf("Типа вывожу в MD %d %d %s\n", worker.getAge(), worker.getSalary(), worker.getName());
	}
}

class JsonDocument extends WorkerDocument {
	public JsonDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		System.out.printf("Типа вывожу в JSON %d %d %s\n", worker.getAge(), worker.getSalary(), worker.getName());
	}
}
