package com.newenergytrading.netplan.forms;

import com.newenergytrading.netplan.domain.Project;
import com.newenergytrading.netplan.domain.TimeUnits;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "knot_input_form")
public class KnotInputForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int operationNumber;

    @NotBlank(message = "Bitte fülle die Vorgangsbeschreibung aus!")
    private String operationDescription;

    @NotNull(message= "Dauer ausfüllen bitte")
    private Integer durationInMinutes;

    private TimeUnits timeUnits = TimeUnits.MINUTE;

    private Integer predecessorOneListIndex;

    private Integer predecessorTwoListIndex;

    private Integer predecessorThreeListIndex;

    @ManyToOne (cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id")
    private Project project;

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

    public KnotInputForm(int operationNumber, String operationDescription, int durationInMinutes, TimeUnits timeUnits, Integer predecessorOneListIndex, Integer predecessorTwoListIndex, Integer predecessorThreeListIndex) {
        this.operationNumber = operationNumber;
        this.operationDescription = operationDescription;
        this.durationInMinutes = durationInMinutes;
        this.timeUnits = timeUnits;
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
        switch (timeUnits) {
            case HOUR:
                return durationInMinutes * 60;
            case DAY:
                return durationInMinutes * 1440;
            case WEEK:
                return durationInMinutes * 10080;
            default:
                return durationInMinutes;
        }
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



    public TimeUnits getTimeUnits() {
        return timeUnits;
    }

    public void setTimeUnits(TimeUnits timeUnits) {
        this.timeUnits = timeUnits;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
