package com.bruce.recipefinder.controller;

import com.bruce.recipefinder.entity.Fridge;
import com.bruce.recipefinder.entity.Ingredient;
import com.bruce.recipefinder.entity.Recipe;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import jdk.nashorn.internal.parser.JSONParser;


@Controller
public class MainController {
    public ArrayList<Fridge> fridgeList = new ArrayList<Fridge>();
    public ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private static ArrayList<Recipe> availableRecipesList = new ArrayList<Recipe>();


    //Home page get fridge, recipes, recommend result
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String getItemList(Model model) {
        model.addAttribute("recipeList",recipeList);
        model.addAttribute("fridgeList",fridgeList);
        model.addAttribute("recipeToday",recommendRecipe(availableRecipesList).getName());
        return "index";
    }

    //Fridge page get fridge list
    @RequestMapping(value="/fridge",method = RequestMethod.GET)
    public String getFridgeList(Model model) {
        model.addAttribute("fridgeList",fridgeList);
        return "fridge";
    }

    //upload fridge with CSV format
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addFridgeObject(@RequestParam(value = "fridgeObject") String fridgeCsvFile) {
        Fridge fridgeObject = new Fridge();
        fridgeObject.setItem(fridgeCsvFile);
        fridgeObject.setId(fridgeList.size()+1);
        fridgeList.add(fridgeObject);
        return "redirect:/fridge";
    }

