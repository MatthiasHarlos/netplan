package com.newenergytrading.netplan.controller;

import com.newenergytrading.netplan.domain.Knot;
import com.newenergytrading.netplan.forms.KnotInputForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

        return "knotInputForm";
    }

    @PostMapping("saveInput")
    public String saveKnotFormInput(@Valid KnotInputForm knotInputForm, BindingResult bindingResult, Model model) {

        return "knotInputForm";
    }

    @GetMapping("netPlanTable")
    public String getNetPlanOutputTable(Model model) {
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

    private List<Knot> calculateNetPlanResults(List<Knot> KnotList) {

        return null;
    }

    private void validate(KnotInputForm knotInputForm, BindingResult bindingResult) {

    }

    private void validateNotTwoEnd(List<Knot> knotList, BindingResult bindingResult) {
        int counter = 0;
        for(Knot knot : knotList) {
            if (knot.getSuccessor() == null) {
                counter++;
            }
        }
        if (counter > 1) {
            //TODO: error
        }

    }

}
