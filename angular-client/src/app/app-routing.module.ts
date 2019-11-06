import { OrderDetailsComponent } from './order/order-details/order-details.component';
import { OrderTableComponent } from './order/order-table/order-table.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  { path: '', component: OrderTableComponent},
  { path: 'orders', component: OrderTableComponent},
  { path: 'orders/:id', component: OrderDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
