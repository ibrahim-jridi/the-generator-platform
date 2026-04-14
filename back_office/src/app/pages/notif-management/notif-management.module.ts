import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { CustomButtonModule, CustomSelectModule, ModalModule } from 'src/app/theme/shared/components';
import { NotifManagementRoutingModule } from './notif-management-routing.module';
import { NotifManagementComponent } from './notif-management.component';
import { NotifViewComponent } from './notif-view/notif-view.component';
import { NotifAddComponent } from './notif-add/notif-add.component';
import { NotifUpdateComponent } from './notif-update/notif-update.component';
import { SharedModule } from '../../theme/shared/shared.module';

@NgModule({
  declarations: [NotifManagementComponent, NotifViewComponent, NotifAddComponent, NotifUpdateComponent],
  imports: [
    CommonModule,
    NotifManagementRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    FormsModule,
    CustomSelectModule,
    SharedModule
  ]
})
export class NotifManagementModule {}
