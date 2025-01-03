package com.poly.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private Long user_id;
    
    private Long tour_id;
    
    private Integer so_luong_nguoi;
    
    @Temporal(TemporalType.DATE)
    private Date ngay_khoi_hanh;
    
    private Long tong_tien;
    
    private Integer trang_thai;
	
    @Temporal(TemporalType.TIMESTAMP)
    private Date booking_at;

    private Integer pt_thanh_toan;

    private String ghi_chu;

    @PrePersist
    public void onCreate() {
    	this.booking_at = new Date();
    }
    
}
