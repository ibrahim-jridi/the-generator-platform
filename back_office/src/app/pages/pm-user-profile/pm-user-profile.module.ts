import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {CustomButtonModule, ModalModule} from "../../theme/shared/components";
import {ReactiveFormsModule} from "@angular/forms";
import {NgSelectModule} from "@ng-select/ng-select";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {SharedModule} from "../../theme/shared/shared.module";
import {PmUserProfileComponent} from "./pm-user-profile.component";
import {PMUserProfileRoutingModule} from "./pm-user-profile-routng.module";
import {MatProgressBar} from "@angular/material/progress-bar";



@NgModule({
  declarations: [PmUserProfileComponent],
  imports: [
    CommonModule,
    PMUserProfileRoutingModule,
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
export class PmUserProfileModule { }
