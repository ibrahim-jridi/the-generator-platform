import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormManagementComponent } from './form-management.component';
import { FormManagementViewComponent } from './form-management-view/form-management-view.component';
import { FormBuilderComponent } from './form-builder/form-builder.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'form-management',
    pathMatch: 'full'
  },
  {
    path: '',
    component: FormManagementComponent
  },
  {
    path: 'view-form/:id',
    component: FormManagementViewComponent
  },
  {
    path: 'update-form/:id',
    component: FormBuilderComponent
  },
  {
    path: 'form-builder/add',
    component: FormBuilderComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FormManagementRoutingModule { }
