package easybuy.com.easybuy.ResponseClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    public OrderData orderData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public class OrderData {

        private List<orders> orders = null;


        public List<orders> getOrders() {
            return orders;
        }

        public void setOrders(List<orders> orders) {
            this.orders = orders;
        }


        public class orders {


            @SerializedName("order_id")
            @Expose
            private String order_id;

            @SerializedName("customer")
            @Expose
            private String customer;

            @SerializedName("telephone")
            @Expose
            private String telephone;

            @SerializedName("email")
            @Expose
            private String email;

            @SerializedName("store")
            @Expose
            private String store;

            @SerializedName("order_status")
            @Expose
            private String order_status;

            @SerializedName("total")
            @Expose
            private String total;

            @SerializedName("vat_charge")
            @Expose
            private String vat_charge;

            @SerializedName("delivery_charge")
            @Expose
            private String delivery_charge;

            @SerializedName("date_added")
            @Expose
            private String date_added;

            @SerializedName("date_modified")
            @Expose
            private String date_modified;

            @SerializedName("order_info")
            @Expose
            public OrderInfo orderInfo;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getCustomer() {
                return customer;
            }

            public void setCustomer(String customer) {
                this.customer = customer;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getVat_charge() {
                return vat_charge;
            }

            public void setVat_charge(String vat_charge) {
                this.vat_charge = vat_charge;
            }

            public String getDelivery_charge() {
                return delivery_charge;
            }

            public void setDelivery_charge(String delivery_charge) {
                this.delivery_charge = delivery_charge;
            }

            public String getDate_added() {
                return date_added;
            }

            public void setDate_added(String date_added) {
                this.date_added = date_added;
            }

            public String getDate_modified() {
                return date_modified;
            }

            public void setDate_modified(String date_modified) {
                this.date_modified = date_modified;
            }

            public OrderInfo getOrderInfo() {
                return orderInfo;
            }

            public void setOrderInfo(OrderInfo orderInfo) {
                this.orderInfo = orderInfo;
            }

            public class OrderInfo{

                @SerializedName("payment_address_1")
                @Expose
                private String payment_address_1;

                @SerializedName("payment_address_2")
                @Expose
                private String payment_address_2;

                @SerializedName("payment_postcode")
                @Expose
                private String payment_postcode;

                @SerializedName("payment_city")
                @Expose
                private String payment_city;

                @SerializedName("store_name")
                @Expose
                private String store_name;

                @SerializedName("order_products")
                @Expose
                private List<OrderProducts> order_products = null;

                public String getPayment_address_1() {
                    return payment_address_1;
                }

                public void setPayment_address_1(String payment_address_1) {
                    this.payment_address_1 = payment_address_1;
                }

                public String getPayment_address_2() {
                    return payment_address_2;
                }

                public void setPayment_address_2(String payment_address_2) {
                    this.payment_address_2 = payment_address_2;
                }

                public String getPayment_postcode() {
                    return payment_postcode;
                }

                public void setPayment_postcode(String payment_postcode) {
                    this.payment_postcode = payment_postcode;
                }

                public String getPayment_city() {
                    return payment_city;
                }

                public void setPayment_city(String payment_city) {
                    this.payment_city = payment_city;
                }

                public List<OrderProducts> getOrder_products() {
                    return order_products;
                }

                public void setOrder_products(List<OrderProducts> order_products) {
                    this.order_products = order_products;
                }

                public String getStore_name() {
                    return store_name;
                }

                public void setStore_name(String store_name) {
                    this.store_name = store_name;
                }

                public class OrderProducts {

                    @SerializedName("order_product_id")
                    @Expose
                    private String order_product_id;

                    @SerializedName("product_id")
                    @Expose
                    private String product_id;

                    @SerializedName("name")
                    @Expose
                    private String name;

                    @SerializedName("quantity")
                    @Expose
                    private String quantity;

                    @SerializedName("price")
                    @Expose
                    private String price;

                    @SerializedName("total")
                    @Expose
                    private String total;

                    @SerializedName("option")
                    @Expose
                    private List<Option> option = null;

                    public String getOrder_product_id() {
                        return order_product_id;
                    }

                    public void setOrder_product_id(String order_product_id) {
                        this.order_product_id = order_product_id;
                    }

                    public String getProduct_id() {
                        return product_id;
                    }

                    public void setProduct_id(String product_id) {
                        this.product_id = product_id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(String quantity) {
                        this.quantity = quantity;
                    }

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }

                    public String getTotal() {
                        return total;
                    }

                    public void setTotal(String total) {
                        this.total = total;
                    }

                    public List<Option> getOption() {
                        return option;
                    }

                    public void setOption(List<Option> option) {
                        this.option = option;
                    }

                    public class Option {

                        @SerializedName("name")
                        @Expose
                        private String name;

                        @SerializedName("value")
                        @Expose
                        private String value;

                        @SerializedName("type")
                        @Expose
                        private String type;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getValue() {
                            return value;
                        }

                        public void setValue(String value) {
                            this.value = value;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }
                    }
            }

            }
        }
    }
}

