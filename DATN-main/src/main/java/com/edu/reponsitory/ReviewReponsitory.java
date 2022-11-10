package com.edu.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.model.Review;
import com.edu.model.load;
@Repository
public interface ReviewReponsitory extends JpaRepository<Review, Long>{
    @Query("select r from Review r where r.product.id = ?1 order by r.createDate desc")
    List<Review> View(Long id);
    
    @Query("select new load(count(r.product.id)) from Review r where r.product.id = ?1 group by r.product.id")
    public List<load> load(Long id);
}
