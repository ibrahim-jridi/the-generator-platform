import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ConfigTemplateReportRoutingModule} from './config-template-report-routing.module';
import {
  ListConfigTemplateReportComponent
} from './list-config-template-report/list-config-template-report.component';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {SharedModule} from "../../../theme/shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {
  EditConfigTemplateReportComponent
} from "./edit-config-template-report/edit-config-template-report.component";
import {NgbDropdownModule, NgbNavModule} from "@ng-bootstrap/ng-bootstrap";
import {NgScrollbar} from "ngx-scrollbar";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatStepperModule} from "@angular/material/stepper";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { ConfigTemplateReportComponent } from './config-template-report.component';


@NgModule({
  declarations: [
    ListConfigTemplateReportComponent,
    EditConfigTemplateReportComponent,
    ConfigTemplateReportComponent
  ],
    exports: [
        ListConfigTemplateReportComponent,
        ConfigTemplateReportComponent,
        EditConfigTemplateReportComponent
    ],
  imports: [
    CommonModule,
    ConfigTemplateReportRoutingModule,
    NgbNavModule,
    TranslateModule,
    SharedModule,
    NgScrollbar,
    MatOptionModule,
    MatSelectModule,
    MatProgressBarModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    NgbDropdownModule
  ]
})
export class ConfigTemplateReportModule { }
