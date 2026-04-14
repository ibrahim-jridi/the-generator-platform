import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SubsidiaryPctRoutingModule } from './subsidiary-pct-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { SubsidiaryPctComponent } from './subsidiary-pct.component';


@NgModule({
  declarations: [SubsidiaryPctComponent],
  imports: [
    CommonModule,
    SubsidiaryPctRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class SubsidiaryPctModule { }
