package com.newenergytrading.netplan.repository;

import com.newenergytrading.netplan.forms.KnotInputForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnotInputFormRepository extends JpaRepository<KnotInputForm, Integer> {

    public List<KnotInputForm> findAllByProjectId(int project_id);

    public List<KnotInputForm> deleteAllByProjectId(int project_id);


}
