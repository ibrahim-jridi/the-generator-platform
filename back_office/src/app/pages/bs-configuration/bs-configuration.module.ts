import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BsConfigurationRoutingModule } from './bs-configuration-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import {NgbCollapse, NgbDropdownModule, NgbNavModule} from '@ng-bootstrap/ng-bootstrap';
import { NgScrollbar } from 'ngx-scrollbar';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import {BsConfigurationComponent} from "./bs-configuration.component";
import {
  ConfigReportElementComponent
} from "./config-report-element/config-report-element.component";
import {ConfigTemplateReportModule} from "./config-template-report/config-template-report.module";
import {CostServiceCreationComponent} from "./config-cost-service/cost-service-creation/cost-service-creation.component";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {ListCostServicesComponent} from "./config-cost-service/list-cost-services/list-cost-services.component";
import {DetailCostServiceComponent} from "./config-cost-service/detail-cost-service/detail-cost-service.component";
@NgModule({
  declarations: [
      BsConfigurationComponent,
      ConfigReportElementComponent,
      CostServiceCreationComponent
  ],
  imports: [
    CommonModule,
    BsConfigurationRoutingModule,
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
    NgbDropdownModule,
    ConfigTemplateReportModule,
    NgxIntlTelInputModule,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    ListCostServicesComponent,
    NgbCollapse,
    DetailCostServiceComponent,
  ],
})
export class BsConfigurationModule {}
