package com.newenergytrading.netplan.forms;

public class KnotInputForm {

    private int operationNumber;
    private String operationDescription;
    private int durationInMinutes;
    private Integer predecessorOneListIndex;
    private Integer predecessorTwoListIndex;
    private Integer predecessorThreeListIndex;

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

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
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
}
