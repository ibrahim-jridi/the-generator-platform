import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternalUserRoutingModule } from './internal-user-routing.module';
import {InternalUserListComponent} from "./internal-user-list/internal-user-list.component";
import {CustomButtonModule, ModalModule , CustomSelectModule} from "../../../theme/shared/components";
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../../theme/shared/components/custom-table/custom-table.module";
import {InternalUserAddComponent} from "./internal-user-add/internal-user-add.component";
import {ReactiveFormsModule} from "@angular/forms";
import {InternalUserViewComponent} from "./internal-user-view/internal-user-view.component";
import {InternalUserUpdateComponent} from "./internal-user-update/internal-user-update.component";
import { NgSelectModule } from '@ng-select/ng-select';
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {BsDatepickerModule} from "ngx-bootstrap/datepicker";
import {SharedModule} from "../../../theme/shared/shared.module";
import {MatProgressBar} from "@angular/material/progress-bar";

@NgModule({
  declarations: [InternalUserListComponent, InternalUserAddComponent, InternalUserViewComponent, InternalUserUpdateComponent],
  imports: [
    CommonModule,
    InternalUserRoutingModule,
    CustomButtonModule,
    CustomSelectModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgxIntlTelInputModule,
    BsDatepickerModule,
    SharedModule,
    MatProgressBar
  ]
})
export class InternalUserModule {}
