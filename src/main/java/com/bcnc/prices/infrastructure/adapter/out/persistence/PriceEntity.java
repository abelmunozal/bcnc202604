package com.bcnc.prices.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
public class PriceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;
    
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;
    
    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;
    
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
    
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;
    
    @Column(name = "PRICE", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    
    @Column(name = "CURR", nullable = false, length = 3)
    private String curr;
    
    public PriceEntity() {
    }
    
    public PriceEntity(Long id, Long brandId, LocalDateTime startDate, LocalDateTime endDate, 
                       Integer priceList, Long productId, Integer priority, BigDecimal price, String curr) {
        this.id = id;
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.curr = curr;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBrandId() {
        return brandId;
    }
    
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public Integer getPriceList() {
        return priceList;
    }
    
    public void setPriceList(Integer priceList) {
        this.priceList = priceList;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCurr() {
        return curr;
    }
    
    public void setCurr(String curr) {
        this.curr = curr;
    }
}
