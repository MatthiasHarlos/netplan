package com.newenergytrading.netplan.domain;

import java.util.ArrayList;
import java.util.List;

public class Knot {

    private int operationNumber;
    private String operationDescription;
    private int durationInMinutes;
    private int earliestStart;
    private int earliestEnd;
    private int latestStart;
    private int latestEnd;
    private int totalBuffer;
    private int freeBuffer;
    private List<Knot> successor = new ArrayList<>();
    private List<Knot> predecessor = new ArrayList<>();

    private static int counter = 1;


    public String getCssConnectionStyle(int successorSize) {
        String test = "";
        if (this.predecessor.size() == 0 || this.successor.size() > 1) {
            test += "#vorgang" + this.getOperationNumber() + "{\n" +
                    "margin-top: 10%;\n" +
                    " \twidth: 20%;\n" +
                    " \theight: 5em;\n" +
                    " \tmargin-left:40%;\n" +
                    " \tmargin-right: 100%;\n" +
                    "}\n";
        } else if (successorSize <= 2) {
            test += "#vorgang" + this.getOperationNumber() + "{\n" +
                    "margin-top: 10%;\n" +
                    " \twidth: 20%;\n" +
                    " \theight: 5em;\n" +
                    " \tmargin-left:" + 40/successorSize + "%;\n" +
                    " \tmargin-right: 20px;\n" +
                    "}\n";
        } else if (successorSize > 2) {
            test += "#vorgang" + this.getOperationNumber() + "{\n" +
                    "margin-top: 10%;\n" +
                    " \twidth: " + 50/successorSize + "%;\n" +
                    " \theight: 5em;\n" +
                    " \tmargin-left:" + 40/successorSize + "%;\n" +
                    " \tmargin-right: 20px;\n" +
                    " font-size: 10px;\n" +
                    "}\n";
        }
        if (this.getSuccessor().size() > 0) {
            for (Knot success : this.successor) {
                test += success.getCssConnectionStyle(this.successor.size());
            }
        }
        return test;
    }

    public List<List<Knot>> calculateCriticalPath(List<Knot> criticalPath) {
        List<List<Knot>> criticalPaths = new ArrayList<>();
        List<Knot> anotherPath;
        if (this.getTotalBuffer() == 0 && this.getFreeBuffer() == 0) {
            criticalPath.add(this);
            if (this.predecessor != null && this.predecessor.size() > 0) {
                if (this.predecessor.size() == 1) {
                    return predecessor.get(0).calculateCriticalPath(criticalPath);
                } else {
                    for (Knot predecessor : this.predecessor) {
                        if (predecessor.getTotalBuffer() == 0 && predecessor.getFreeBuffer() == 0) {
                            anotherPath = new ArrayList<>(criticalPath);
                            criticalPaths.addAll(predecessor.calculateCriticalPath(anotherPath));
                        }
                    }
                }
            }
        }
        criticalPaths.add(criticalPath);
        return criticalPaths;
    }

    public void calculateEarliestTime(int earliestTime) {
        if (this.predecessor != null && this.predecessor.size() > 0) {
            Knot result = this.predecessor.get(0);
            for (Knot predecessor : this.predecessor) {
                if(result.getEarliestEnd() < predecessor.getEarliestEnd()){
                    result = predecessor;
                }
            }
            this.setEarliestStart(result.getEarliestEnd());
            this.setEarliestEnd(this.earliestStart + this.durationInMinutes);
        } else {
            this.setEarliestEnd(this.durationInMinutes);
        }
        if (this.successor != null && this.successor.size() > 0) {
            for (Knot successor : this.successor) {
                successor.calculateEarliestTime(this.getEarliestEnd());
            }
        }
    }

    public void calculateLatestTime(int latestTime) {
        if (this.successor != null && this.successor.size() > 0) {
            Knot result = this.successor.get(0);
            for (Knot successor : this.successor) {
                if(result.getLatestStart() > successor.getLatestStart()){
                    result = successor;
                }
            }
            this.setLatestEnd(result.getLatestStart());
            this.setLatestStart(this.latestEnd - this.durationInMinutes);
        } else {
            this.setLatestEnd(this.getEarliestEnd());
            this.setLatestStart(this.getLatestEnd() - this.durationInMinutes);
        }
        if (this.predecessor != null && this.predecessor.size() > 0) {
            for (Knot predecessor : this.predecessor) {
                predecessor.calculateLatestTime(this.getLatestStart());
            }
        }
    }

    public void calculateBuffer() {
        if (this.successor != null && this.successor.size() > 0) {
            this.totalBuffer = this.latestStart - this.earliestStart;
            this.freeBuffer = this.getSuccessor().get(0).getEarliestStart() - this.earliestEnd;
        }
        if (this.predecessor != null && this.predecessor.size() > 0) {
            for (Knot predecessor : this.predecessor) {
                predecessor.calculateBuffer();
            }
        }
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, List<Knot> successor, List<Knot> predecessor) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.successor = successor;
        this.predecessor = predecessor;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, List<Knot> successor, List<Knot> predecessor, int earliestStart, int earliestEnd) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.successor = successor;
        this.predecessor = predecessor;
        this.earliestStart = earliestStart;
        this.earliestEnd = earliestEnd;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, List<Knot> successor, List<Knot> predecessor, int earliestStart, int earliestEnd, int latestStart, int latestEnd) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.successor = successor;
        this.predecessor = predecessor;
        this.earliestStart = earliestStart;
        this.earliestEnd = earliestEnd;
        this.latestStart = latestStart;
        this.latestEnd = latestEnd;
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

    public int getEarliestStart() {
        return earliestStart;
    }

    public void setEarliestStart(int earliestStart) {
        this.earliestStart = earliestStart;
    }

    public int getEarliestEnd() {
        return earliestEnd;
    }

    public void setEarliestEnd(int earliestEnd) {
        this.earliestEnd = earliestEnd;
    }

    public int getLatestStart() {
        return latestStart;
    }

    public void setLatestStart(int latestStart) {
        this.latestStart = latestStart;
    }

    public int getLatestEnd() {
        return latestEnd;
    }

    public void setLatestEnd(int latestEnd) {
        this.latestEnd = latestEnd;
    }

    public int getTotalBuffer() {
        return totalBuffer;
    }

    public void setTotalBuffer(int totalBuffer) {
        this.totalBuffer = totalBuffer;
    }

    public int getFreeBuffer() {
        return freeBuffer;
    }

    public void setFreeBuffer(int freeBuffer) {
        this.freeBuffer = freeBuffer;
    }

    public List<Knot> getSuccessor() {
        return successor;
    }

    public List<Knot> getPredecessor() {
        return predecessor;
    }

    public void setSuccessor(List<Knot> successor) {
        this.successor = successor;
    }

    public void setPredecessor(List<Knot> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return "Knot{" +
                "operationNumber=" + operationNumber +
                ", operationDescription='" + operationDescription + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", earliestStart=" + earliestStart +
                ", earliestEnd=" + earliestEnd +
                ", latestStart=" + latestStart +
                ", latestEnd=" + latestEnd +
                ", totalBuffer=" + totalBuffer +
                ", freeBuffer=" + freeBuffer +
                '}';
    }
}
