import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RoleRoutingModule } from './role-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { CustomButtonModule, ModalModule } from 'src/app/theme/shared/components';
import { TranslateModule } from '@ngx-translate/core';
import { RoleListComponent } from './role-list/role-list.component';
import { RoleAddComponent } from './role-add/role-add.component';
import { RoleUpdateComponent } from './role-update/role-update.component';
import { RoleViewComponent } from './role-view/role-view.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgScrollbar } from 'ngx-scrollbar';
import { SharedModule } from '../../theme/shared/shared.module';

@NgModule({
  declarations: [RoleListComponent, RoleAddComponent, RoleUpdateComponent, RoleViewComponent],
  imports: [
    CommonModule,
    RoleRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgScrollbar,
    SharedModule
  ]
})
export class RoleModule {}
