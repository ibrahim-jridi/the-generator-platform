import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ExternalUserRoutingModule } from './external-user-routing.module';
import { ExternalUserListComponent } from './external-user-list/external-user-list.component';
import { CustomButtonModule, ModalModule } from 'src/app/theme/shared/components';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../../theme/shared/components/custom-table/custom-table.module';
import { ExternalUserAddComponent } from './external-user-add/external-user-add.component';
import { ExternalUserUpdateComponent } from './external-user-update/external-user-update.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ExternalUserViewComponent } from './external-user-view/external-user-view.component';


@NgModule({
  declarations: [
    ExternalUserListComponent,
    ExternalUserAddComponent,
    ExternalUserUpdateComponent,
    ExternalUserViewComponent
  ],
  imports: [
    CommonModule,
    ExternalUserRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ReactiveFormsModule,
    ModalModule,
  ]
})
export class ExternalUserModule { }
