import { OrderSseService } from './services/sse.service';
import { HttpService } from './services/http.service';
import { OrderHttpService } from './services/order-http.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { OrderTableComponent } from './order/order-table/order-table.component';
import { OrderDetailsComponent } from './order/order-details/order-details.component';
import { ProductTableComponent } from './order/order-details/product-table/product-table.component';
import { AddressTileComponent } from './order/order-details/address-tile/address-tile.component';
import { OverviewTileComponent } from './order/order-details/overview-tile/overview-tile.component';

@NgModule({
  declarations: [
    AppComponent,
    OrderTableComponent,
    OrderDetailsComponent,
    ProductTableComponent,
    AddressTileComponent,
    OverviewTileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [OrderHttpService, HttpService, OrderSseService],
  bootstrap: [AppComponent]
})
export class AppModule { }
