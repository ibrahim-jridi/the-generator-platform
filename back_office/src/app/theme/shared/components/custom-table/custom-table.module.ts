import {CommonModule} from "@angular/common";
import {CardModule} from "../card/card.module";
import { NgbPaginationModule, NgbTooltipModule, NgbDropdownModule, NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ClickOutsideModule} from "ng-click-outside";
import {CustomTableComponent} from "./custom-table.component";
import {DataTablesModule} from "angular-datatables";
import {NgSelectModule} from "@ng-select/ng-select";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {NgModule} from "@angular/core";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepickerModule} from "@angular/material/datepicker";

@NgModule({
  imports: [
    CommonModule,
    CardModule,
    DataTablesModule,
    NgbTooltipModule,
    NgbPaginationModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    TranslateModule,
    ClickOutsideModule,
    NgbDropdownModule,
    NgbPopoverModule,
    MatOptionModule,
    MatSelectModule,
    MatDatepickerModule
  ],
  declarations: [CustomTableComponent],
  exports: [CustomTableComponent],
  providers: [TranslatePipe]
})
export class CustomTableModule {

}
