package myapp.abstractions;

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
public interface Button {
	public void click();
}
