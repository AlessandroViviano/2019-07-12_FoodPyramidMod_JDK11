package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INIZIO_PREPARAZIONE, //Viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE, //La stazione ha completato la preparazione di un cibo
	}
	
	private Double time; //Tempo in minuti
	private EventType type;
	private Stazione stazione;
	private Food food;
	
	
	public Event(Double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.stazione = stazione;
		this.food = food;
		this.type = type;
	}


	public Double getTime() {
		return time;
	}


	public Stazione getStazione() {
		return stazione;
	}


	public Food getFood() {
		return food;
	}
	
	public EventType getType() {
		return type;
	}
	
	
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

}
