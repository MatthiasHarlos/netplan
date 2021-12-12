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

    private List<Knot> resultList(){
        List<Knot> resultList = new ArrayList<>();
        resultList.add(new Knot(1,"Entscheidung GE",2,null,null,0,2));
        resultList.add(new Knot(2,"Angebote einholen",14,null,List.of(resultList.get(0)),2,16));
        resultList.add(new Knot(3,"Mitarbeiterinformationen",1,null,List.of(resultList.get(0)),2,3));
        resultList.add(new Knot(4,"Testen G1",1,null,List.of(resultList.get(1)),16,17));
        resultList.add(new Knot(5,"Testen G2",2,null,List.of(resultList.get(1)),16,18));
        resultList.add(new Knot(6,"Testen G3",1,null,List.of(resultList.get(1)),16,17));
        resultList.add(new Knot(7,"Auswahl Lieferanten",1,null,List.of(resultList.get(3), resultList.get(4), resultList.get(5)),18,19));
        resultList.add(new Knot(8,"Lieferung",5,null,List.of(resultList.get(6)),19,24));
        resultList.add(new Knot(9,"Raumauswahl",2,null,List.of(resultList.get(6)), 19,21));
        resultList.add(new Knot(10,"Elektroinstallation",2,null,List.of(resultList.get(8)),21,23));
        resultList.add(new Knot(11,"Computeraufstellung",1,null,List.of(resultList.get(7),resultList.get(9)),24,25));
        resultList.add(new Knot(12,"Mitarbeiterschulung",7,null,List.of(resultList.get(2), resultList.get(10)),25,32));
        resultList.add(new Knot(13,"Arbeitsaufnahme",1,null,List.of(resultList.get(11)),32,33));

        resultList.get(0).setSuccessor(List.of(resultList.get(1),resultList.get(2)));
        resultList.get(1).setSuccessor(List.of(resultList.get(3),resultList.get(4), resultList.get(5)));
        resultList.get(2).setSuccessor(List.of(resultList.get(11)));
        resultList.get(3).setSuccessor(List.of(resultList.get(6)));
        resultList.get(4).setSuccessor(List.of(resultList.get(6)));
        resultList.get(5).setSuccessor(List.of(resultList.get(6)));
        resultList.get(6).setSuccessor(List.of(resultList.get(7), resultList.get(8)));
        resultList.get(7).setSuccessor(List.of(resultList.get(10)));
        resultList.get(8).setSuccessor(List.of(resultList.get(9)));
        resultList.get(9).setSuccessor(List.of(resultList.get(10)));
        resultList.get(10).setSuccessor(List.of(resultList.get(11)));
        resultList.get(11).setSuccessor(List.of(resultList.get(12)));
        return resultList;
    }

    @Test
    void Test1 () {
        List<Knot> knotList = knotList();
        List<Knot> resultList = resultList();

        for (Knot knot : knotList) {
            // Funktioniet nur mit null. Mit size funktioniert der test nicht.
            // Knot methoden Abfrage war zuerst mit Null. Jetzt mit size(), da das Programm nicht mit null arbeitet,
            // Der Test aber schon.
            // Alle Tests funktionieren nur wenn im Knot size() mit null abfrage ersetzt wird.
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
