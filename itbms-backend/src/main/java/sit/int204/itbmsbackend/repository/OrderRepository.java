package sit.int204.itbmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sit.int204.itbmsbackend.constant.OrderStatus;
import sit.int204.itbmsbackend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByBuyer_Id(Integer buyerId, Pageable pageable);
    Page<Order> findByBuyer_IdAndStatus(Integer buyer_Id, OrderStatus orderStatus, Pageable pageable);
    Page<Order> findBySeller_Id(Integer sellerId, Pageable pageable);
    Page<Order> findBySeller_IdAndStatus(Integer sellerId, OrderStatus orderStatus, Pageable pageable);

    // Count orders that a specific buyer has NOT viewed
    @Query("SELECT COUNT(o) FROM Order o WHERE o.buyer.id = :userId AND o.buyerViewed = false")
    Long countUnviewedByBuyer(@Param("userId") Integer userId);

    // Count orders that a specific seller has NOT viewed
    @Query("SELECT COUNT(o) FROM Order o WHERE o.seller.id = :userId AND o.sellerViewed = false")
    Long countUnviewedBySeller(@Param("userId") Integer userId);
}