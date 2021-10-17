package com.taicheetah.mydictionary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taicheetah.mydictionary.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	
}

