package com.example.tendersystem.interfaces;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
  ID create(T entity);

  Optional<T> read(ID id);

  void update(T entity);

  void delete(ID id);

  List<T> findAll();

}
