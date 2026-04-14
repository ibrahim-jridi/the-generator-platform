import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PhysicalUserUpdateRoutingModule } from './physical-user-update-routing.module';
import {PhysicalUserUpdateComponent} from "./physical-user-update.component";
import {CustomButtonModule, CustomSelectModule, ModalModule} from "../../theme/shared/components";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatProgressBar} from "@angular/material/progress-bar";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {SharedModule} from "../../theme/shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [PhysicalUserUpdateComponent],
  imports: [
    CommonModule,
    PhysicalUserUpdateRoutingModule,
    CustomButtonModule,
    CustomSelectModule,
    FormsModule,
    MatProgressBar,
    ModalModule,
    NgxIntlTelInputModule,
    ReactiveFormsModule,
    SharedModule,
    TranslateModule
  ]
})
export class PhysicalUserUpdateModule { }
