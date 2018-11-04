package com.bruce.recipefinder.controller;

import com.bruce.recipefinder.entity.Fridge;
import com.bruce.recipefinder.entity.Recipe;
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
}
