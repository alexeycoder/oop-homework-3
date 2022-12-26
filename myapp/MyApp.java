package myapp;

import myapp.abstractions.Dialog;
import myapp.concrete.SaveAsDialog;
import myapp.entities.Worker;

public class MyApp {
	public static void main(String[] args) {
		// Экземпляр сущности, что будем "сохранять":
		Worker worker = new Worker("Johnny Five", 123, 100500);

		// Создаём экземпляр нашей "сохранялки" в формате на выбор,
		// и передаём ему то, что надо сохранить:
		Dialog dialog = new SaveAsDialog(worker);

		// Запускаем "сохранялку", на этом этапе её цель -- выяснить у пользователя
		// в каком же формате сохранить переданное ей чудо:
		dialog.start();

		// Имитируем нажатие кнопки "Сохранить" пользователем:
		dialog.clickButton();
	}
}
