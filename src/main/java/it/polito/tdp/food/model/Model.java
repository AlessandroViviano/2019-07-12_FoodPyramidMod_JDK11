package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private FoodDAO dao;
	private List<Adiacenza> adiacenze;
	private List<Food> cibi;
	
	public Model() {
		dao = new FoodDAO();
		idMap = new HashMap<>();
		cibi = new ArrayList<>();
	}
	
	public List<Food> getFoods(int x){
		cibi = dao.getFoods(x);
		
		return cibi;
	}
	
	public void creaGrafo(int x) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.loadAllFood(idMap, x);
		adiacenze = dao.getAdiacenze(idMap);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo gli archi
		for(Adiacenza a: adiacenze) {
			if(this.grafo.getEdge(a.getF1(), a.getF2())==null && this.grafo.getEdge(a.getF2(), a.getF1())==null) {
				Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
	}
	
	public int vertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int archi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		List<FoodCalories> classifica = new ArrayList<>();
		
		List<Food> vicini = Graphs.neighborListOf(this.grafo, f);
		
		for(Food v: vicini) {
			Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f, v));
			classifica.add(new FoodCalories(v, peso));
		}
		
		Collections.sort(classifica);
		return classifica;
	}
	
	public String simula(Food cibo, int K) {
		Simulator sim = new Simulator(this.grafo, this);
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		
		String messaggio = String.format("Preparati %d cibi in %f minuti\n",
				sim.getCibiPreparati(), sim.getTempoPreparazione());
		return messaggio;
	}
	
}
