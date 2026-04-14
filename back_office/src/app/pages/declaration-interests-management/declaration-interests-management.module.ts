import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../theme/shared/shared.module";
import {
  DeclarationInterestsManagementRoutingModule
} from "./declaration-interests-management-routing.module";
import {
  DeclarationInterestsManagementComponent
} from "./declaration-interests-management.component";
import {NgbDropdownModule, NgbPopoverModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import {FullCalendarModule} from "@fullcalendar/angular";

@NgModule({
  imports: [CommonModule, DeclarationInterestsManagementRoutingModule, SharedModule, NgbDropdownModule, NgbTooltipModule, NgbPopoverModule, FullCalendarModule],
  declarations: [DeclarationInterestsManagementComponent]
})
export class DeclarationInterestsManagementModule {
}
