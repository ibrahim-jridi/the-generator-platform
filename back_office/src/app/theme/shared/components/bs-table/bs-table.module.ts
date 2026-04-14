import {CommonModule} from "@angular/common";
import {CardModule} from "../card/card.module";
import {NgbPaginationModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ClickOutsideModule} from "ng-click-outside";
import {BsTableComponent} from "./bs-table.component";
import {DataTablesModule} from "angular-datatables";
import {NgSelectModule} from "@ng-select/ng-select";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {NgModule} from "@angular/core";

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
    ClickOutsideModule
  ],
  declarations: [BsTableComponent],
  exports: [BsTableComponent],
  providers: [TranslatePipe]
})
export class BsTableModule {

}
