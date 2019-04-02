package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.List;

public class RecipeBackendController {
    private final RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private String cuisine;
    private String[] availableCuisines = {"Sverige", "Grekland", "Asien","Frankrike", "Indien", "Afrika"};
    private String mainIngredient;
    private String[] availableIngredients = {"Kött", "Fisk", "Kyckling","Vegetarisk"};
    private String difficulty;
    private String[] availableDifficulties = {"Lätt", "Mellan", "Svår"};
    private int maxPrice;
    private int maxTime;

    public List<Recipe> getRecipes(){
        List<Recipe> filter = db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        return filter;
    }
    public void setCuisine(String cuisine){
        this.cuisine = null;
        for (int i = 0; i < availableCuisines.length - 1; i++){
            if (availableCuisines[i].equals(cuisine)){
                this.cuisine = cuisine;
                break;
            }
        }
    }
    public void setMainIngredient(String mainIngredient){
        this.mainIngredient = null;
        for (int i = 0; i < availableIngredients.length - 1; i++){
            if (availableIngredients[i].equals(mainIngredient)){
                this.mainIngredient = mainIngredient;
                break;
            }
        }
    }
    public void setDifficulty(String difficulty){ //om ej funkar prob för att ej samma object
        this.difficulty = null;
        for (int i = 0; i < availableIngredients.length - 1; i++){
            if (availableDifficulties[i].equals(difficulty)){
                this.difficulty = difficulty;
                break;
            }
        }
    }
    public void setMaxPrice(int maxPrice){
        this.maxPrice = 0;
        if (maxPrice>0){
            this.maxPrice = maxPrice;
        }
    }
    public void setMaxTime(int maxTime){
        this.maxTime = 0;
        if (maxTime>0 && maxTime<=150 && (maxTime % 10 == 0)){
            this.maxTime = maxTime;
        }
    }
}
