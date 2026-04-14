import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminComponent} from "./theme/layout/admin/admin.component";
import {AuthGuardAdmin} from "./shared/guards/auth-guard-admin";
import {ResetPasswordComponent} from "./auth/reset-password/reset-password.component";


const routes: Routes = [
  {
    path: '',
    redirectTo: 'pages',
    pathMatch: 'full'
  },
  {
    path: 'forgot-password/:token',
    component: ResetPasswordComponent
  },
  {
    path: 'pages',
    component: AdminComponent,
    canActivateChild: [AuthGuardAdmin],
    loadChildren: () => import('./pages/pages.module').then((module) => module.PagesModule)
  },
  {
    path: 'redirect/ectd',
    redirectTo: '/pages/ectd',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
