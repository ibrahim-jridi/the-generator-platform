import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IndustrieVetRoutingModule } from './industrie-vet-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { IndustrieVetComponent } from './industrie-vet.component';


@NgModule({
  declarations: [IndustrieVetComponent],
  imports: [
    CommonModule,
    IndustrieVetRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class IndustrieVetModule { }
