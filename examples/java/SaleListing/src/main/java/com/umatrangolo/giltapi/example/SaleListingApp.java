package com.umatrangolo.giltapi.example;

import com.umatrangolo.giltapi.Products;
import com.umatrangolo.giltapi.Sales;

import com.umatrangolo.giltapi.model.Sale;
import com.umatrangolo.giltapi.model.Product;
import com.umatrangolo.giltapi.model.Store;

import scala.concurrent.Await;
import scala.Option;

import static com.umatrangolo.giltapi.client.GiltClientFactory.*;
import scala.concurrent.duration.Duration;

import java.util.List;

// TODO Option -> Guava Optional
// TODO scala.concurrent.Future -> Guava ListenableFuture (using a converter)
public class SaleListingApp {
    public static void main(String[] args) throws Exception {
        System.out.println("**** Fetching sale listing ... ");

        Sales salesClient = newSalesClientInstance();
        Products productClient = newProductsClientInstance();

        // active sales
        System.out.println("======== ACTIVE SALES ========");
        List<Sale> activeSales = Await.result(salesClient.getActiveSales(), Duration.create(30, "seconds"));
        for (Sale sale: activeSales) {
            System.out.println(">> " + sale.getName());
        }

        System.out.println("======== UPCOMING SALES ========");
        List<Sale> upcomingSales = Await.result(salesClient.getUpcomingSales(), Duration.create(30, "seconds"));
        for (Sale sale: upcomingSales) {
            System.out.println(">> " + sale.getName());
        }

        // details about a single sale in the Women store
        System.out.println("======== FIRST SALE FOR WOMEN ========");
        List<Sale> salesForWomen = Await.result(salesClient.getActiveSales(Store.Women), Duration.create(30, "seconds"));
        if (!salesForWomen.isEmpty()) {
            Sale sale = salesForWomen.get(0);
            System.out.println(">> Getting details about " + sale.getName());
            Sale details = Await.result(salesClient.getSale(sale.getKey(), Store.Women), Duration.create(30, "seconds")).get();
            System.out.println(">> [%s] is \n%s".format(sale.getKey(), details));
        }

        // browsing a product
        System.out.println("======== BROWSING PRODUCT ========");
        if (!salesForWomen.isEmpty()) {
            Sale sale = salesForWomen.get(0);
            List<Long> productIds = sale.getProductIds();
            if (!productIds.isEmpty()) {
                Product product = Await.result(productClient.getProduct(productIds.get(0)), Duration.create(30, "seconds")).get();
                System.out.println("---- Product with id " + product);
            }
        }
    }
}
