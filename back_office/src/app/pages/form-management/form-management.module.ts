import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {CustomButtonModule, ModalModule} from 'src/app/theme/shared/components';
import {TranslateModule} from '@ngx-translate/core';
import {CustomTableModule} from '../../theme/shared/components/custom-table/custom-table.module';
import {FormManagementRoutingModule} from './form-management-routing.module';
import {FormManagementComponent} from './form-management.component';
import {FormManagementViewComponent} from './form-management-view/form-management-view.component';
import {FormioAppConfig, FormioModule} from '@formio/angular';
import {SharedModule} from "../../theme/shared/shared.module";
import {PrismService} from "../../shared/services/Prism.service";
import {FormioAuthConfig, FormioAuthService} from "@formio/angular/auth";
import {FormioResources} from "@formio/angular/resource";
import {BSConfig} from "../../app-config";
import {FormBuilderComponent} from "./form-builder/form-builder.component";

@NgModule({
  declarations: [
    FormManagementComponent,
    FormManagementViewComponent,
    FormBuilderComponent
  ],
  imports: [
    CommonModule,
    FormManagementRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    FormioModule,
    SharedModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [PrismService,
    FormioAuthService,
    FormioResources,
    {provide: FormioAppConfig, useValue: BSConfig.formioConfig},
    {
      provide: FormioAuthConfig, useValue: {
        login: {
          form: 'user/login'
        },
        register: {
          form: 'user/register'
        }
      }
    }

  ]
})
export class FormManagementModule { }
