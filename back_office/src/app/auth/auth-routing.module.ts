import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {WelcomePageComponent} from './welcome-page/welcome-page.component';
import {ResetPasswordComponent} from './reset-password/reset-password.component';
import {LoginMoralPersonComponent} from './login-moral-person/login-moral-person.component';
import {SignupComponent} from './signup/signup.component';
import {MailVerificationComponent} from "./mail-verification/mail-verification.component";
import {MailActivationComponent} from "./mail-activation/mail-activation.component";
import {
  ResetPasswordRequestComponent
} from "./reset-password/reset-password-request/reset-password-request.component";
import {PreSignupComponent} from "./pre-signup/pre-signup.component";
import { RneLoginComponent } from './rne-login/rne-login.component';
import {LoginAdminComponent} from "./login-admin/login-admin.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'welcome',
        pathMatch: 'full'
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'welcome',
        component: WelcomePageComponent
      },
      {
        path: 'forgot-password',
        component: ResetPasswordComponent
      },
      {
        path: 'login-moral-person',
        component: LoginMoralPersonComponent
      },
      {
        path: 'signup',
        component: SignupComponent
      },
      {
        path: 'pre-signup',
        component: PreSignupComponent
      },
      {
        path: 'verify-mail',
        component: MailVerificationComponent
      },
      {
        path:'activation-mail',
        component: MailActivationComponent
      },
      {
        path: 'reset-password-request',
        component: ResetPasswordRequestComponent
      },
      {
        path: 'rne-login',
        component: RneLoginComponent
      },
      {
        path: 'login-admin',
        component: LoginAdminComponent
      },
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
