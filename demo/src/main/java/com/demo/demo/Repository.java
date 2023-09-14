package com.demo.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<User, Integer> {
}
