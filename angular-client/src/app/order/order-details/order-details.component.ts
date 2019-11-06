import { OrderHttpService } from './../../services/order-http.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs';
import {formatDate} from './../util';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit, OnDestroy {
  paramsSubscription: Subscription;
  order: any;
  private orderID: number ;

  constructor(private route: ActivatedRoute, private orderService: OrderHttpService) {
  }

  ngOnInit() {
    // this.recipeID= this.route.snapshot.params['id'];
     this.paramsSubscription = this.route.params
   .subscribe(
     (params: Params) => {
       this.orderID = params.id;
       this.orderService.getOrder(this.orderID).subscribe((order) => {
        this.order = order.value;
        this.order.orderDate = formatDate(this.order.orderDate);
        this.order.shippedDate = formatDate(this.order.shippedDate);
        this.order.requiredDate = formatDate(this.order.requiredDate);
      });

     }
   );
   }

   ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
