import {NgModule, OnInit} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../theme/shared/shared.module";
import {DemoRoutingModule} from "./demo-routing.module";
import {DemoComponent} from "./demo.component";
import {NgbDropdownModule, NgbPopoverModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import {FullCalendarModule} from "@fullcalendar/angular";

@NgModule({
  imports: [CommonModule, DemoRoutingModule, SharedModule, NgbDropdownModule, NgbTooltipModule, NgbPopoverModule, FullCalendarModule],
  declarations: [DemoComponent]
})
export class DemoModule {
}
