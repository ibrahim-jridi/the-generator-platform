import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuditManagementRoutingModule } from './audit-management-routing.module';
import { ActivityAuditComponent } from './activity-audit/list-activity-audit/activity-audit.component';
import {TranslateModule} from "@ngx-translate/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MatSelectModule } from '@angular/material/select';
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {ErrorAuditComponent} from "./error-audit/list-error-audit/error-audit.component";
import {
  ViewActivityAuditComponent
} from "./activity-audit/view-activity-audit/view-activity-audit.component";

@NgModule({
  declarations: [
    ErrorAuditComponent,
    ActivityAuditComponent,
    ViewActivityAuditComponent
  ],
  imports: [
    CommonModule,
    AuditManagementRoutingModule,
    FormsModule,
    MatSelectModule,
    TranslateModule,
    CustomTableModule,
    TranslateModule,
    ReactiveFormsModule
  ]
})
export class AuditManagementModule { }
