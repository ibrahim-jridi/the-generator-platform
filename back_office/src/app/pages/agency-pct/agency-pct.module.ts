import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AgencyPctRoutingModule } from './agency-pct-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { AgencyPctComponent } from './agency-pct.component';


@NgModule({
  declarations: [AgencyPctComponent],
  imports: [
    CommonModule,
    AgencyPctRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class AgencyPctModule { }
