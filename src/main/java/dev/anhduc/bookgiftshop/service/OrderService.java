package dev.anhduc.bookgiftshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.request.RequestOrderDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.dto.response.orderDTO.ResCreateOrderDTO;
import dev.anhduc.bookgiftshop.dto.response.orderDTO.ResOrderDTO;
import dev.anhduc.bookgiftshop.entity.Cart;
import dev.anhduc.bookgiftshop.entity.CartItem;
import dev.anhduc.bookgiftshop.entity.Order;
import dev.anhduc.bookgiftshop.entity.OrderDetail;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.entity.Promotion;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.OrderDetailRepository;
import dev.anhduc.bookgiftshop.repository.OrderRepository;
import dev.anhduc.bookgiftshop.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }

    public ResCreateOrderDTO createOrder(User currentUser, Cart cart, RequestOrderDTO orderDTO) {
        List<CartItem> cartItems = cart.getCartItems();
        List<OrderDetail> orderDetails = new ArrayList();
        Order order = new Order();
        this.orderRepository.save(order);
        double totalAmount = 0;
        double discount = 0;
        double finalAmount = 0;
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            totalAmount += cartItem.getQuantity() * cartItem.getProduct().getPrice();
            double percent = 0;
            List<Promotion> promotions = cartItem.getProduct().getPromotions();
            for (Promotion promotion : promotions) {
                percent += promotion.getDiscountPercent();
            }
            discount += percent * totalAmount / 100.0;
            long quantity = cartItem.getQuantity();
            double priceProduct = cartItem.getProduct().getPrice();
            double unitPrice = quantity * priceProduct;
            double discountProduct = percent * cartItem.getProduct().getPrice() * cartItem.getQuantity() / 100.0;
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(unitPrice);
            orderDetail.setDiscountAmount(discountProduct);
            orderDetail.setFinalPrice(unitPrice - discountProduct);
            this.orderDetailRepository.save(orderDetail);
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        finalAmount = totalAmount - discount;
        if (finalAmount < 0) {
            finalAmount = 0;
        }

        order.setFinalPrice(finalAmount);
        order.setDiscount(discount);
        order.setTotalPrice(totalAmount);
        order.setReceiverAddress(orderDTO.getReceiverAddress());
        order.setReceiverName(orderDTO.getReceiverName());
        order.setReceiverPhone(orderDTO.getReceiverPhone());
        order.setUser(currentUser);
        order = this.orderRepository.save(order);
        ResCreateOrderDTO res = new ResCreateOrderDTO();
        res.setId(order.getId());
        res.setTotalPrice(order.getTotalPrice());
        res.setDiscount(order.getDiscount());
        res.setFinalPrice(order.getFinalPrice());
        res.setReceiverAddress(order.getReceiverAddress());
        res.setReceiverName(order.getReceiverName());
        res.setReceiverPhone(order.getReceiverPhone());
        res.setStatus(order.getStatus());
        res.setCreatedAt(order.getCreatedAt());
        return res;
    }

    public ResOrderDTO fetchOrderById(Long id) throws IdInvalidException {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new IdInvalidException("Order not found!");
        }
        Order order = orderOptional.get();
        return convertOrderDTO(order);
    }

    public ResOrderDTO convertOrderDTO(Order order) {
        ResOrderDTO res = new ResOrderDTO();
        res.setId(order.getId());
        res.setTotalPrice(order.getTotalPrice());
        res.setDiscount(order.getDiscount());
        res.setFinalPrice(order.getFinalPrice());
        res.setReceiverAddress(order.getReceiverAddress());
        res.setReceiverName(order.getReceiverName());
        res.setReceiverPhone(order.getReceiverPhone());
        res.setStatus(order.getStatus());
        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());
        return res;
    }

    public ResultPaginationDTO fetchAllOrders(Specification<Order> specification, Pageable pageable) {
        Page<Order> orderPage = this.orderRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(orderPage.getTotalPages());
        meta.setTotal(orderPage.getTotalElements());
        result.setMeta(meta);
        List<ResOrderDTO> res = orderPage.getContent().stream().map(x -> this.convertOrderDTO(x))
                .collect(Collectors.toList());
        result.setResult(res);
        return result;
    }

    @Transactional
    public void deleteOrderById(Long id) throws IdInvalidException {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new IdInvalidException("Order not found!");
        }
        Order order = orderOptional.get();
        List<OrderDetail> orderDetails = order.getOrderDetails();
        List<Product> products = new ArrayList<>();
        orderDetails.forEach(u -> {
            Product product = u.getProduct();
            Product productDB = this.productRepository.findById(product.getId()).get();
            productDB.setStockQuantity(productDB.getStockQuantity() + u.getQuantity());
            this.orderDetailRepository.delete(u);
            products.add(productDB);
        });
        this.productRepository.saveAll(products);
        order.setDeleted(true);
    }
}
