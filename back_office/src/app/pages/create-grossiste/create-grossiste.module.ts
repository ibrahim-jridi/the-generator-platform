import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CreateGrossisteRoutingModule } from './create-grossiste-routing.module';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CreateGrossisteComponent } from './create-grossiste.component';


@NgModule({
  declarations: [CreateGrossisteComponent],
  imports: [
    CommonModule,
    CreateGrossisteRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ]
})
export class CreateGrossisteModule { }
