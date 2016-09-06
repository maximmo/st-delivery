package ru.serv_techno.delivery_st;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProductCardActivity extends AppCompatActivity {

    //public final static String ID = "ID";
    public Product mProduct;

    private TextView twProductName;
    private TextView twProductPrice;
    private TextView twProductWeight;
    private TextView twProductDesc;
    private Button bntAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_card);

//        mProduct = Product.getProductById(getIntent().getIntExtra("ProductID", 0));
        Intent intent = getIntent();

        long ProductID = intent.getLongExtra("ProductID", 0);
        int iProductID = (int) ProductID;
        mProduct = Product.getProductById(iProductID);
        if (mProduct!=null) {

            this.twProductName = (TextView) findViewById(R.id.product_name);
            this.twProductName.setText(mProduct.name);

            this.twProductPrice = (TextView) findViewById(R.id.product_price);
            this.twProductPrice.setText(String.valueOf(mProduct.price) + " \u20BD");

            this.twProductWeight = (TextView) findViewById(R.id.product_weight);
            this.twProductWeight.setText(String.valueOf(mProduct.weight));

            this.twProductDesc = (TextView) findViewById(R.id.product_desc);
            this.twProductDesc.setText(mProduct.description);

            this.bntAdd = (Button) findViewById(R.id.buttonAdd);
            this.bntAdd.setOnClickListener(btnAddPress);

            if (mProduct.bigImageLink != null) {
                new DownloadImageTask((ImageView) findViewById(R.id.product_image)).execute(mProduct.bigImageLink);
            }

        }
    }

    View.OnClickListener btnAddPress = new View.OnClickListener() {

        public void onClick(View v) {

            Product ProductPressed = mProduct;

            List<OrderProducts> orderProduct = OrderProducts.find(OrderProducts.class, "extid = ? and productid = ?", "0", String.valueOf(ProductPressed.getId()));
            if (orderProduct.size()==0){
                OrderProducts orderProducts = new OrderProducts(0, Integer.valueOf(String.valueOf(ProductPressed.getId())), 1, 0);
                orderProducts.save();
            }else{
                OrderProducts orderProducts = orderProduct.get(0);
                orderProducts.amount++;
                orderProducts.save();
            }

            Toast toast = Toast.makeText(getApplicationContext(), "Добавлен товар: " + ProductPressed.name, Toast.LENGTH_SHORT);
            toast.show();
        }
    };
}
