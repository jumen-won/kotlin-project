# 카카오페이 서버 개발과제 - 커피 주문 시스템

## 목차

- [개발 환경](#개발-환경)
- [API 명세](#API-명세)
- [요구사항](#요구사항)

---

## 개발 환경

- Mac OS M1
- Kotlin 1.8
- Jdk 17 (corretto)
- Spring Boot 2.7.7
- JPA/Hibernate
- Docker
    - mysql:8.0
    - redis:alpine:3.17.2
    - cp-zookeeper:7.3.2
    - cp-kafka:7.3.2
- Test
    - testcontainers:1.17.2

## API 명세
http://localhost:8080/swagger-ui.html

## DB
### 상품 테이블
- 상품 정보를 저장합니다.
- disabled를 통해 soft delete 처리
- 인덱스 구성
  - 필요에 따라 금액/이름/신상품 정렬이 가능하도록 구성
```
create table `product`
(
    id         bigint auto_increment primary key,
    product_id varchar(255) not null unique key,
    title      varchar(100) not null,
    price      bigint       not null,
    disabled   boolean      not null,
    created_at timestamp    not null,
    updated_at timestamp
);

create index `product_price_index`
    on `product` (price);

create index `product_title_index`
    on `product` (title);

create index `product_created_at_index`
    on `product` (created_at desc);
```
### 지갑 테이블
- 각 유저의 지갑 정보를 저장합니다.
```
create table `wallet`
(
    id         bigint auto_increment primary key,
    balance    bigint    not null,
    created_at timestamp not null,
    updated_at timestamp
);
```
### 충전/지불 트랜잭션 테이블
- 각 지갑의 충전/지불 내역에 대한 트랜잭션을 저장합니다.
- 인덱스 구성
  - 각 지갑의 충전/지불 내역 조회용으로 구성
```
create table `wallet_transaction`
(
    id         bigint auto_increment primary key,
    receipt    binary(16)  not null,
    wallet_id  bigint      not null,
    amount     bigint      not null,
    type       varchar(50) not null, // DEPOSIT|WITHDRAW
    created_at timestamp   not null,
    updated_at timestamp
);

create index `wallet_transaction_wallet_id_index`
    on `wallet_transaction` (wallet_id);
```

### 주문 내역 테이블
- 지갑과 지불 트랜잭션을 포함한 상품에 대한 주문 내역을 저장합니다.
- 인덱스 구성
  - 각 지갑의 최신 지불 트랜잭션 내역 조회용으로 구성
```
create table `purchase`
(
    id                    bigint auto_increment primary key,
    wallet_id             bigint       not null,
    wallet_transaction_id bigint       not null,
    product_id            varchar(255) not null,
    created_at            timestamp    not null,
    updated_at            timestamp
);

create index `purchase_wallet_id_wallet_transaction_id_index`
    on `purchase` (wallet_id asc, wallet_transaction_id desc);
```

## 요구사항
### 커피 메뉴 목록 조회 API
```
#Request
GET http://localhost:8080/api/products

#Response
{
  "products": [
    {
      "productId": "coffee.1",
      "title": "test_coffee_1",
      "price": 1000
    },
    {
      "productId": "coffee.2",
      "title": "test_coffee_2",
      "price": 2000
    },
    {
      "productId": "coffee.3",
      "title": "test_coffee_3",
      "price": 3000
    },
    {
      "productId": "coffee.4",
      "title": "test_coffee_4",
      "price": 4000
    },
    {
      "productId": "coffee.5",
      "title": "test_coffee_5",
      "price": 5000
    },
    {
      "productId": "coffee.6",
      "title": "test_coffee_6",
      "price": 6000
    },
    {
      "productId": "coffee.7",
      "title": "test_coffee_7",
      "price": 7000
    },
    {
      "productId": "coffee.8",
      "title": "test_coffee_8",
      "price": 8000
    },
    {
      "productId": "coffee.9",
      "title": "test_coffee_9",
      "price": 9000
    },
    {
      "productId": "coffee.10",
      "title": "test_coffee_10",
      "price": 10000
    },
    {
      "productId": "coffee.11",
      "title": "test_coffee_11",
      "price": 11000
    },
    {
      "productId": "coffee.12",
      "title": "test_coffee_12",
      "price": 12000
    }
  ]
}
```
### 포인트 충전하기 API
- 포인트 충전시 최소 충전 금액과 단위로 처리합니다.
  - 최소 충전 금액 : 10000P
  - 충전 단위 : 만 단위
- 트랜잭션 단위
  - 지갑 객체의 잔액 충전
  - 충전 트랜잭션 저장
```
#Request
POST http://localhost:8080/api/wallet
Content-Type: application/json

{
  "walletId": 1,
  "amount": 100000
}

#Resposne
{
  "walletId": 1,
  "balance": 228529000
}
```
### 커피 주문,결제하기 API
- 해당 상품의 다건을 주문하고 지갑의 포인트 잔액으로 결제합니다.
- 트랜잭션 단위
  - 지갑 객체의 잔액 감소
  - 주문 트랜잭션 저장
  - 주문 이벤트 전송
    - 메뉴별 주문 횟수 카운트 처리
  - 주문 내역에 대한 데이터 수집 플랫폼 전송
    - kafka를 이용하여 DataLogEvent 토픽으로 주문 내역을 전송합니다.
```
#Request
POST http://localhost:8080/api/purchase
Content-Type: application/json

{
  "productId": "coffee.7",
  "walletId" : 1,
  "quantity" : 100
}

#Response
{
  "walletId": 1,
  "productId": "coffee.7",
  "price": 7000,
  "quantity": 100,
  "totalPrice": 700000,
  "receipt": "e356831c-70cc-409a-afff-351b1758c205",
  "purchaseAt": "2023-02-27"
}
```
### 인기메뉴 목록 조회 API
- 요청시점의 날짜를 포함한 최근 7일간 주문 횟수가 가장 많은 인기메뉴 3개를 조회합니다.
- 일간 상품별 주문 횟수 카운트는 주문,결제 비즈니스에서 주문 생성 이벤트 발행을 통해 처리됩니다.
- redis sorted-set을 사용합니다.
  - Key: "Purchase:{yyyy-mm-dd}" // 주문 일자 
    - key는 하루가 지나면 expire 됩니다.
  - Score: 주문 요청시 입력받은 수량(quantity)을 누적
  - Member: "Product:{productId}"
  ```
  #예시) Key별 {Member, Score} 내역 
  
  Purchase:2023-02-21
  Product:coffee.1 , 10
  Product:coffee.2 , 200
  Product:coffee.3 , 300
  
  Purchase:2023-02-22
  Product:coffee.1 , 10
  Product:coffee.2 , 200
  Product:coffee.3 , 300
  .....
  (생략)
  .....
  Purchase:2023-02-26
  Product:coffee.1 , 10
  Product:coffee.2 , 200
  Product:coffee.3 , 300
  
  Purchase:2023-02-27
  Product:coffee.1 , 10
  Product:coffee.2 , 200
  Product:coffee.3 , 300
  ```

```
#Request
GET http://localhost:8080/api/products/popular

#Response
{
  "products": [
    {
      "productInfo": {
        "productId": "coffee.7",
        "title": "test_coffee_7",
        "price": 7000
      },
      "count": 100
    },
    {
      "productInfo": {
        "productId": "coffee.1",
        "title": "test_coffee_1",
        "price": 1000
      },
      "count": 50
    },
    {
      "productInfo": {
        "productId": "coffee.2",
        "title": "test_coffee_2",
        "price": 2000
      },
      "count": 7
    }
  ]
}
```
### 테스트
- 각 서비스별 단위테스트 작성
- testcontainer를 이용하여 컨트롤러 통합테스트 작성