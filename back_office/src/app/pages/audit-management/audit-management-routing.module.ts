import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorAuditComponent} from "./error-audit/list-error-audit/error-audit.component";
import {ActivityAuditComponent} from "./activity-audit/list-activity-audit/activity-audit.component";
import {
  ViewActivityAuditComponent
} from "./activity-audit/view-activity-audit/view-activity-audit.component";
import {ViewErrorAuditComponent} from "./error-audit/view-error-audit/view-error-audit.component";


const routes: Routes = [
  {
    path: 'error-audit',
    component: ErrorAuditComponent,
  },
  {

    path: 'activity-audit',
    component: ActivityAuditComponent,
  },
  {
    path: 'activity-audit/view-activity-audit/:id',
    component: ViewActivityAuditComponent,
  },
  {
    path: 'error-audit/view-error-audit/:id',
    component: ViewErrorAuditComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class AuditManagementRoutingModule { }

