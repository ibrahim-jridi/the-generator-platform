import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IndustrieHumainRoutingModule } from './industrie-humain-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { IndustrieHumainComponent } from './industrie-humain.component';


@NgModule({
  declarations: [IndustrieHumainComponent],
  imports: [
    CommonModule,
    IndustrieHumainRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class IndustrieHumainModule { }
