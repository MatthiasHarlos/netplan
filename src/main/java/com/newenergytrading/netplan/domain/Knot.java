package com.newenergytrading.netplan.domain;

import java.util.ArrayList;
import java.util.List;

public class Knot {

    private int operationNumber;
    private String operationDescription;
    private int durationInMinutes;
    private TimeUnits timeUnits;
    private int earliestStart;
    private int earliestEnd;
    private int latestStart;
    private int latestEnd;
    private int totalBuffer;
    private int freeBuffer;
    private List<Knot> successor = new ArrayList<>();
    private List<Knot> predecessor = new ArrayList<>();
    private int rowListIndexSelf;
    private int rowListLowestSuccessorIndex;
    private int rowListHighestPredecessorIndex;


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

    public List<List<Knot>> getAllPaths(List<Knot> nextKnots) {
        List<List<Knot>> allPaths = new ArrayList<>();
        List<Knot> anotherPath;
            nextKnots.add(this);
            if (this.predecessor != null && this.predecessor.size() > 0) {
                    for (Knot predecessor : this.predecessor) {
                            anotherPath = new ArrayList<>(nextKnots);
                            allPaths.addAll(predecessor.getAllPaths(anotherPath));
                    }
                }
            allPaths.add(nextKnots);
        return allPaths;
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

    public int getDurationTimeUnits() {
        switch (this.getTimeUnits()) {
            case HOUR:
                return this.durationInMinutes / 60;
            case DAY:
                return this.durationInMinutes / 1440;
            case WEEK:
                return this.durationInMinutes / 10080;
            default:
                return this.durationInMinutes;
        }
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
    }

    public Knot(int operationNumber, String operationDescription, int durationInMinutes, TimeUnits timeUnits) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.timeUnits = timeUnits;
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

    public TimeUnits getTimeUnits() {
        return timeUnits;
    }

    public void setTimeUnits(TimeUnits timeUnits) {
        this.timeUnits = timeUnits;
    }

    public int getRowListIndexSelf() {
        return rowListIndexSelf;
    }

    public void setRowListIndexSelf(int rowListIndexSelf) {
        this.rowListIndexSelf = rowListIndexSelf;
    }

    public int getRowListLowestSuccessorIndex() {
        return rowListLowestSuccessorIndex;
    }

    public void setRowListLowestSuccessorIndex(int rowListLowestSuccessorIndex) {
        this.rowListLowestSuccessorIndex = rowListLowestSuccessorIndex;
    }

    public int getRowListHighestPredecessorIndex() {
        return rowListHighestPredecessorIndex;
    }

    public void setRowListHighestPredecessorIndex(int rowListHighestPredecessorIndex) {
        this.rowListHighestPredecessorIndex = rowListHighestPredecessorIndex;
    }

    @Override
    public String toString() {
        return "Knot{" +
                "operationNumber=" + operationNumber +
                '}';
    }
}
