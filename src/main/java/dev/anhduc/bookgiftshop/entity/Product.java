package dev.anhduc.bookgiftshop.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.anhduc.bookgiftshop.utils.SecurityUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên sản phẩm bắt buộc nhập")
    private String name;
    @NotNull(message = "Số lượng không được để trống")
    @Min(0)
    private Long stockQuantity;
    private double price;
    private Long sold;
    private String shortDescription;
    private String detailDescription;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean deleted;
    private String photo;
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Author> authors;
    @NotNull(message = "Phải chọn nhà xuất bản")
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    @NotEmpty(message = "Sản phẩm phải thuộc ít nhất một danh mục")
    @ManyToMany
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    @ManyToMany
    @JoinTable(name = "product_promotion", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private List<Promotion> promotions;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        this.deleted = false;
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
