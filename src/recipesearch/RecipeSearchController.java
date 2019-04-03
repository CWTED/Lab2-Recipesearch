
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController backend = new RecipeBackendController();
    private ObservableList<String> availableIngredients;
    private ObservableList<String> availableCuisines;

    @FXML
    private Label maxTimeLabel;

    @FXML
    private AnchorPane recipeDetailPane;

    @FXML
    private SplitPane searchPane;

    @FXML
    private ComboBox<String> mainIngredientBox;

    @FXML
    private ComboBox<String> cuisineBox;

    @FXML
    private RadioButton diffAllbutton;

    @FXML
    private RadioButton diffEasybutton;

    @FXML
    private RadioButton diffMediumbutton;

    @FXML
    private RadioButton diffHardbutton;

    @FXML
    private Button closeButton;

    @FXML
    private Spinner<Integer> maxPriceSpinner;

    @FXML
    private Slider maxTimeSlider;

    @FXML
    private FlowPane searchResultPane;

    @FXML
    private Label listItemLabel;

    @FXML
    private ImageView listItemPic;

    @FXML
    private Label recipeDesc;

    @FXML
    private ImageView appImg;

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    private void updateRecipeList() {
        searchResultPane.getChildren().clear();
        List<Recipe> items = backend.getRecipes();
        for (Recipe r : items) {
            searchResultPane.getChildren().add(recipeListItemMap.get(r.getName()));
        }
        /*searchResultPane.getChildren().clear();
        List<Recipe> recipes = backend.getRecipes();
        for (Recipe r : recipes){
            searchResultPane.getChildren().add(new RecipeListItem(r, this));
        }*/

    }

    private void populateRecipeDetailView(Recipe recipe) {
        listItemLabel.setText(recipe.getName());
        listItemPic.setImage(recipe.getFXImage());
    }

    @FXML
    public void closeRecipeView() {
        recipeDetailPane.toBack();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        availableIngredients = FXCollections.observableArrayList(
                "Kött",
                "Fisk",
                "Kyckling",
                "Vegetarisk"
        );
        availableCuisines = FXCollections.observableArrayList(
                "Sverige",
                "Grekland",
                "Asien",
                "Afrika",
                "Indien",
                "Frankrike"

        );

        mainIngredientBox.setItems(availableIngredients);
        mainIngredientBox.getSelectionModel().select("Visa alla");

        cuisineBox.setItems(availableCuisines);
        cuisineBox.getSelectionModel().select("Visa alla");

        maxPriceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 0, 10));

        maxPriceSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                backend.setMaxPrice(newValue);
                updateRecipeList();
            }
        });

        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) {
                    //focusgained - do nothing
                } else {
                    Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    backend.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });

        maxTimeLabel.setText("0 min");

        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int value = (int) Math.round(maxTimeSlider.getValue());
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {
                    backend.setMaxTime(value);
                    maxTimeLabel.setText(Integer.toString(value) + " min");
                    updateRecipeList();
                }

            }
        });


        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        diffAllbutton.setToggleGroup(difficultyToggleGroup);
        diffEasybutton.setToggleGroup(difficultyToggleGroup);
        diffHardbutton.setToggleGroup(difficultyToggleGroup);
        diffMediumbutton.setToggleGroup(difficultyToggleGroup);
        diffAllbutton.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    backend.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        mainIngredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backend.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backend.setCuisine(newValue);
                updateRecipeList();
            }
        });
        for (Recipe recipe : backend.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();

    }

}