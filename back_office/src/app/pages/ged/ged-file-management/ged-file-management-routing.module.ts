import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AllFileMangementComponent } from './all-file-mangement/all-file-mangement.component';
import { MyFileMangementComponent } from './my-file-mangement/my-file-mangement.component';


const routes: Routes = [{
  path: '',

  children: [
    {
      path: 'file-manager',
      component: AllFileMangementComponent,
      data: {
        breadcrumb: 'File Manager',
        status: true}
    },
    {
      path: 'my-file',
      component: MyFileMangementComponent,
      data: {
        breadcrumb: 'My Files',
        status: true}
    },

  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GedFileManagementRoutingModule { }
