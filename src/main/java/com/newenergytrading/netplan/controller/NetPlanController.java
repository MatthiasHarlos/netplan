package com.newenergytrading.netplan.controller;

import com.newenergytrading.netplan.domain.Knot;
import com.newenergytrading.netplan.forms.KnotInputForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class NetPlanController {

    private static List<KnotInputForm> knotInputFormList = new ArrayList<>();
    private static List<Knot> knotList = new ArrayList<>();
    private static List<List<Knot>> criticalPathsClean = new ArrayList<>();

    @GetMapping("/")
    public String getStartPage(){
        return "startpage";
    }

    @GetMapping("input")
    public String getKnotFormInput(Model model) {
        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "eingeben");
        return "knotInputForm";
    }

    @PostMapping("changeInput")
    public String changeSavedKnotInputForm(Model model, KnotInputForm changeKnotInputForm) {
        model.addAttribute("knotInputFormToSave", knotInputFormList.get(changeKnotInputForm.getOperationNumber()));
        model.addAttribute("operationNumber", changeKnotInputForm.getOperationNumber()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "ändern <a href='https://netzplan.herokuapp.com/input'>|abbrechen|</a>");
        return "knotInputForm";
    }

    @GetMapping("deletedOld")
    public String deleteOldNetPlan () {
        knotInputFormList = new ArrayList<>();
        knotList = new ArrayList<>();
        return "redirect:input";
    }


    @PostMapping("saveInput")
    public String saveKnotFormInput(@Valid @ModelAttribute("knotInputFormToSave") KnotInputForm knotInputFormToSave, BindingResult bindingResult, Model model) {
        knotList = new ArrayList<>();
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "eingeben");
        //validate(knotInputFormToSave, bindingResult);
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("error");
            model.addAttribute("knotInputFormToSave", knotInputFormToSave);
            return "knotInputForm";
        }

        model.addAttribute("knotInputFormToSave", new KnotInputForm());

        //changing already Input
        for (KnotInputForm knotInputForm : knotInputFormList) {
            if (knotInputFormToSave.getOperationNumber() == knotInputForm.getOperationNumber()) {
                knotInputForm.setOperationDescription(knotInputFormToSave.getOperationDescription());
                knotInputForm.setOperationNumber(knotInputFormToSave.getOperationNumber());
                knotInputForm.setDurationInMinutes(knotInputFormToSave.getDurationInMinutes());
                knotInputForm.setPredecessorOneListIndex(knotInputFormToSave.getPredecessorOneListIndex());
                knotInputForm.setPredecessorTwoListIndex(knotInputFormToSave.getPredecessorTwoListIndex());
                knotInputForm.setPredecessorThreeListIndex(knotInputFormToSave.getPredecessorThreeListIndex());
                return "knotInputForm";
            }
        }

        System.out.println(knotInputFormToSave);
        knotInputFormToSave.setOperationNumber(knotInputFormList.size()+1);
        knotInputFormList.add(knotInputFormToSave);
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        return "knotInputForm";
    }


    @GetMapping("netPlanTable")
    public String getNetPlanOutputTable(Model model) {
        if (knotInputFormList.size() == 0) {
            return "redirect:input";
        }
        knotList = convertKnotInputFormListToKnotList(knotInputFormList);
        String error = null;
        error = validateNotTwoEnd(knotList, error);
        System.out.println(error);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("operationNumber", knotInputFormList.size()+1);
            model.addAttribute("knotInputFormToSave", new KnotInputForm());
            model.addAttribute("knotInputFormList", knotInputFormList);
            knotList = new ArrayList<>();
            return "knotInputForm";
        }
        calculateNetPlanResults();

        List<List<Knot>> criticalPathsUnclean = new ArrayList<>(knotList.get(knotList.size() - 1).calculateCriticalPath(new ArrayList<>()));
        System.out.println("Unclean Pfad: " + criticalPathsUnclean);
        for(List<Knot> criticalPath : criticalPathsUnclean) {
            if (criticalPath.get(0).getSuccessor().size() == 0 && criticalPath.get(criticalPath.size()-1).getPredecessor().size() == 0) {
                Collections.reverse(criticalPath);
                criticalPathsClean.add(criticalPath);
            }
        }
        System.out.println("Pfade: " + criticalPathsClean);
        model.addAttribute("pathsList", criticalPathsClean);
        model.addAttribute("knotList", knotList);
        return "outputTable";
    }

    @GetMapping("graphicNetplan")
    public String getNetPlanOutputGraphic(Model model) {
        model.addAttribute("knotList", knotList);
        model.addAttribute("pathsList", criticalPathsClean);
        return "outputGraphic";
    }

    public List<Knot> convertKnotInputFormListToKnotList(List<KnotInputForm> knotInputFormList) {
        List<Knot> result = new ArrayList<>();
        for (KnotInputForm knotInputForm : knotInputFormList) {
            result.add(new Knot(knotInputForm.getOperationNumber(),knotInputForm.getOperationDescription(),knotInputForm.getDurationInMinutes()));
            if (knotInputForm.getPredecessorOneListIndex() != null) {
                result.get(result.size()-1).getPredecessor().add(result.get(knotInputForm.getPredecessorOneListIndex()));
                result.get(knotInputForm.getPredecessorOneListIndex()).getSuccessor().add(result.get(knotInputForm.getOperationNumber()-1));
            }
            if (knotInputForm.getPredecessorTwoListIndex() != null) {
                result.get(result.size()-1).getPredecessor().add(result.get(knotInputForm.getPredecessorTwoListIndex()));
                result.get(knotInputForm.getPredecessorTwoListIndex()).getSuccessor().add(result.get(knotInputForm.getOperationNumber()-1));
            }
            if (knotInputForm.getPredecessorThreeListIndex() != null) {
                result.get(result.size()-1).getPredecessor().add(result.get(knotInputForm.getPredecessorThreeListIndex()));
                result.get(knotInputForm.getPredecessorThreeListIndex()).getSuccessor().add(result.get(knotInputForm.getOperationNumber()-1));
            }
        }
        return result;
    }

    private void calculateNetPlanResults() {
        for (Knot knot : knotList) {
            if (knot.getPredecessor().size() == 0) {
                knot.calculateEarliestTime(0);
            }
        }
        for (Knot knot : knotList) {
            if (knot.getSuccessor().size() == 0) {
                knot.calculateLatestTime(knot.getEarliestEnd());
                knot.calculateBuffer();
            }
        }
    }

    private String validateNotTwoEnd(List<Knot> knotList, String error) {
        int counter = 0;
        for(Knot knot : knotList) {
            System.out.println(knot);
            if (knot.getSuccessor().size() < 1) {
                counter++;
            }
        }
        if (counter > 1) {
            error = "Sie brauchen ein EndVorgang! Es darf nur einen Endvorgang geben";
        }
        return error;
    }

}
