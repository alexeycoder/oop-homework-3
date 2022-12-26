package myapp.concrete;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import myapp.abstractions.Button;
import myapp.abstractions.Dialog;
import myapp.abstractions.Document;
import myapp.entities.Worker;
import myapp.enums.DocType;

/** Конкретная реализация -- диалог-сохранялка. Консольная. */
public class SaveAsDialog implements Dialog {

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
		printTitle();
		askFormat();
		printSeparator();
	}

	@Override
	public void clickButton() {
		if (document == null) {
			System.err.println("Ошибка: тип документа не задан.");
			return;
		}
		System.out.println("Нажата кнопка сохранения.\nРезультат:\n");

		button.click();
	}

	private void askFormat() {
		printSeparator();
		System.out.println("Сохраняемый Worker:");
		System.out.println(worker.toString());
		printSeparator();

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

	private static void printTitle() {
		System.out.println("\nДиалог Сохранения\n");
	}

	private static void printSeparator() {
		System.out.println("=".repeat(40));
	}
}
