import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PhysicalUserProfileRoutingModule } from './physical-user-profile-routing.module';
import { PhysicalUserProfileComponent } from './physical-user-profile.component';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { CustomButtonModule, ModalModule } from 'src/app/theme/shared/components';
import { NgxIntlTelInputModule } from 'ngx-intl-tel-input';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [PhysicalUserProfileComponent],
  imports: [
    CommonModule,
    PhysicalUserProfileRoutingModule,
        TranslateModule,
        CustomTableModule,
        ModalModule,
        ReactiveFormsModule,
        NgSelectModule,
        NgxIntlTelInputModule,
        SharedModule,
        CustomButtonModule

  ]
})
export class PhysicalUserProfileModule { }
