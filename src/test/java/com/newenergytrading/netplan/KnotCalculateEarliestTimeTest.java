package com.newenergytrading.netplan;

import com.newenergytrading.netplan.domain.Knot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class KnotCalculateEarliestTimeTest {

    private List<Knot> knotList(){
        List<Knot> knotList = new ArrayList<>();
        knotList.add(new Knot(1,"Entscheidung GE",2,null,null));
        knotList.add(new Knot(2,"Angebote einholen",14,null,List.of(knotList.get(0))));
        knotList.add(new Knot(3,"Mitarbeiterinformationen",1,null,List.of(knotList.get(0))));
        knotList.add(new Knot(4,"Testen G1",1,null,List.of(knotList.get(1))));
        knotList.add(new Knot(5,"Testen G2",2,null,List.of(knotList.get(1))));
        knotList.add(new Knot(6,"Testen G3",1,null,List.of(knotList.get(1))));
        knotList.add(new Knot(7,"Auswahl Lieferanten",1,null,List.of(knotList.get(3), knotList.get(4), knotList.get(5))));
        knotList.add(new Knot(8,"Lieferung",5,null,List.of(knotList.get(6))));
        knotList.add(new Knot(9,"Raumauswahl",2,null,List.of(knotList.get(6))));
        knotList.add(new Knot(10,"Elektroinstallation",2,null,List.of(knotList.get(8))));
        knotList.add(new Knot(11,"Computeraufstellung",1,null,List.of(knotList.get(7), knotList.get(9))));
        knotList.add(new Knot(12,"Mitarbeiterschulung",7,null,List.of(knotList.get(2), knotList.get(10))));
        knotList.add(new Knot(13,"Arbeitsaufnahme",1,null,List.of(knotList.get(11))));

        knotList.get(0).setSuccessor(List.of(knotList.get(1),knotList.get(2)));
        knotList.get(1).setSuccessor(List.of(knotList.get(3),knotList.get(4), knotList.get(5)));
        knotList.get(2).setSuccessor(List.of(knotList.get(11)));
        knotList.get(3).setSuccessor(List.of(knotList.get(6)));
        knotList.get(4).setSuccessor(List.of(knotList.get(6)));
        knotList.get(5).setSuccessor(List.of(knotList.get(6)));
        knotList.get(6).setSuccessor(List.of(knotList.get(7), knotList.get(8)));
        knotList.get(7).setSuccessor(List.of(knotList.get(10)));
        knotList.get(8).setSuccessor(List.of(knotList.get(9)));
        knotList.get(9).setSuccessor(List.of(knotList.get(10)));
        knotList.get(10).setSuccessor(List.of(knotList.get(11)));
        knotList.get(11).setSuccessor(List.of(knotList.get(12)));
        return knotList;
    }

    @Test
    void Test1 () {
        List<Knot> knotList = knotList();

        for (Knot knot : knotList) {
            List<Knot> predecessor = knot.getPredecessor();
            if (predecessor == null) {
                knot.calculateEarliestTime(0);
            }
        }
        Assertions.assertEquals(16, knotList.get(1).getEarliestEnd());
        Assertions.assertEquals(17, knotList.get(5).getEarliestEnd());
        Assertions.assertEquals(19, knotList.get(6).getEarliestEnd());
        Assertions.assertEquals(23, knotList.get(9).getEarliestEnd());
        Assertions.assertEquals(25, knotList.get(10).getEarliestEnd());
        Assertions.assertEquals(3, knotList.get(2).getEarliestEnd());
        Assertions.assertEquals(32, knotList.get(11).getEarliestEnd());
        Assertions.assertEquals(33, knotList.get(12).getEarliestEnd());
    }
}
