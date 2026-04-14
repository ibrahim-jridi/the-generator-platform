import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RenewalCategoryWaitingListRoutingModule } from './renewal-category-waiting-list-routing.module';
import { RenewalCategoryWaitingListComponent } from './renewal-category-waiting-list.component';
import {
  UnsubscribeWaitingListRoutingModule
} from "../unsubscribe-waiting-list/unsubscribe-waiting-list-routing.module";
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {ReactiveFormsModule} from "@angular/forms";
import {NgSelectModule} from "@ng-select/ng-select";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {SharedModule} from "../../theme/shared/shared.module";
import {CustomButtonModule} from "../../theme/shared/components";


@NgModule({
  declarations: [
    RenewalCategoryWaitingListComponent
  ],
  imports: [
    CommonModule,
    RenewalCategoryWaitingListRoutingModule,
    TranslateModule,
    CustomTableModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgxIntlTelInputModule,
    SharedModule,
    CustomButtonModule
  ]
})
export class RenewalCategoryWaitingListModule { }
