package it.polito.tdp.food.model;

public class FoodCalories implements Comparable<FoodCalories>{
	
	private Food food;
	private Double calories;
	
	public FoodCalories(Food food, double calories) {
		super();
		this.food = food;
		this.calories = calories;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public double getCalories() {
		return calories;
	}

	public void setCalories(double calories) {
		this.calories = calories;
	}
	
	public int compareTo(FoodCalories other) {
		return -(this.calories.compareTo(other.calories));
	}
}
