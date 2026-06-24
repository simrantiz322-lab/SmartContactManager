package com.example.SmartContactManager.dao;

import com.example.SmartContactManager.entities.Contact;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.plaf.ListUI;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

/*
    //we used this before using pagination
    @Query("from Contact as c where c.user.userid=:userId ")
    public List<Contact> findContactsByUser(@Param("userId") int userId);

 */

    @Query("from Contact as c where c.user.userid=:userId ")
    public Page<Contact> findContactsByUser(@Param("userId") int userId,
                                            Pageable pageable);
                        //this pageable has info of current page and contacts per page-5

}
