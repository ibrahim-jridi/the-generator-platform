import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    UserRoutingModule,
    NgxIntlTelInputModule
  ]
})
export class UserModule { }
