package com.revshop.productservice.entity;

import java.util.List;

import com.revshop.productservice.entity.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Categories")
public class Category {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int categoryId;

	    @Column(name = "category_name", nullable = false, length = 100)
	    private String categoryName;

	    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	    private List<Size> sizes;

	    // Getters and Setters
	    public int getCategoryId() {
	        return categoryId;
	    }

	    public void setCategoryId(int categoryId) {
	        this.categoryId = categoryId;
	    }

	    public String getCategoryName() {
	        return categoryName;
	    }

	    public void setCategoryName(String categoryName) {
	        this.categoryName = categoryName;
	    }

	    public List<Size> getSizes() {
	        return sizes;
	    }

	    public void setSizes(List<Size> sizes) {
	        this.sizes = sizes;
	    }

		@Override
		public String toString() {
			return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", sizes=" + sizes + "]";
		}
	    
	    
}
