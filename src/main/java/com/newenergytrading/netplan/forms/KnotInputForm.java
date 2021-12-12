package com.newenergytrading.netplan.forms;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class KnotInputForm {

    private int operationNumber;

    @NotBlank(message = "Bitte fülle die Vorgangsbeschreibung aus!")
    private String operationDescription;

    @NotNull(message= "Dauer ausfüllen bitte")
    private Integer durationInMinutes;
    private Integer predecessorOneListIndex;
    private Integer predecessorTwoListIndex;
    private Integer predecessorThreeListIndex;

    public KnotInputForm() {
    }

    public KnotInputForm(int operationNumber, String operationDescription, int durationInMinutes, Integer predecessorOneListIndex, Integer predecessorTwoListIndex, Integer predecessorThreeListIndex) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.predecessorOneListIndex = predecessorOneListIndex;
        this.predecessorTwoListIndex = predecessorTwoListIndex;
        this.predecessorThreeListIndex = predecessorThreeListIndex;
    }

    public int getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Integer getPredecessorOneListIndex() {
        return predecessorOneListIndex;
    }

    public void setPredecessorOneListIndex(Integer predecessorOneListIndex) {
        this.predecessorOneListIndex = predecessorOneListIndex;
    }

    public Integer getPredecessorTwoListIndex() {
        return predecessorTwoListIndex;
    }

    public void setPredecessorTwoListIndex(Integer predecessorTwoListIndex) {
        this.predecessorTwoListIndex = predecessorTwoListIndex;
    }

    public Integer getPredecessorThreeListIndex() {
        return predecessorThreeListIndex;
    }

    public void setPredecessorThreeListIndex(Integer predecessorThreeListIndex) {
        this.predecessorThreeListIndex = predecessorThreeListIndex;
    }

    @Override
    public String toString() {
        return "KnotInputForm{" +
                "operationNumber=" + operationNumber +
                ", operationDescription='" + operationDescription + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", predecessorOneListIndex=" + predecessorOneListIndex +
                ", predecessorTwoListIndex=" + predecessorTwoListIndex +
                ", predecessorThreeListIndex=" + predecessorThreeListIndex +
                '}';
    }
}
