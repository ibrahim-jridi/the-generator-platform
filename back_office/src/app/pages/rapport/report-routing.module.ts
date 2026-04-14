import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportListComponent } from './report-list/report-list.component';
import { ReportAddComponent } from './report-add/report-add.component';
import { ReportUpdateComponent } from './report-update/report-update.component';
import { ReportViewComponent } from './report-view/report-view.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'report-management',
    pathMatch: 'full'
  },
  {
    path: '',
    component: ReportListComponent
  },
  {
    path: 'add',
    component: ReportAddComponent
  },
  {
    path: 'update-report/:id',
    component: ReportUpdateComponent
  },
  {
    path: 'view-report/:id',
    component: ReportViewComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RapportRoutingModule { }
