import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../theme/shared/shared.module';
import { DigitalEngagementManagementRoutingModule } from './digital-engagement-management-routing.module';
import { DigitalEngagementManagementComponent } from './digital-engagement-management.component';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [
    CommonModule,
    DigitalEngagementManagementRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ],
  declarations: [DigitalEngagementManagementComponent]
})
export class DigitalEngagementManagementModule {}
