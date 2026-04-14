import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionListComponent } from './transaction-list/transaction-list.component';
import { CustomButtonModule } from 'src/app/theme/shared/components';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { TranslateModule } from '@ngx-translate/core';
import { TransactionViewComponent } from './transaction-view/transaction-view.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    TransactionListComponent,
    TransactionViewComponent
  ],
  imports: [
    CommonModule,
    TransactionRoutingModule,
    TranslateModule,
    CustomTableModule,
    CustomButtonModule,
    ReactiveFormsModule
  ]
})
export class TransactionModule { }
