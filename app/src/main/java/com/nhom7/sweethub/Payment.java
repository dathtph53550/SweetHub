package com.nhom7.sweethub;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.os.StrictMode;import android.util.Log;import android.view.View;import android.widget.Button;import android.widget.ImageView;import android.widget.Toast;import androidx.activity.EdgeToEdge;import androidx.appcompat.app.AppCompatActivity;import androidx.core.graphics.Insets;import androidx.core.view.ViewCompat;import androidx.core.view.WindowInsetsCompat;import com.bumptech.glide.Glide;import com.nhom7.sweethub.Model.Cart;import com.nhom7.sweethub.Model.OrderCartt;import com.nhom7.sweethub.Model.Response;import com.nhom7.sweethub.servers.HttpRequest;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import java.nio.charset.StandardCharsets;import java.util.ArrayList;import java.util.Map;import java.util.Random;import java.util.TreeMap;import javax.crypto.Mac;import javax.crypto.spec.SecretKeySpec;import lombok.extern.slf4j.Slf4j;import retrofit2.Call;import retrofit2.Callback;import vn.payos.PayOS;import vn.payos.type.CheckoutResponseData;import vn.payos.type.PaymentData;import vn.payos.type.PaymentLinkData;@Slf4jpublic class Payment extends AppCompatActivity {    private static final String CHECKSUM_KEY = "468e88c85f5161bd725b0b50d095acd1b3c6e2ed14ed88af3f205ce61a10db24";    private static final Logger log = LoggerFactory.getLogger(Payment.class);    PayOS payOS = new PayOS("ea769d3e-2c6d-4b09-9adb-7ca561560981", "5ca75666-f36f-4402-ba06-2c4ea84df79a", "468e88c85f5161bd725b0b50d095acd1b3c6e2ed14ed88af3f205ce61a10db24");    ImageView qrThanhToan;    Button btnPaid, btnCancel;    ImageView backButton;    ArrayList<Cart> list;    double totalAmount;    ArrayList<String> productNames,productPrices,productQuantities;    HttpRequest httpRequest;    ArrayList<Cart> cartList;    String address;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        EdgeToEdge.enable(this);        setContentView(R.layout.activity_payment);        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);            return insets;        });        httpRequest = new HttpRequest();         productNames = new ArrayList<>();         productPrices = new ArrayList<>();         productQuantities = new ArrayList<>();        list = new ArrayList<>();        Intent intent = getIntent();        address = intent.getStringExtra("address");        cartList = getIntent().getParcelableArrayListExtra("cartList");        double totalPrice = intent.getDoubleExtra("amount", 0.0); // Lấy giá trị amount (totalPrice)        int quantity = intent.getIntExtra("quantity", 0);        if (cartList != null) {            for (Cart cart : cartList) {                productNames.add(cart.getName());                productPrices.add(cart.getPrice());                productQuantities.add(cart.getQuantity());            }        }        Log.d("yyyyyyy", "onCreate: " + productNames.size());        Log.d("yyyyyyy", "onCreate: " + productPrices.size());        Log.d("yyyyyyy", "onCreate: " + productQuantities.size());        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();        StrictMode.setThreadPolicy(policy);        qrThanhToan = findViewById(R.id.qrThanhToan);        btnPaid = findViewById(R.id.btnPaid);        btnCancel = findViewById(R.id.btnCancel);        backButton = findViewById(R.id.backButton);        //https://img.vietqr.io/image/${paymentInfo.bin}-${paymentInfo.accountNumber}-vietqr_pro.jpg?addInfo=${paymentInfo.description.replace(" ","")}&amount=${paymentInfo.amount}        long orderCode = generateRandomNumberBasedOnTime();        int amount = (int)totalPrice;        String description = "Thanh Toan " + quantity + "San Pham"; // them còng ten banh        String cancelUrl = "/abc.html";        String returnUrl = "/abc.html";        String signature = generateSignature(orderCode, amount, description, cancelUrl, returnUrl, CHECKSUM_KEY);        try {            long abc = Long.parseLong(String.valueOf(orderCode));            PaymentData paymentData = PaymentData.builder()                    .orderCode(abc)                    .amount(amount)                    .description(description)                    .returnUrl(cancelUrl)                    .cancelUrl(returnUrl)                    .signature(signature)                    .build();            CheckoutResponseData paymentLinkData = payOS.createPaymentLink(paymentData);            String imageUrl = "https://img.vietqr.io/image/" +                    paymentLinkData.getBin() + "-" +                    paymentLinkData.getAccountNumber() + "-vietqr_pro.jpg?" +                    "addInfo=" + paymentLinkData.getDescription().replace(" ", "") +                    "&amount=" + paymentLinkData.getAmount();            Glide.with(this)                    .load(imageUrl)                    .error(R.drawable.baseline_broken_image_24)                    .into(qrThanhToan);            Log.d("paymentLinkData",   imageUrl);            Log.d("paymentLinkData", paymentLinkData.getCheckoutUrl());        } catch (Exception e) {            e.printStackTrace();            throw new RuntimeException(e);        }        backButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                finish();            }        });        btnPaid.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                try {                    PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(Long.parseLong(String.valueOf(orderCode)));                    checkPaymentStatus(paymentLinkData);                } catch (Exception e) {                    throw new RuntimeException(e);                }            }        });        btnCancel.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                try {                    PaymentLinkData paymentLinkData = payOS.cancelPaymentLink(orderCode, "Hủy đơn hàng");                    if (paymentLinkData != null && paymentLinkData.getStatus().equals("CANCELLED")) {                        Toast.makeText(Payment.this, "Đơn hàng đã hủy thành công!", Toast.LENGTH_SHORT).show();                    } else {                        Toast.makeText(Payment.this, "Không thể hủy đơn hàng.", Toast.LENGTH_SHORT).show();                    }                } catch (Exception e) {                    Toast.makeText(Payment.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();                }            }        });    }    public static long generateRandomNumberBasedOnTime() {        long time = System.currentTimeMillis();        Random random = new Random();        int randomPart = random.nextInt(1000000);        long result = time % 100000000L + randomPart;        return (long) result;    }    //by gpt    private String generateSignature(long orderCode, int amount, String description, String cancelUrl, String returnUrl, String secretKey) {        Map<String, String> params = new TreeMap<>();        params.put("amount", String.valueOf(amount));        params.put("cancelUrl", cancelUrl);        params.put("description", description);        params.put("orderCode", String.valueOf(orderCode));        params.put("returnUrl", returnUrl);        StringBuilder signatureString = new StringBuilder();        for (Map.Entry<String, String> entry : params.entrySet()) {            if (signatureString.length() > 0) {                signatureString.append("&");            }            signatureString.append(entry.getKey()).append("=").append(entry.getValue());        }        try {            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");            sha256_HMAC.init(secret_key);            byte[] signedBytes = sha256_HMAC.doFinal(signatureString.toString().getBytes(StandardCharsets.UTF_8));            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                return java.util.Base64.getEncoder().encodeToString(signedBytes); // Base64 encoded signature (API >= 26)            } else {                return android.util.Base64.encodeToString(signedBytes, android.util.Base64.DEFAULT); // Android hỗ trợ Base64 (API < 26)            }        } catch (Exception e) {            e.printStackTrace();            return null;        }    }    public void checkPaymentStatus(PaymentLinkData paymentLinkData) {        String status = paymentLinkData.getStatus();        int amount = paymentLinkData.getAmount();        int amountPaid = paymentLinkData.getAmountPaid();        int amountRemaining = paymentLinkData.getAmountRemaining();        String canceledAt = paymentLinkData.getCanceledAt();        switch (status) {            case "PAID":                if (amountPaid == amount && amountRemaining == 0) {                    Toast.makeText(Payment.this, "Thanh toán thành công.", Toast.LENGTH_SHORT).show();                    addOrderToCart();                    deleteCart();                    startActivity(new Intent(Payment.this, PaidSuccess.class));                    finish();                } else {                    Toast.makeText(Payment.this, "Thanh toán chưa hoàn tất. Số tiền đã thanh toán: " + amountPaid, Toast.LENGTH_SHORT).show();                }                break;            case "PENDING":                Toast.makeText(Payment.this, "Thanh toán đang chờ xử lý.", Toast.LENGTH_SHORT).show();                break;            case "CANCELLED":                if (canceledAt != null) {                    Toast.makeText(Payment.this, "Thanh toán bị hủy tại: " + canceledAt, Toast.LENGTH_SHORT).show();                } else {                    Toast.makeText(Payment.this, "Thanh toán bị hủy, không có thời gian hủy.", Toast.LENGTH_SHORT).show();                }                break;            case "FAILED":                Toast.makeText(Payment.this, "Thanh toán không thành công.", Toast.LENGTH_SHORT).show();                break;            default:                Toast.makeText(Payment.this, "Trạng thái thanh toán không xác định: " + status, Toast.LENGTH_SHORT).show();                break;        }        if (amountRemaining > 0) {            Toast.makeText(Payment.this, "Số tiền còn lại: " + amountRemaining, Toast.LENGTH_SHORT).show();        }    }    public void addOrderToCart() {        // Tạo danh sách OrderCart từ các mảng đã có        ArrayList<OrderCartt> orderCartList = new ArrayList<>();        // Log dữ liệu trong orderCartList        for(int i = 0; i < cartList.size();i++){            Log.d("ttttttt", "addOrderToCart: " + cartList.get(i).getName());            Log.d("ttttttt", "addOrderToCart: " + cartList.get(i).getPrice());            Log.d("ttttttt", "addOrderToCart: " + cartList.get(i).getQuantity());            orderCartList.add(new OrderCartt(cartList.get(i).getName().toString(), cartList.get(i).getPrice().toString(), cartList.get(i).getQuantity()));        }        // Gọi API để thêm đơn hàng vào giỏ        HttpRequest httpRequest = new HttpRequest();        httpRequest.callAPI().addListOrderCart(orderCartList).enqueue(new Callback<Response<OrderCartt>>() {            @Override            public void onResponse(Call<Response<OrderCartt>> call, retrofit2.Response<Response<OrderCartt>> response) {                if(response.isSuccessful()){                    if(response.body().getStatus() == 200){                        Toast.makeText(Payment.this, "Đã thêm đơn hàng vào giỏ hàng", Toast.LENGTH_SHORT).show();                    }                }            }            @Override            public void onFailure(Call<Response<OrderCartt>> call, Throwable t) {            }        });    }    public void deleteCart(){        httpRequest.callAPI().clearListOrderCart().enqueue(new Callback<Response>() {            @Override            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {                if(response.isSuccessful()){                    if(response.body().getStatus() == 200){                        Log.d("ttttttt", "onResponse: " + "Đã xoá hết cart");                    }                }                else Log.d("ttttttt", "onResponse: " + "Chua xoa thanh cong");            }            @Override            public void onFailure(Call<Response> call, Throwable t) {                Log.d("ttttttt", "onResponse: " + t.getMessage());            }        });    }}