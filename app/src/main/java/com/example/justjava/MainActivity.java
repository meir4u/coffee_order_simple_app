package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final int ITEM_COST = 5;
    final int MAX_ORDER_CUPS = 100;
    final int MIN_ORDER_CUPS = 1;
    final int WHIPPED_CREAM_PRICE = 1;
    final int CHOCOLATE_PRICE = 2;

    boolean whippedCream;
    boolean hasChocolate;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setWhippedCream(View view){
        CheckBox v = (CheckBox)findViewById(R.id.whipped_cream_check_box);
        this.whippedCream = v.isChecked();
    }

    public void setHasChocolate(View view){
        CheckBox v = (CheckBox)findViewById(R.id.chocolate_check_box);
        this.hasChocolate = v.isChecked();
    }

    public void setName(){
        EditText n = (EditText)findViewById(R.id.name_edit_text);
        this.name = n.getText().toString();
    }

    private int getQuantityTextView(){
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view
        );
        return Integer.parseInt(quantityTextView.getText().toString());
    }

    /**
     * send the total order to email app.
     * @param view
     */
    public void submitOrder(View view){
        int total_price = calculateOrderSummary();
        setName();
        String priceMessage = createOrderSummary(total_price);
        String subject = getString(R.string.email_subject,this.name);

        this.composeEmail( subject,priceMessage);
    }

    /**
     * Change the displayed quantity of coffee on the screen
     * @param number
     */
    @SuppressLint("SetTextI18n")
    private void displayQuantity(int number){
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view
        );
        quantityTextView.setText(Integer.toString(number));
    }

    /**
     * send the text with order details to email app
     * @param subject
     * @param body
     */
    public void composeEmail(String subject,String body) {
        String[] emails = {"meir@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * increment the total coffee cups to order
     * max cup of coffee is 100 caps
     * @param view
     */
    public void increment(View view){
        int n = getQuantityTextView();
        if(getQuantityTextView() <= MAX_ORDER_CUPS){
            n += 1;
        }

        displayQuantity(n);
    }

    /**
     * decrement the total coffee cups to order
     * minimum order 1 cup of coffee
     * @param view
     */
    public void decrement(View view){
        int n = getQuantityTextView();
        if( n > MIN_ORDER_CUPS){
           n -= 1;
        }
        displayQuantity(n);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculateOrderSummary() {
        int price = getQuantityTextView() * ITEM_COST;
        if(this.hasChocolate){
            price += CHOCOLATE_PRICE;
        }
        if(this.whippedCream){
            price += WHIPPED_CREAM_PRICE;
        }
        return price;
    }

    /**
     * create order summary string
     * @param total - total to pay
     * @return string with order details
     */
    private String createOrderSummary(int total){
        String sender_name = getString(R.string.summary_client_full_name,this.name);
        String quantity = getString(R.string.summary_total_cups_coffee, getQuantityTextView());
        String cream = (this.whippedCream) ? getString(R.string.summary_whipped_cream_yes) : getString(R.string.summary_whipped_cream_no) ;
        String chocolate = (this.hasChocolate) ? getString(R.string.summary_chocolate_yes) : getString(R.string.summary_chocolate_no);
        String endLine = getString(R.string.summary_thank_you);
        String summary_total = getString(R.string.summary_total, total);

        return (sender_name + "\n" +
                cream + "\n" +
                chocolate + "\n" +
                quantity + "\n" +
                summary_total + "\n" +
                endLine);
    }

}