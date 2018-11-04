package com.bruce.recipefinder.controller;

import com.bruce.recipefinder.entity.Fridge;
import com.bruce.recipefinder.entity.Recipe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

//import jdk.nashorn.internal.parser.JSONParser;


@Controller
public class MainController {
    public ArrayList<Fridge> fridgeList = new ArrayList<Fridge>();
    public ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

    //Home page get fridge, recipes, recommend result
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String getFridgeItemList(Model model) {
        model.addAttribute("recipeList",recipeList);
        model.addAttribute("fridgeList",fridgeList);
        return "index";
    }

    //Fridge page get fridge list
    @RequestMapping(value="/fridge",method = RequestMethod.GET)
    public String getFridgeList(Model model) {
        model.addAttribute("fridgeList",fridgeList);
        return "fridge";
    }
}
