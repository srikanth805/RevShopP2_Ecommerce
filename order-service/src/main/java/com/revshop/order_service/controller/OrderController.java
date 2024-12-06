package com.revshop.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.revshop.order_service.entity.Order;
import com.revshop.order_service.entity.OrderItem;
import com.revshop.order_service.entity.OrderResponse;
import com.revshop.order_service.entity.UserModel;
import com.revshop.order_service.respository.UserRepository;
import com.revshop.order_service.service.OrderService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/place")
	public ResponseEntity<?> createOrder(@RequestBody OrderResponse orderRes) {
		Order order = orderService.createOrder(orderRes.getUser(), orderRes.getCart(), orderRes.getShippingAddress(),
				orderRes.getBillingAddress());
		return ResponseEntity.ok(order);
	}

	@PostMapping("/items")
	public ResponseEntity<?> getOrders(@RequestBody Order order) {
		List<OrderItem> orderItems = orderService.getOrderItemsByOrder(order);
		return ResponseEntity.ok(orderItems);
	}

	@GetMapping("/orderHistory")
	public String orderHistoryBySeller(Model model) {
	    RestTemplate restTemplate = new RestTemplate();
	    
	    try {
	        // Assuming you are calling another service here (i.e., an external API)
	        String url = "http://localhost:5050/api/v1/orders/orderHistory";
	        
	        // Make sure the response is correctly parsed into a List<Order>
	        ResponseEntity<List<Order>> response = restTemplate.exchange(
	            url, 
	            HttpMethod.GET, 
	            null, 
	            new ParameterizedTypeReference<List<Order>>() {}
	        );
	        
	        // Extract the body (List<Order>) from the response
	        List<Order> orders = response.getBody();
	        
	        // Add orders to the model for Thymeleaf
	        model.addAttribute("orders", orders);
	        
	        // Return the view name
	        return "orderHistory";
	    } catch (RestClientException e) {
	        // Log the error and display a message in the view
	        model.addAttribute("error", "Failed to fetch order history: " + e.getMessage());
	        return "orderHistory";
	    }
	}

	@GetMapping("/details")
	public ResponseEntity<?> orderDetails(@RequestParam("orderId") int orderId) {
		Order order = orderService.getOrderById(orderId);
		List<OrderItem> orderItems = orderService.getOrderItemsByOrder(order);
		return ResponseEntity.ok(orderItems);
	}

	@GetMapping("/history")
	public ResponseEntity<?> orderHistory(@RequestParam("userid") int userid, Model model) {
		UserModel user = userRepository.findByUserId(userid);
		if (user == null) {
			return (ResponseEntity<?>) ResponseEntity.badRequest();
		}

		List<Order> orders = orderService.getOrdersByUser(user);
		return ResponseEntity.ok(orders);
	}
}