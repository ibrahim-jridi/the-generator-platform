import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../theme/shared/shared.module';
import { BestPractiseManagementRoutingModule } from './best-practise-management-routing.module';
import { BestPractiseManagementComponent } from './best-practise-management.component';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [
    CommonModule,
    BestPractiseManagementRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ],
  declarations: [BestPractiseManagementComponent]
})
export class BestPractiseManagementModule {}
