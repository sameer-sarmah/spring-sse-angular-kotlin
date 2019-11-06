import { OrderSseService } from './../../services/sse.service';
import { OrderHttpService } from './../../services/order-http.service';
import { Component, OnInit } from '@angular/core';
import {formatDate} from './../util';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-table',
  templateUrl: './order-table.component.html',
  styleUrls: ['./order-table.component.css']
})
export class OrderTableComponent implements OnInit {

  orders: Array<any>;

  constructor(private orderService: OrderHttpService, private router: Router, private sseService: OrderSseService) {


  }


  ngOnInit() {
    this.sseService.orderStream().subscribe((order) => {
      order.orderDate = formatDate(order.orderDate);
      order.shippedDate = formatDate(order.shippedDate);
      order.requiredDate = formatDate(order.requiredDate);
      this.orders = this.orders || [];
      this.orders = this.orders.concat([order]);
  });
}


  navigateToOrderDetails(order) {
    this.router.navigate(['orders', order.id]);
   }

}
