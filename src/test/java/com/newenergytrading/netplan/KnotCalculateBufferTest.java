package com.newenergytrading.netplan;

import com.newenergytrading.netplan.domain.Knot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class KnotCalculateBufferTest {

    private List<Knot> resultList(){
        List<Knot> resultList = new ArrayList<>();
        resultList.add(new Knot(1,"Entscheidung GE",2,null,null,0,2,0,2));
        resultList.add(new Knot(2,"Angebote einholen",14,null,List.of(resultList.get(0)),2,16,2,16));
        resultList.add(new Knot(3,"Mitarbeiterinformationen",1,null,List.of(resultList.get(0)),2,3,24,25));
        resultList.add(new Knot(4,"Testen G1",1,null,List.of(resultList.get(1)),16,17,17,18));
        resultList.add(new Knot(5,"Testen G2",2,null,List.of(resultList.get(1)),16,18,16,18));
        resultList.add(new Knot(6,"Testen G3",1,null,List.of(resultList.get(1)),16,17,17,18));
        resultList.add(new Knot(7,"Auswahl Lieferanten",1,null,List.of(resultList.get(3), resultList.get(4), resultList.get(5)),18,19,18,19));
        resultList.add(new Knot(8,"Lieferung",5,null,List.of(resultList.get(6)),19,24,19,24));
        resultList.add(new Knot(9,"Raumauswahl",2,null,List.of(resultList.get(6)), 19,21,20,22));
        resultList.add(new Knot(10,"Elektroinstallation",2,null,List.of(resultList.get(8)),21,23,22,24));
        resultList.add(new Knot(11,"Computeraufstellung",1,null,List.of(resultList.get(7),resultList.get(9)),24,25,24,25));
        resultList.add(new Knot(12,"Mitarbeiterschulung",7,null,List.of(resultList.get(2), resultList.get(10)),25,32,25,32));
        resultList.add(new Knot(13,"Arbeitsaufnahme",1,null,List.of(resultList.get(11)),32,33,32,33));

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
    void test1() {
        List<Knot> knotList = resultList();

        for (Knot knot : knotList) {
            if (knot.getSuccessor() == null) {
                knot.calculateBuffer();
            }
        }


        Assertions.assertEquals(0, knotList.get(0).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(0).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(1).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(1).getFreeBuffer());
        Assertions.assertEquals(22, knotList.get(2).getTotalBuffer());
        Assertions.assertEquals(22, knotList.get(2).getFreeBuffer());
        Assertions.assertEquals(1, knotList.get(3).getTotalBuffer());
        Assertions.assertEquals(1, knotList.get(3).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(4).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(4).getFreeBuffer());
        Assertions.assertEquals(1, knotList.get(5).getTotalBuffer());
        Assertions.assertEquals(1, knotList.get(5).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(6).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(6).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(7).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(7).getFreeBuffer());
        Assertions.assertEquals(1, knotList.get(8).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(8).getFreeBuffer());
        Assertions.assertEquals(1, knotList.get(9).getTotalBuffer());
        Assertions.assertEquals(1, knotList.get(9).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(10).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(10).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(11).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(11).getFreeBuffer());
        Assertions.assertEquals(0, knotList.get(12).getTotalBuffer());
        Assertions.assertEquals(0, knotList.get(12).getFreeBuffer());
    }

}
