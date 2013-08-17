package com.umatrangolo.giltapi.example;

import com.umatrangolo.giltapi.Products;
import com.umatrangolo.giltapi.Sales;

import com.umatrangolo.giltapi.model.Sale;
import com.umatrangolo.giltapi.model.Product;
import com.umatrangolo.giltapi.model.Store;

import scala.concurrent.Await;

import static com.umatrangolo.giltapi.client.GiltClientFactory.*;
import scala.concurrent.duration.Duration;

import java.util.List;

public class SaleListingApp {
    public static void main(String[] args) throws Exception {
        System.out.println("**** Fetching sale listing ... ");

        Sales salesClient = newSalesClientInstance();
        Products productClient = newProductsClientInstance();

        // active sales
        System.out.println("======== ACTIVE SALES ========");
        List<Sale> activeSales = salesClient.getActiveSales().get();
        for (Sale sale: activeSales) {
            System.out.println(">> " + sale.getName());
        }

        System.out.println("======== UPCOMING SALES ========");
        List<Sale> upcomingSales = salesClient.getUpcomingSales().get();
        for (Sale sale: upcomingSales) {
            System.out.println(">> " + sale.getName());
        }

        // details about a single sale in the Women store
        System.out.println("======== FIRST SALE FOR WOMEN ========");
        List<Sale> salesForWomen = salesClient.getActiveSales(Store.Women).get();
        if (!salesForWomen.isEmpty()) {
            Sale sale = salesForWomen.get(0);
            System.out.println(">> Getting details about " + sale.getName());
            Sale details = salesClient.getSale(sale.getKey(), Store.Women).get().get();
            System.out.println(">> [%s] is \n%s".format(sale.getKey(), details));
        }

        // browsing a product
        System.out.println("======== BROWSING PRODUCT ========");
        if (!salesForWomen.isEmpty()) {
            Sale sale = salesForWomen.get(0);
            List<Long> productIds = sale.getProductIds();
            if (!productIds.isEmpty()) {
                Product product = productClient.getProduct(productIds.get(0)).get().get();
                System.out.println("---- Product with id " + product);
            }
        }

        System.out.println("======== BYE ========");
    }
}
