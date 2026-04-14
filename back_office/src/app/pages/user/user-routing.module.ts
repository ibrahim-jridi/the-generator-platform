import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'internal-user-management',
        loadChildren: () => import('./internal-user/internal-user.module').then((module) => module.InternalUserModule)
      },
      {
        path: 'external-user-management',
        loadChildren: () => import('./external-user/external-user.module').then((module) => module.ExternalUserModule)
      },

    ]
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
