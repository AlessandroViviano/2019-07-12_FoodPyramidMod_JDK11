package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDAO {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public void loadAllFood(Map<Integer, Food> idMap, int x) {
		String sql = "SELECT portion.food_code AS id, food.display_name AS name, COUNT(portion.portion_id) AS cnt " + 
				"FROM food_pyramid_mod.portion, food_pyramid_mod.food " + 
				"WHERE portion.food_code = food.food_code " + 
				"GROUP BY portion.food_code " + 
				"HAVING COUNT(cnt) = ?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Food f = new Food(rs.getInt("id"), rs.getString("name"));
				
				if(!idMap.containsKey(rs.getInt("id"))) {
					idMap.put(f.getFood_code(), f);
				}
			}
			
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al database", e);
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, Food> idMap){
		String sql = "SELECT f1.food_code AS f1, f2.food_code AS f2, AVG(c1.condiment_calories) AS peso " + 
				"FROM food_pyramid_mod.food_condiment AS f1, food_pyramid_mod.food_condiment AS f2, " + 
				"food_pyramid_mod.condiment AS c1, food_pyramid_mod.condiment AS c2 " + 
				"WHERE f1.condiment_code = c1.condiment_code AND " + 
				"f2.condiment_code = c2.condiment_code AND " + 
				"f1.food_code > f2.food_code AND " + 
				"f1.condiment_code = f2.condiment_code " + 
				"GROUP BY f1.food_code, f2.food_code " + 
				"HAVING COUNT(*) > 0";
		
		List<Adiacenza> adiacenze = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Adiacenza a = new Adiacenza(idMap.get(rs.getInt("f1")), idMap.get(rs.getInt("f2")),
						rs.getDouble("peso"));
				if(!adiacenze.contains(a) && idMap.containsKey(rs.getInt("f1")) && idMap.containsKey(rs.getInt("f2"))) {
					adiacenze.add(a);
				}
			}
			
			conn.close();
			return adiacenze;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al database", e);
		}
	}
	
	public List<Food> getFoods(int x){
		String sql = "SELECT portion.food_code AS id, food.display_name AS name, COUNT(portion.portion_id) AS cnt " + 
				"FROM food_pyramid_mod.portion, food_pyramid_mod.food " + 
				"WHERE portion.food_code = food.food_code " + 
				"GROUP BY portion.food_code " + 
				"HAVING COUNT(cnt) = ?";
		
		List<Food> cibi = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				cibi.add(new Food(rs.getInt("id"), rs.getString("name")));
			}
			conn.close();
			return cibi;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al database", e);
		}
	}
}

