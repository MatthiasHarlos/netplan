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
import java.util.List;

@Controller
public class NetPlanController {

    private static List<KnotInputForm> knotInputFormList = new ArrayList<>();
    private static List<Knot> knotList = new ArrayList<>();

    @GetMapping("/")
    public String getStartPage(){
        return "startpage";
    }

    @GetMapping("input")
    public String getKnotFormInput(Model model) {
        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        return "knotInputForm";
    }

    @PostMapping("saveInput")
    public String saveKnotFormInput(@Valid @ModelAttribute("knotInputFormToSave") KnotInputForm knotInputFormToSave, BindingResult bindingResult, Model model) {
        knotList = new ArrayList<>();
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        //validate(knotInputFormToSave, bindingResult);
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("error");
            return "knotInputForm";
        }
        model.addAttribute("knotInputFormToSave", new KnotInputForm());

        System.out.println(knotInputFormToSave);
        knotInputFormToSave.setOperationNumber(knotInputFormList.size()+1);
        knotInputFormList.add(knotInputFormToSave);
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        return "knotInputForm";
    }

    @GetMapping("netPlanTable")
    public String getNetPlanOutputTable(Model model) {
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
        List<List<Knot>> criticalPathsClean = new ArrayList<>();
        for(List<Knot> criticalPath : criticalPathsUnclean) {
            if (criticalPath.get(0).getSuccessor() == null && criticalPath.get(criticalPath.size()-1).getPredecessor() == null) {
                criticalPathsClean.add(criticalPath);
            }
        }
        model.addAttribute("knotList", knotList);
        return "outputTable";
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
