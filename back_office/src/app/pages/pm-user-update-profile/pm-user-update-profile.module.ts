import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {PMUserProfileRoutingModule} from "../pm-user-profile/pm-user-profile-routng.module";
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {CustomButtonModule, ModalModule} from "../../theme/shared/components";
import {ReactiveFormsModule} from "@angular/forms";
import {NgSelectModule} from "@ng-select/ng-select";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {SharedModule} from "../../theme/shared/shared.module";
import {MatProgressBar} from "@angular/material/progress-bar";
import {PMUserUpdateProfileRoutingModule} from "./pm-user-update-profile-routing.module";
import {PmUserUpdateProfileComponent} from "./pm-user-update-profile.component";



@NgModule({
  declarations: [PmUserUpdateProfileComponent],
  imports: [
    CommonModule,
    CommonModule,
    PMUserUpdateProfileRoutingModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgxIntlTelInputModule,
    SharedModule,
    CustomButtonModule,
    MatProgressBar

  ]
})
export class PmUserUpdateProfileModule { }
