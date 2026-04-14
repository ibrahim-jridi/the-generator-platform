import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../theme/shared/shared.module';
import { RseEngagementManagementRoutingModule } from './rse-engagement-management-routing.module';
import { RseEngagementManagementComponent } from './rse-engagement-management.component';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [
    CommonModule,
    RseEngagementManagementRoutingModule,
    SharedModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbPopoverModule,
    FullCalendarModule
  ],
  declarations: [RseEngagementManagementComponent]
})
export class RseEngagementManagementModule {}
