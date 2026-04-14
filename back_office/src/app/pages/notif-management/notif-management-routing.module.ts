import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotifManagementComponent } from './notif-management.component';
import { NotifViewComponent } from './notif-view/notif-view.component';
import { NotifAddComponent } from './notif-add/notif-add.component';
import { NotifUpdateComponent } from './notif-update/notif-update.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'notif-management',
    pathMatch: 'full'
  },
  {
    path: '',
    component: NotifManagementComponent
  },
  {
    path: 'notif-view/:id',
    component: NotifViewComponent
  },
  {
    path: 'notif-add',
    component: NotifAddComponent
  },
  {
    path: 'notif-update/:id',
    component: NotifUpdateComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NotifManagementRoutingModule {}
