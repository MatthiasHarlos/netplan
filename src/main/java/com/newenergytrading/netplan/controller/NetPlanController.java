package com.newenergytrading.netplan.controller;

import com.newenergytrading.netplan.domain.*;
import com.newenergytrading.netplan.forms.KnotInputForm;
import com.newenergytrading.netplan.repository.AuthoritiesRepository;
import com.newenergytrading.netplan.repository.KnotInputFormRepository;
import com.newenergytrading.netplan.repository.ProjectRepository;
import com.newenergytrading.netplan.repository.UserRepository;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.SortComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.servlet.session.SessionSecurityMarker;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Controller
public class NetPlanController {

    public static List<KnotInputForm> knotInputFormList = new ArrayList<>();

    private static List<Knot> knotList = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthoritiesRepository authRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    IUserService userService;

    @Autowired
    UserRepository urepo;

    @Autowired
    HttpSession session;

    @Autowired
    ProjectRepository prorepo;

    @Autowired
    KnotInputFormRepository knorepo;

    @RequestMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("users", new Users());
        return "signup";
    }

    @GetMapping("/test")
    public String testeRegistration() {
        Users users = new Users();
        users.setUsername("Peter");
        users.setPassword(passwordEncoder.encode("pass"));
        users.setEnabled(true);
        urepo.save(users);
        Authorities auth = new Authorities(users.getUsername(), "ROLE_USER");
        authRepo.save(auth);
        return "redirect:/login";
    }

    @PostMapping("/signup")
    public String addUser(Users users)
    {
        System.out.println(users.getUsername());

        //ModelAndView mv=new ModelAndView("success");
        List<Users> list=urepo.findByUsername(users.getUsername());


        if(list.size()!=0)
        {
            //TODO: ERROR OUTPUT
            return "signup";
        }
        else
        {
            String password = users.getPassword();
            System.out.println(password);
            users.setPassword(passwordEncoder.encode(password));
            System.out.println(users.getPassword());
            users.setEnabled(true);
            urepo.saveAndFlush(users);
            Authorities auth = new Authorities(users.getUsername(), "ROLE_USER");
            authRepo.save(auth);
        }

        return "redirect:/login";
    }

    @RequestMapping("/logout")
    public String getLogout()
    {
        knotInputFormList = new ArrayList<>();
        return "login";
    }


    @GetMapping("projects")
    public String getProjectPage(Model model) {
        model.addAttribute("project", new Project());
        List<Users> user = urepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("projects", prorepo.findByUsername(user.get(0)));
        return "projects";
    }

    @PostMapping("loadProject")
    public String getProject(Project project) {
       knotInputFormList = knorepo.findAllByProjectId(project.getId());
        return "redirect:/";
    }

    @PostMapping("deleteProject")
    public String deleteProject(Project project) {
       // knorepo.deleteAllByProjectId(project.getId());
        List<Users> auser = urepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        prorepo.deleteByProjectnameAndUsername(project.getProjectname(), auser.get(0));
        return "redirect:projects";
    }

    @GetMapping("saveProject")
    public String saveProject() {
        List<Users> auser = urepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
              Project project = new Project("test", auser.get(0));
                project.setKnotinputform(knotInputFormList);
                knorepo.saveAll(knotInputFormList);
                auser.get(0).getProject().add(project);
                for (KnotInputForm knot : knotInputFormList) {
                    knot.setProject(project);
                }
                //project.setId(countProjectId);
                //countProjectId++;
                prorepo.save(project);
        return "redirect:projects";
    }


    @GetMapping("/")
    public String getStartPage(){
      /*  String filterName = "Julius";
        String querryTemplate = "SELECT * FROM test WHERE name = '%s';";
        String querry = String.format(querryTemplate, filterName);
        System.out.println(querry);
        jdbcTemplate.execute(querry);


        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM test");
        while (sqlRowSet.next()) {
            System.out.println(sqlRowSet.getInt("ID") + ": " + sqlRowSet.getString("Name"));
        }

      //  sqlRowSet.getMetaData().get
*/
        return "startpage";
    }

    @GetMapping("input")
    public String getKnotFormInput(Model model) {
        model.addAttribute("knotInputFormToSave", new KnotInputForm());
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "eingeben");
        return "knotInputForm";
    }

    @PostMapping("changeInput")
    public String changeSavedKnotInputForm(Model model, KnotInputForm changeKnotInputForm) {
        model.addAttribute("knotInputFormToSave", knotInputFormList.get(changeKnotInputForm.getOperationNumber()));
        model.addAttribute("operationNumber", changeKnotInputForm.getOperationNumber()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "ändern <a href='http://localhost:8080/input'>|abbrechen|</a>");
        return "knotInputForm";
    }

    @GetMapping("deletedOld")
    public String deleteOldNetPlan () {
        knotInputFormList = new ArrayList<>();
        knotList = new ArrayList<>();
        return "redirect:input";
    }


    @PostMapping("saveInput")
    public String saveKnotFormInput(@Valid @ModelAttribute("knotInputFormToSave") KnotInputForm knotInputFormToSave, BindingResult bindingResult, Model model) {
        knotList = new ArrayList<>();
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        model.addAttribute("knotInputFormList", knotInputFormList);
        model.addAttribute("operation", "eingeben");
        //validate(knotInputFormToSave, bindingResult);
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("error");
            model.addAttribute("knotInputFormToSave", knotInputFormToSave);
            return "knotInputForm";
        }

        model.addAttribute("knotInputFormToSave", new KnotInputForm());

        //changing already Input
        for (KnotInputForm knotInputForm : knotInputFormList) {
            if (knotInputFormToSave.getOperationNumber() == knotInputForm.getOperationNumber()) {
                knotInputForm.setOperationDescription(knotInputFormToSave.getOperationDescription());
                knotInputForm.setOperationNumber(knotInputFormToSave.getOperationNumber());
                knotInputForm.setDurationInMinutes(knotInputFormToSave.getDurationInMinutes());
                knotInputForm.setPredecessorOneListIndex(knotInputFormToSave.getPredecessorOneListIndex());
                knotInputForm.setPredecessorTwoListIndex(knotInputFormToSave.getPredecessorTwoListIndex());
                knotInputForm.setPredecessorThreeListIndex(knotInputFormToSave.getPredecessorThreeListIndex());
                return "knotInputForm";
            }
        }

        System.out.println(knotInputFormToSave);
        knotInputFormToSave.setOperationNumber(knotInputFormList.size()+1);
        knotInputFormList.add(knotInputFormToSave);
        model.addAttribute("operationNumber", knotInputFormList.size()+1);
        return "knotInputForm";
    }


    @GetMapping("netPlanTable")
    public String getNetPlanOutputTable(Model model) {
        if (knotInputFormList.size() == 0) {
            return "redirect:input";
        }
        knotList = convertKnotInputFormListToKnotList(knotInputFormList);
        String error = null;
        error = validateNotTwoEnd(knotList, error);
        System.out.println(error);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("operationNumber", knotInputFormList.size()+1);
            model.addAttribute("knotInputFormToSave", new KnotInputForm());
            model.addAttribute("knotInputFormList", knotInputFormList);
            knotList = new ArrayList<>();
            return "knotInputForm";
        }
        calculateNetPlanResults();

        List<List<Knot>> criticalPathsClean = calculateCriticalPathClean();

        System.out.println("Pfade: " + criticalPathsClean);
        model.addAttribute("pathsList", criticalPathsClean);
        model.addAttribute("knotList", knotList);
        return "outputTable";
    }

    private List<List<Knot>> calculateCriticalPathClean() {
        List<List<Knot>> criticalPathsUnclean = new ArrayList<>(knotList.get(knotList.size() - 1).calculateCriticalPath(new ArrayList<>()));
        List<List<Knot>> criticalPathsClean = new ArrayList<>();
        System.out.println("Unclean Pfad: " + criticalPathsUnclean);
        for(List<Knot> criticalPath : criticalPathsUnclean) {
            if (criticalPath.get(0).getSuccessor().size() == 0 && criticalPath.get(criticalPath.size()-1).getPredecessor().size() == 0) {
                Collections.reverse(criticalPath);
                criticalPathsClean.add(criticalPath);
            }
        }
        return criticalPathsClean;
    }

    @GetMapping("graphicNetplan")
    public String getNetPlanOutputGraphic(Model model) {
        knotList = convertKnotInputFormListToKnotList(knotInputFormList);
        String error = null;
        error = validateNotTwoEnd(knotList, error);
        System.out.println(error);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("operationNumber", knotInputFormList.size()+1);
            model.addAttribute("knotInputFormToSave", new KnotInputForm());
            model.addAttribute("knotInputFormList", knotInputFormList);
            knotList = new ArrayList<>();
            return "knotInputForm";
        }
        calculateNetPlanResults();

        List<List<Knot>> criticalPathsClean = calculateCriticalPathClean();
        model.addAttribute("knotList", knotList);
        model.addAttribute("pathsList", criticalPathsClean);

        String javaScriptKnotConnection = "function connectAll() {\n";
        int counter = 0;
        List<Integer> countedPaths = new ArrayList<>();
        String test = "";

        int countingStartKnots = 0;
        for(Knot knot : knotList) {
            if (knot.getPredecessor().size() == 0) {
                countingStartKnots++;
            }
        }

        for(Knot knot : knotList) {
            if (knot.getSuccessor().size() > 0) {
                for (Knot success : knot.getSuccessor()) {
                    counter++;
                    countedPaths.add(counter);
                    javaScriptKnotConnection += "    connectElements($(\"#svg1\"), $(\"#path" + counter + "\"), $(\"#vorgang" + knot.getOperationNumber() + "\"),   $(\"#vorgang" + success.getOperationNumber() + "\"));\n";
                }
            }
        }
        javaScriptKnotConnection += "}";

        System.out.println(javaScriptKnotConnection);
        System.out.println(test);
        System.out.println(countedPaths);

        Knot end = new Knot(0, "end", 0);
        end.getPredecessor().add(knotList.get(knotList.size()-1));
        knotList.get(knotList.size()-1).getSuccessor().add(end);
        knotList.add(end);


        List<List<Knot>> rowList = getRows();

        model.addAttribute("rowList", rowList);
        model.addAttribute("countedPaths", countedPaths);
        //model.addAttribute("cssConnectionStyle", test);
        model.addAttribute("javaScriptKnots", javaScriptKnotConnection);

        return "outputGraphic";
    }

    public List<List<Knot>> getRows() {
        List<List<Knot>> allPaths = getPaths();

        List<Knot> longestPath = allPaths.get(0);
        for (List<Knot> path : allPaths) {
            if (longestPath.size() < path.size()) {
                longestPath = path;
            }
        }

        List<List<Knot>> rowList = getRowList(longestPath, allPaths);

        // remove duplicates from Knot in rows Lists
        int counting = rowList.size()-1;
        while (counting != 0) {
            List<Knot> result = rowList.get(counting);
            for (List<Knot> initialRow : rowList) {
                if (rowList.indexOf(result) != rowList.indexOf(initialRow)) {
                    initialRow.removeAll(new HashSet<>(result));
                }
            }
            counting--;
        }

        for (List<Knot> rowing : rowList) {
            System.out.println("Rows" + rowing);
        }
        addRowListIndexToKnots(rowList);
        addRowListLowestSuccessorIndex();
        addRowListHighestPredecessorIndex();
        return rowList;
    }

    private void addRowListHighestPredecessorIndex() {
        for (Knot knot : knotList) {
            if (knot.getPredecessor().size() != 0) {
                int result = knot.getPredecessor().get(0).getRowListIndexSelf();
                for (Knot predecessor : knot.getPredecessor()) {
                    if (result > predecessor.getRowListIndexSelf()) {
                        result = predecessor.getRowListIndexSelf();
                    }
                }
                knot.setRowListHighestPredecessorIndex(result);
                System.out.println("PredecessorROWIndex: " + knot.getRowListHighestPredecessorIndex() + "Knoten: " + knot.getOperationNumber());

            }
            System.out.println("SelfIndex: " + knot.getRowListIndexSelf() + "Knoten: " + knot.getOperationNumber());
        }
    }


    private void addRowListLowestSuccessorIndex() {
        for (Knot knot : knotList) {
            if (knot.getSuccessor().size() != 0) {
                int result = knot.getSuccessor().get(0).getRowListIndexSelf();
                for (Knot successor : knot.getSuccessor()) {
                    if (result < successor.getRowListIndexSelf()) {
                        result = successor.getRowListIndexSelf();
                    }
                }
                knot.setRowListLowestSuccessorIndex(result);
            }
        }
    }

    private void addRowListIndexToKnots(List<List<Knot>> rowList) {
        for (List<Knot> row : rowList) {
            int rowIndex = rowList.indexOf(row);
            for (Knot knot : row) {
                knot.setRowListIndexSelf(rowIndex);
            }
        }
    }

    public List<List<Knot>> getPaths () {
        List<List<Knot>> allPaths = knotList.get(knotList.size()-1).getAllPaths(new ArrayList<>());
        List<List<Knot>> cleanedPaths = new ArrayList<>();
        for (List<Knot> path : allPaths) {
            Collections.reverse(path);
            if(path.get(0).getPredecessor().size() == 0 && path.get(path.size()-1).getSuccessor().size() == 0) {
                cleanedPaths.add(path);
            }
        }
        return cleanedPaths;
    }

    public List<List<Knot>> getRowList(List<Knot> longestPath, List<List<Knot>> allPaths) {
        List<Knot> row = new ArrayList<>();
        List<List<Knot>> rowList = new ArrayList<>();
        int counter = 0;
        while (counter != longestPath.size()-1) {

            for (int i = 0; i < allPaths.size(); i++) {
                if (allPaths.get(i).size() > counter) {
                    row.add(allPaths.get(i).get(counter));
                }
            }
            HashSet<Knot> witUnigeValue = new HashSet<>(row);
            row.clear();
            row.addAll(witUnigeValue);
            rowList.add(row);
            row = new ArrayList<>();

            counter++;
        }
        return rowList;
    }

    public List<Knot> convertKnotInputFormListToKnotList(List<KnotInputForm> knotInputFormList) {
        List<Knot> result = new ArrayList<>();
        for (KnotInputForm knotInputForm : knotInputFormList) {
            result.add(new Knot(knotInputForm.getOperationNumber(),knotInputForm.getOperationDescription(),knotInputForm.getDurationInMinutes(), knotInputForm.getTimeUnits()));
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

    private void calculateNetPlanResults() {
        for (Knot knot : knotList) {
            if (knot.getPredecessor().size() == 0) {
                knot.calculateEarliestTime(0);
            }
        }
        for (Knot knot : knotList) {
            if (knot.getSuccessor().size() == 0) {
                knot.calculateLatestTime(knot.getEarliestEnd());
                knot.calculateBuffer();
            }
        }
    }

    private String validateNotTwoEnd(List<Knot> knotList, String error) {
        int counter = 0;
        for(Knot knot : knotList) {
            System.out.println(knot);
            if (knot.getSuccessor().size() < 1) {
                counter++;
            }
        }
        if (counter > 1) {
            error = "Sie brauchen ein EndVorgang! Es darf nur einen Endvorgang geben";
        }
        return error;
    }

}
