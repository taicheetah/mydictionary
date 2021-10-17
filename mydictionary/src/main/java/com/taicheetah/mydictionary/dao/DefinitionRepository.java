package com.taicheetah.mydictionary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taicheetah.mydictionary.entity.Definition;
import com.taicheetah.mydictionary.entity.DefinitionId;

public interface DefinitionRepository extends JpaRepository<Definition, DefinitionId> {

}
