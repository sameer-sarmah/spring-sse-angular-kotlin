/* tslint:disable:no-string-literal */
import { Component, OnInit, Input, OnChanges , SimpleChanges } from '@angular/core';


@Component({
  selector: 'app-product-table',
  templateUrl: './product-table.component.html',
  styleUrls: ['./product-table.component.css']
})
export class ProductTableComponent implements OnInit, OnChanges  {
  @Input() order: any;
  products: Array<Product>;

  constructor() {

  }

  ngOnInit() {
    if (!!this.order) {
    this.products =  this.order['orderItems'].map((orderItem) => {
      const product = new Product();
      product.id = orderItem.product.id;
      product.name = orderItem.product.name;
      product.unitPrice = orderItem.product.unitPrice;
      product.quantity = orderItem.quantity;
      return product;
    });
    console.log(this.products);
  }
}

ngOnChanges(changes: SimpleChanges) {
  for (const order in changes) {
    if (changes.hasOwnProperty(order)) {
        const change = changes[order];
        const currentOrder = change.currentValue;
        if (!!currentOrder) {
          this.products =  currentOrder['orderItems'].map((orderItem) => {
            const product = new Product();
            product.id = orderItem.product.id;
            product.name = orderItem.product.name;
            product.unitPrice = orderItem.product.unitPrice;
            product.quantity = orderItem.quantity;
            return product;
          });
        }
    }
  }
}

}

class Product {
  id: number;
  name: string;
  unitPrice: number;
  quantity: number;
}
