import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RegistrationWaitingListRoutingModule } from './registration-waiting-list-routing.module';
import { RegistrationWaitingListComponent } from './registration-waiting-list.component';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';


@NgModule({
  declarations: [
    RegistrationWaitingListComponent
  ],
  imports: [
    CommonModule,
    RegistrationWaitingListRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class RegistrationWaitingListModule { }
