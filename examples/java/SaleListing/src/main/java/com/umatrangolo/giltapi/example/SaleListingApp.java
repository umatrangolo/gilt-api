package com.umatrangolo.giltapi.example;

import com.umatrangolo.giltapi.Sales;
import com.umatrangolo.giltapi.Products;

import static com.umatrangolo.giltapi.client.GiltClientFactory.*;

public class SaleListingApp {
    public static void main(String[] args) {
        System.out.println("**** Fetching sale listing ... ");

        Sales salesClient = newSalesClientInstance();
        Products productClient = newProductsClientInstance();

        // active sales
        System.out.println("======== ACTIVE SALES ========");
        activeSales: LinearSeq[Sale] = Await.result(salesClient.activeSales, 30 seconds)
  activeSales.groupBy { _.store }.foreach { case (store, sales) =>
    println(">> Store: " + store)
    sales.foreach { sale => println(">> " + sale.name) }
  }

    }
}
