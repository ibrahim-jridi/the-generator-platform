import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  ListConfigTemplateReportComponent
} from "./list-config-template-report/list-config-template-report.component";
import {
  EditConfigTemplateReportComponent
} from "./edit-config-template-report/edit-config-template-report.component";
const routes: Routes = [
  {
    path: 'list',
    component: ListConfigTemplateReportComponent
  },
  {
    path: 'edit-config',
    component: EditConfigTemplateReportComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigTemplateReportRoutingModule { }
