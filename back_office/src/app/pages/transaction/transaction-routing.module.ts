import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionListComponent } from './transaction-list/transaction-list.component';
import { TransactionViewComponent } from './transaction-view/transaction-view.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'transaction-management',
    pathMatch: 'full'
  },
  {
    path: '',
    component: TransactionListComponent
  },
  {
    path: 'view-transaction/:id',
    component: TransactionViewComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionRoutingModule { }
