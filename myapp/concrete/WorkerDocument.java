package myapp.concrete;

import java.util.Objects;

import myapp.abstractions.Document;
import myapp.entities.Worker;

abstract class WorkerDocument implements Document {

	protected Worker worker;

	public WorkerDocument(Worker worker) {
		this.worker = Objects.requireNonNull(worker);
	}
}
