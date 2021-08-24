# [Yeah 24 프로젝트] 7. Order 클래스 작성하기



**DB Schema**

![](https://lh6.googleusercontent.com/4kc35oe9FuGGgx7UezeaRB90cOFG_J_44_Ti8yRFkH3nbfVyXHUkVsbwjdT8kPggZ68CdCPU37n4878y8Ob4iVVTfuQRNASYc7v30LUp2zsR_ZIJQ5D-p-RnVfvg72oam_l-S-hq)





## 1) Order 클래스 작성하기

**Order 클래스**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"orderer", "orderBooks"})
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_order_to_user"))
    private User orderer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Address deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderBook> orderBooks = new ArrayList<>();


    private Order(User orderer, OrderStatus orderStatus, Address deliveryAddress) {
        this.orderer = orderer;
        this.status = orderStatus;
        this.deliveryAddress = new Address(deliveryAddress.getPostcode(), deliveryAddress.getRoadAddr(), deliveryAddress.getJibunAddr(), deliveryAddress.getDetailAddr(), deliveryAddress.getExtraAddr());
    }


    public static Order createOrder(User orderer, List<OrderInfoVO> orderInfos, Address deliveryAddress) {
        Order order = new Order(orderer, OrderStatus.ORDER, deliveryAddress);

        for (OrderInfoVO orderInfo : orderInfos) {
            OrderBook orderBook = new OrderBook(order, orderInfo);
            Book book = orderInfo.getBook();
            book.minusStockQuantity(orderInfo.getAmount());
            order.orderBooks.add(orderBook);
        }

        return order;
    }


    public void cancel() {
        this.status = OrderStatus.CANCEL;

        for (OrderBook orderBook : this.orderBooks) {
            orderBook.cancel();
        }
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderInfoVO {
        private Book book;
        private int amount;
    }
}
```

- 필드 설명
  - orderer: 주문한 User
  - status: ORDER(주문 완료 상태) 또는 CANCEL(주문 취소 상태)
  - deliveryAddress: 배송지 주소



- Order 대 OrderBook은 일대다 관계다. 
  - Order에서 OrderBook까지 관리하고 싶어서 양방향 매핑으로 @OneToMany 애너테이션을 사용했다.
  - cascade는 ALL을 주지않고 PERSIST 속성으로 설정해서 저장만 같이 되게 했다.
    - @OneToMany인 경우 삭제시 CascadeType.REMOVE 속성이 걸려 있으면 성능 이슈가 생길 수 있다. 
    - [참고 링크](https://thorben-janssen.com/avoid-cascadetype-delete-many-assocations/)
- Order를 생성할 때는 생성자가 아니라 `static` 메서드인 `createOrder(...)`로 생성하게 했다.
  - 생성자보다 의미가 명확한 것 같아서 이렇게 했다.



- `OrderInfoVO` 클래스
  - 사용자가 도서를 주문할 때 **<u>어떤 책을 몇 권 주문하고 싶은지</u>**에 대한 정보를 담는 클래스



- static Order createOrder(User orderer, List\<OrderInfoVO> orderInfos, Address deliveryAddress)
  - 주문을 할 때 필요한 정보는 아래와 같다.
    - 누가 주문하는지 (User orderer),
    - 어떤 도서들을 몇 권씩 주문할 것인지 (List\<OrderInfoVO> orderInfos),
    - 어디로 배송할 것인지 (Address deliveryAddress)
  - 따라서 위와 같은 정보들을 받아서 Order를 생성하고 해당 Order에 알맞은 OrderBook 인스턴스들을 orderBooks 리스트에 넣어준다. 
  - 또, 주문한 수량 만큼 Book의 재고 수량을 감소시켜준다.



- void cancel() 

  - 주문 취소시 주문 상태를 CANCEL로 바꿔준다.
  - 갖고 있는 orderBook 인스턴스들에게 모두 취소 메시지를 날린다.

  



## 2) OrderBook 클래스 작성하기

**OrderBookKey**

```java
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class OrderBookKey implements Serializable {

    private Long orderId;

    private Long bookId;
}
```

- OrderBook의 복합키 클래스를 정의한다.



**OrderBook**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderBook extends AuditingEntity {

    @EmbeddedId
    private OrderBookKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_book_to_order"))
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_order_book_to_book"))
    @MapsId("bookId")
    private Book book;

    private int amount;
    private int orderPrice;

    public OrderBook(Order order, OrderInfoVO orderInfo) {
        this.order = order;
        this.book = orderInfo.getBook();
        this.id = new OrderBookKey(order.getId(), orderInfo.getBook().getId());
        this.amount = orderInfo.getAmount();
        this.orderPrice = orderInfo.getBook().getPrice();
    }


    public void cancel() {
        this.book.plusStockQuantity(amount);
    }

}
```

- OrderBook 조인 테이블에는 복합키 컬럼 외에도 amount(주문 수량), orderPrice(주문 가격) 컬럼이 존재한다.



- OrderBook은 Order와 OrderInfoVO를 통해 생성하게 한다.

  

- void cancel()
  - 주문 취소시에는 Book의 재고를 다시 늘려준다. 





