package it.polito.tdp.food.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		m.creaGrafo(6);
		
		System.out.format("Grafo creato! %d vertici e %d archi", m.vertici(), m.archi());
	}

}
