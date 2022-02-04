package com.newenergytrading.netplan;

import com.newenergytrading.netplan.controller.NetPlanController;
import com.newenergytrading.netplan.domain.Knot;
import com.newenergytrading.netplan.forms.KnotInputForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class NetplanApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetplanApplication.class, args);
		/*List<KnotInputForm> knotInputFormList = new ArrayList<>();

		knotInputFormList.add(new KnotInputForm(1,"Entscheidung GE",2,null,null,null));
		knotInputFormList.add(new KnotInputForm(2,"Angebote einholen",14,0,null,null));
		knotInputFormList.add(new KnotInputForm(3,"Mitarbeiterinformationen",1,0,null,null));
		knotInputFormList.add(new KnotInputForm(4,"Testen G1",1,1,null,null));
		knotInputFormList.add(new KnotInputForm(5,"Testen G2",2,1,null,null));
		knotInputFormList.add(new KnotInputForm(6,"Testen G3",1,1,null,null));
		knotInputFormList.add(new KnotInputForm(7,"Auswahl Lieferanten",1,3,4,5));
		knotInputFormList.add(new KnotInputForm(8,"Lieferung",5,6,null,null));
		knotInputFormList.add(new KnotInputForm(9,"Raumauswahl",2,6,null,null));
		knotInputFormList.add(new KnotInputForm(10,"Elektroinstallation",2,8,null,null));
		knotInputFormList.add(new KnotInputForm(11,"Computeraufstellung",1,7,9,null));
		knotInputFormList.add(new KnotInputForm(12,"Mitarbeiterschulung",7,2,10,null));
		knotInputFormList.add(new KnotInputForm(13,"Arbeitsaufnahme",1,11,null,null));


		NetPlanController controller = new NetPlanController();
		controller.knotInputFormList.addAll(knotInputFormList);*/



	}

}
