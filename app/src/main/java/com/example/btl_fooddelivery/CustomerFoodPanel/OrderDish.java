package com.example.btl_fooddelivery.CustomerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.btl_fooddelivery.ChefFoodPanel.UpdateDishModel;
import com.example.btl_fooddelivery.Model.User;
import com.example.btl_fooddelivery.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDish extends AppCompatActivity {

    String RandomId, ChefID;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = (TextView) findViewById(R.id.food_name);
        ChefName = (TextView) findViewById(R.id.chef_name);
        ChefLoaction = (TextView) findViewById(R.id.chef_location);
        FoodQuantity = (TextView) findViewById(R.id.food_quantity);
        FoodPrice = (TextView) findViewById(R.id.food_price);
        FoodDescription = (TextView) findViewById(R.id.food_description);
        imageView = (ImageView) findViewById(R.id.image);
        additem = (ElegantNumberButton) findViewById(R.id.number_btn);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("User").child(userid);

        RandomId = getIntent().getStringExtra("FoodMenu");
        ChefID = getIntent().getStringExtra("ChefId");
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User cust = dataSnapshot.getValue(User.class);
                City = cust.getCity();

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UpdateDishModel updateDishModel = snapshot.getValue(UpdateDishModel.class);
                        Foodname.setText(updateDishModel.getDishes());

                        String qua = "<b>" + "Số lượng: " + "</b>" + updateDishModel.getQuantity();
                        FoodQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Mô tả: " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Giá: vnd " + "</b>" + updateDishModel.getPrice();
                        FoodPrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(imageView);

                        chefdata = FirebaseDatabase.getInstance().getReference("User").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User chef = dataSnapshot.getValue(User.class);
                                String name = "<b>" + "Tên đầu bếp: " + "</b>" + chef.getFname() + " " + chef.getLname();
                                ChefName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Địa chỉ: " + "</b>" + chef.getAddress();
                                ChefLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Cart cart = snapshot.getValue(Cart.class);
                                        if (snapshot.exists()) {
                                            assert cart != null;
                                            additem.setNumber(cart.getDishQuantity());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Cart cart1=null;
                                                if (dataSnapshot.exists()) {
                                                    int totalcount = 0;
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        totalcount++;
                                                    }
                                                    int i = 0;
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        i++;
                                                        if (i == totalcount) {
                                                            cart1 = snapshot.getValue(Cart.class);
                                                        }
                                                    }
                                                    if(ChefID.equals(cart1.getChefId())){
                                                        data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(ChefID).child(RandomId);
                                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                                                dishname = update.getDishes();
                                                                dishprice = Integer.parseInt(update.getPrice());

                                                                int num = Integer.parseInt(additem.getNumber());
                                                                int totalprice = num * dishprice;
                                                                if (num != 0) {
                                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                                    hashMap.put("DishName", dishname);
                                                                    hashMap.put("DishID", RandomId);
                                                                    hashMap.put("DishQuantity", String.valueOf(num));
                                                                    hashMap.put("Price", String.valueOf(dishprice));
                                                                    hashMap.put("Totalprice", String.valueOf(totalprice));
                                                                    hashMap.put("ChefId", ChefID);
                                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                } else {

                                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                    else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDish.this);
                                                        builder.setMessage("Bạn không thể đặt món của nhiều đầu bếp trong cùng thời gian giao hàng!");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                Intent intent = new Intent(OrderDish.this, CustomerFoodPanel_BottomNavigation.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                        AlertDialog alert = builder.create();
                                                        alert.show();
                                                    }
                                                }
                                                else{
                                                    data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(ChefID).child(RandomId);
                                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                                            dishname = update.getDishes();
                                                            dishprice = Integer.parseInt(update.getPrice());
                                                            int num = Integer.parseInt(additem.getNumber());
                                                            int totalprice = num * dishprice;

                                                            if (num != 0) {
                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put("DishName", dishname);
                                                                hashMap.put("DishID", RandomId);
                                                                hashMap.put("DishQuantity", String.valueOf(num));
                                                                hashMap.put("Price", String.valueOf(dishprice));
                                                                hashMap.put("Totalprice", String.valueOf(totalprice));
                                                                hashMap.put("ChefId", ChefID);
                                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}