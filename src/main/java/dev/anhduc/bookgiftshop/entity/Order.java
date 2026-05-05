package dev.anhduc.bookgiftshop.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.anhduc.bookgiftshop.utils.SecurityUtil;
import dev.anhduc.bookgiftshop.utils.constants.OrderStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalPrice;
    private double discount;
    private double finalPrice;
    private String receiverName;
    private String receiverAddress;
    @Pattern(regexp = "^[0-9]{10}$", message = "SĐT không hợp lệ")
    private String receiverPhone;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean deleted;
    private String updatedBy;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @PrePersist
    public void beforeCreate() {
        this.createdAt = Instant.now();
        this.status = OrderStatusEnum.PENDING;
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
