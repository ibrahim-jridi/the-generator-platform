import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../theme/shared/shared.module';
import { StartOfActivityRoutingModule } from './start-of-activity-routing.module';
import { StartOfActivityComponent } from './start-of-activity.component';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [CommonModule, StartOfActivityRoutingModule, SharedModule, NgbDropdownModule, NgbTooltipModule, NgbPopoverModule, FullCalendarModule],
  declarations: [StartOfActivityComponent]
})
export class StartOfActivityModule {}
