import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RenewalRegionWaitingListRoutingModule } from './renewal-region-waiting-list-routing.module';
import { RenewalRegionWaitingListComponent } from './renewal-region-waiting-list.component';
import { SharedModule } from 'src/app/theme/shared/shared.module';


@NgModule({
  declarations: [
    RenewalRegionWaitingListComponent
  ],
  imports: [
    CommonModule,
    RenewalRegionWaitingListRoutingModule,
    SharedModule
  ]
})
export class RenewalRegionWaitingListModule { }
