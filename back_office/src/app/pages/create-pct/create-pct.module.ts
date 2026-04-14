import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CreatePctRoutingModule } from './create-pct-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CreatePctComponent } from './create-pct.component';


@NgModule({
  declarations: [CreatePctComponent],
  imports: [
    CommonModule,
    CreatePctRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class CreatePctModule { }
