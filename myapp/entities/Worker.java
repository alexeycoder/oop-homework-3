package myapp.entities;

/**
 * Тип сущности с которым работаем (будем "как бы"-сохранять в разных форматах).
 * Работник.
 */
public class Worker implements java.io.Serializable {
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