    //Upload Fridge CSV file
    @RequestMapping(value = "/uploadFridgeCSV", method = RequestMethod.POST)
    public String uploadFridgeCSVFile(@RequestParam(value = "uploadFridgeCSV") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/fridge";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);
            readFridgeCSV(file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/fridge";
    }

    //Read CSV file
    public void readFridgeCSV(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            // Reading header, Ignoring
            String line = br.readLine();
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String item = fields[0];
                    String amount = fields[1];
                    String unit = fields[2];
                    String useBy = fields[3];
                    Fridge fridge = new Fridge(fridgeList.size() + 1, item, Double.valueOf(amount), unit, new SimpleDateFormat("dd/MM/yyyy").parse(useBy));
                    fridgeList.add(fridge);
                }

            }
            br.close();
        }catch (FileNotFoundException exception) {
            System.out.println(exception);
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    //Clear the data in Fridge page
    @RequestMapping(value = "/resetFridge", method = RequestMethod.POST)
    public String resetFridgeData(@RequestParam(value = "ResetFridge") String fridgeCsvFile) {
        fridgeList.clear();
        return "redirect:/fridge";
    }

    //Fridge Page Delete specific item
    @RequestMapping(value = "/deleteFridgeItem", method = RequestMethod.POST)
    public String deleteFridgeObject(@RequestParam(value = "deleteFridgeItemId") String fridgeItemId) {
//        System.out.println(fridgeItemId);
        for(int x = 0; x < fridgeList.size(); x = x+1) {
//            System.out.println(fridgeItemId);
            if (fridgeList.get(x).getId() == Integer.valueOf(fridgeItemId)){
                fridgeList.remove(x);
            }
        }
        return "redirect:/fridge";
    }

    //Fridge page get fridge list
    @RequestMapping(value="/recipes",method = RequestMethod.GET)
    public String getRecipeList(Model model) {
        model.addAttribute("recipesList",recipeList);
        return "recipes";
    }

    //Recipe page upload recipe JSON file
    @RequestMapping(value = "/uploadRecipeJSON", method = RequestMethod.POST)
    public String uploadRecipeJSONFile(@RequestParam(value = "uploadRecipeJSON") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/recipes";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);
            readRecipeJsonFile(file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/recipes";
    }

    //Recipe Page read JSON File
    public void readRecipeJsonFile(String path) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(path));
            JSONArray recipeArrayList = (JSONArray) obj;

            for(Object o : recipeArrayList){
                JSONObject recipeObject = (JSONObject) o;
                String name = (String) recipeObject.get("name");
                JSONArray ingredientList = (JSONArray) recipeObject.get("ingredients");
                ArrayList<Ingredient> newIngredientList = new ArrayList<Ingredient>();
                for(Object os : ingredientList){
                    JSONObject recipeObjects = (JSONObject) os;
                    String item = (String) recipeObjects.get("item");
                    String amount = (String) recipeObjects.get("amount");
                    String unit = (String) recipeObjects.get("unit");
                    Ingredient newIngredient = new Ingredient(item,Double.valueOf(amount),unit);
                    newIngredientList.add(newIngredient);
                }
                Recipe newRecipe = new Recipe();
                newRecipe.setId(recipeList.size()+1);
                newRecipe.setName(name);
                newRecipe.setIngredients(newIngredientList);
                recipeList.add(newRecipe);
            }
        }catch (FileNotFoundException exception) {
            System.out.println(exception);
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    @RequestMapping(value="/generateRecipe",method = RequestMethod.POST)
    public String generateRecommendRecipe(@RequestParam(value = "generateRecipes") String ll){
        for(int x = 0; x < recipeList.size(); x = x+1) {
            int recipeListSize = recipeList.get(x).getIngredients().size();
            int compareListSize = 0;
            for(int n = 0; n <recipeList.get(x).getIngredients().size(); n++){
                for(int k = 0;k<fridgeList.size();k++){
                    if (fridgeList.get(k).getItem().equals(recipeList.get(x).getIngredients().get(n).getItem()) &&
                            fridgeList.get(k).getAmount() >= recipeList.get(x).getIngredients().get(n).getAmount()){
                        compareListSize = compareListSize + 1;
                    }
                }
            }
            if (recipeListSize == compareListSize){
                availableRecipesList.add(recipeList.get(x));
//                System.out.println(recipeList.get(x).getName());
            }
        }
        return "redirect:/";
    }

    public  Recipe recommendRecipe(List<Recipe> availableRecipesList){
        Date closeDate = new Date();
        Date today = new Date();
        Recipe recommendRecipe = new Recipe();
        for (Recipe recommend : availableRecipesList){
            for (Ingredient ingredient :recommend.getIngredients()){
                for(Fridge f : fridgeList){
                    if (today.compareTo(closeDate) == 0){
                        recommendRecipe = recommend;
                        closeDate = f.getUseBy();
                    }
                    if (f.getItem().equals(ingredient.getItem())){
                        if (f.getUseBy().before(closeDate)){
                            recommendRecipe = recommend;
                            closeDate = f.getUseBy();
                        }
                    }
                }
            }
        }

        if(availableRecipesList.size() == 0 || recommendRecipe == null){
            recommendRecipe =  new Recipe();
            recommendRecipe.setName("Order Takeout");

        }



//
//        System.out.println("\t > No. of valid Recipes to cook: " + availableRecipesList.size());
//        Date today = new Date();
//        Recipe recommendedRecipe = null;
//        List<Date> usedByDate = new ArrayList<Date>();
//        Map<Recipe, Date> recipeScore = new HashMap<Recipe, Date>();
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        Date closestDate = null;
//        String itemExpiringSoon = null;
//
//        // Gather the items expiring soon
//        for(Fridge f : fridgeList){
//            if (f.getUseBy().after(today)){
//
//                if(closestDate == null){
//                    closestDate = f.getUseBy();
//                    itemExpiringSoon = f.getItem();
//                }else{
//                    if (f.getUseBy().before(closestDate)){
//                        closestDate = f.getUseBy();
//                        itemExpiringSoon = f.getItem();
//                    }
//                }
//            }
//        }
//
//        if (itemExpiringSoon!=null){
//            System.out.println("\t > " + itemExpiringSoon + " is expiring soon at " + dateFormatter.format(closestDate));
//        }
//
//        for (Recipe recipe : availableRecipesList){
//
//            for (Ingredient ingredient : recipe.getIngredients()){
//
//                if(ingredient.getItem().equals(itemExpiringSoon)){
//                    recommendedRecipe = recipe;
//                }
//            }
//        }
//
//        if(availableRecipesList.size() == 0 || recommendedRecipe == null){
//            recommendedRecipe =  new Recipe();
//            recommendedRecipe.setName("Order Takeout");
//
//        }

        return recommendRecipe;

    }

}
