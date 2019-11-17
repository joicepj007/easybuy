package easybuy.com.easybuy.Controller;

public class Constants {

    public interface API {

        String BASE_URL =  "http://18.220.177.244/grocaryapp/index.php?route=";


        String LOGIN = BASE_URL+"restapi/account/login/groceryLogin";

        String ORDERLIST = BASE_URL+"api/order/agentOrderList";

        String ORDERHISTORY = BASE_URL+"api/order/agentOrderHistory";

        String ORDERACCEPTREJECT = BASE_URL+"api/order/history";

       // String ORDERREPORTMAIL = BASE_URL+"api/order/export";

        String ORDERREPORTMAIL = BASE_URL+"api/order/pdfexport";

    }
}
